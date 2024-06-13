package br.com.itau.challenge.usecase

import br.com.itau.challenge.dataprovider.http.account.AccountHttpProvider
import br.com.itau.challenge.dataprovider.http.bacen.BacenHttpProvider
import br.com.itau.challenge.dataprovider.http.client.ClientHttpProvider
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.TransferenciaResponse
import br.com.itau.challenge.exception.AccountDeactivateException
import br.com.itau.challenge.exception.BacenMultiplesRequestsException
import br.com.itau.challenge.exception.DailyLimitExceededException
import br.com.itau.challenge.exception.InsufficientBalanceException
import br.com.itau.challenge.usecase.entity.AccountEntity
import br.com.itau.challenge.usecase.entity.ClientEntity
import br.com.itau.challenge.usecase.entity.TransferenciaEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*

@Component
class TransferUseCase(
    private val accountProvider: AccountHttpProvider,
    private val bacenProvider: BacenHttpProvider,
    private val clientProvider: ClientHttpProvider,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun transferClientToClient(transfer: TransferenciaEntity) = coroutineScope {
        log.info("m=transferCustomerToCustomer, stage=init, clientId=${transfer.idCliente}")

        withContext(context = Dispatchers.IO) {
            val allPromises = awaitAll(
                async(defaultDispatcher) { accountProvider.getAccountById(transfer.conta.idOrigem) },
                async(defaultDispatcher) { clientProvider.getClientById(transfer.idCliente) }
            )

            val accountReceive = allPromises[0] as AccountEntity
            val clientTransfer = allPromises[1] as ClientEntity

            log.info("m=transferCustomerToCustomer, msg=Cliente validado, clientId=${clientTransfer.id}")

            validateAccountIsEnabled(accountReceive)
            validateAccountBalance(accountReceive, transfer.valor)
            checkDailyLimit(accountReceive, transfer.valor)

            accountProvider.updateBalance()

            log.info("m=transferCustomerToCustomer, stage=finish, clientId=${transfer.idCliente}")
        }

        GlobalScope.launch {
            flow {
                emit(bacenProvider.notifyPostTransfer())
            }.retryWhen { cause, _ ->
                if (cause is BacenMultiplesRequestsException) {
                    delay(10000)
                    return@retryWhen true
                }
                return@retryWhen false

                // Na vida real esse trecho de código não iria suprir todas as necessidades,
                // Aqui ficaria segurando uma courotine até concluir a requisição com sucesso.
                // Em um projeto real, colocaria um attempt de 10, caso chegasse no limite, notificaria ou salvaria em um banco o erro para retentar
            }
        }
        TransferenciaResponse(UUID.randomUUID())
    }

    private fun validateAccountIsEnabled(account: AccountEntity) {
        log.info("m=validateAccountIsEnabled, stage=init, clientId=${account.id}")
        if (!account.ativo) throw AccountDeactivateException("Conta desativada, id =${account.id}")
        log.info("m=validateAccountIsEnabled, stage=finish, clientId=${account.id}")
    }

    private fun validateAccountBalance(account: AccountEntity, valueToTransfer: BigDecimal) {
        log.info("m=validateAccountBalance, stage=init, clientId=${account.id}, saldo=${account.saldo}, valor=${valueToTransfer}")
        if (account.saldo.minus(valueToTransfer) < BigDecimal.ZERO) throw InsufficientBalanceException(
            "Saldo insuficiente para realizar a transação, " +
                    "account=${account.id}, " +
                    "balance= ${account.saldo}, " +
                    "transferência = $valueToTransfer"
        )
        log.info("m=validateAccountBalance, stage=finish, clientId=${account.id}, saldo=${account.saldo}, valor=${valueToTransfer}")

    }

    private fun checkDailyLimit(account: AccountEntity, valueToTransfer: BigDecimal) {
        log.info("m=checkDailyLimit, stage=finish, clientId=${account.id}, saldo=${account.saldo}, valor=${valueToTransfer}")
        if (account.limiteDiario.minus(valueToTransfer) < BigDecimal.ZERO) throw DailyLimitExceededException(
            "Limite diário insuficiente para realizar a transação, " +
                    "account=${account.id}, " +
                    "balance= ${account.saldo}," +
                    "transferência = $valueToTransfer"
        )
        log.info("m=checkDailyLimit, stage=finish, clientId=${account.id}, saldo=${account.saldo}, valor=${valueToTransfer}")
    }
}
