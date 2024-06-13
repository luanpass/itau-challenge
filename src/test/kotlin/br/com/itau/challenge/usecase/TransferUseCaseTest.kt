package br.com.itau.challenge.usecase

import br.com.itau.challenge.dataprovider.http.account.AccountHttpProvider
import br.com.itau.challenge.dataprovider.http.bacen.BacenHttpProvider
import br.com.itau.challenge.dataprovider.http.client.ClientHttpProvider
import br.com.itau.challenge.exception.*
import br.com.itau.challenge.factory.FactoryEntity.Companion.generateAccountEntity
import br.com.itau.challenge.factory.FactoryEntity.Companion.generateAccountEntityDailyLimitZero
import br.com.itau.challenge.factory.FactoryEntity.Companion.generateAccountEntityDeactivate
import br.com.itau.challenge.factory.FactoryEntity.Companion.generateClientEntity
import br.com.itau.challenge.factory.FactoryEntity.Companion.generateTransferenciaEntity
import br.com.itau.challenge.factory.FactoryEntity.Companion.generateTransferenciaEntityBalanceOneHundred
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TransferUseCaseTest {
    private val accountProvider: AccountHttpProvider = mockk()
    private val bacenProvider: BacenHttpProvider = mockk()
    private val clientProvider: ClientHttpProvider = mockk()

    private val useCase = TransferUseCase(
        accountProvider = accountProvider,
        bacenProvider = bacenProvider,
        clientProvider = clientProvider
    )

    @Test
    fun `should finish with success`() {
        val transfer = generateTransferenciaEntity()

        coEvery { accountProvider.getAccountById(transfer.conta.idOrigem) } returns generateAccountEntity()
        coEvery { accountProvider.updateBalance() } just runs
        coEvery { bacenProvider.notifyPostTransfer() } just runs
        coEvery { clientProvider.getClientById(transfer.idCliente) } returns generateClientEntity()

        runBlocking {
            useCase.transferClientToClient(transfer)
        }
    }

    @Test
    fun `should finish with error when not found the account`() {
        val transfer = generateTransferenciaEntity()

        coEvery { accountProvider.getAccountById(transfer.conta.idOrigem) } throws NotFoundException("Not Found")
        coEvery { accountProvider.updateBalance() } just runs
        coEvery { bacenProvider.notifyPostTransfer() } just runs
        coEvery { clientProvider.getClientById(transfer.idCliente) } returns generateClientEntity()

        assertThrows(NotFoundException::class.java) {
            runBlocking {
                useCase.transferClientToClient(transfer)
            }
        }
    }

    @Test
    fun `should finish with error when not found the client`() {
        val transfer = generateTransferenciaEntity()

        coEvery { accountProvider.getAccountById(transfer.conta.idOrigem) } returns generateAccountEntity()
        coEvery { accountProvider.updateBalance() } just runs
        coEvery { bacenProvider.notifyPostTransfer() } just runs
        coEvery { clientProvider.getClientById(transfer.idCliente) } throws ClientNotFoundException("Cliente não encontrado")

        assertThrows(ClientNotFoundException::class.java) {
            runBlocking {
                useCase.transferClientToClient(transfer)
            }
        }
    }

    @Test
    fun `should finish with error when the client no has balance`() {
        val transfer = generateTransferenciaEntityBalanceOneHundred()

        coEvery { accountProvider.getAccountById(transfer.conta.idOrigem) } returns generateAccountEntity()
        coEvery { accountProvider.updateBalance() } just runs
        coEvery { bacenProvider.notifyPostTransfer() } just runs
        coEvery { clientProvider.getClientById(transfer.idCliente) } returns generateClientEntity()

        assertThrows(InsufficientBalanceException::class.java) {
            runBlocking {
                useCase.transferClientToClient(transfer)
            }
        }
    }

    @Test
    fun `should finish with error when the client no has daily limit`() {
        val transfer = generateTransferenciaEntity()

        coEvery { accountProvider.getAccountById(transfer.conta.idOrigem) } returns generateAccountEntityDailyLimitZero()
        coEvery { accountProvider.updateBalance() } just runs
        coEvery { bacenProvider.notifyPostTransfer() } just runs
        coEvery { clientProvider.getClientById(transfer.idCliente) } returns generateClientEntity()

        assertThrows(DailyLimitExceededException::class.java) {
            runBlocking {
                useCase.transferClientToClient(transfer)
            }
        }
    }

    @Test
    fun `should finish with error when the account is deactivate`() {
        val transfer = generateTransferenciaEntity()

        coEvery { accountProvider.getAccountById(transfer.conta.idOrigem) } returns generateAccountEntityDeactivate()
        coEvery { accountProvider.updateBalance() } just runs
        coEvery { bacenProvider.notifyPostTransfer() } just runs
        coEvery { clientProvider.getClientById(transfer.idCliente) } returns generateClientEntity()

        assertThrows(AccountDeactivateException::class.java) {
            runBlocking {
                useCase.transferClientToClient(transfer)
            }
        }
    }
}