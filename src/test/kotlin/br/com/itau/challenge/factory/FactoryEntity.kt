package br.com.itau.challenge.factory

import br.com.itau.challenge.usecase.entity.AccountEntity
import br.com.itau.challenge.usecase.entity.ClientEntity
import br.com.itau.challenge.usecase.entity.ContaEntity
import br.com.itau.challenge.usecase.entity.TransferenciaEntity
import java.math.BigDecimal

class FactoryEntity {
    companion object {
        fun generateTransferenciaEntity(): TransferenciaEntity {
            return TransferenciaEntity(
                idCliente = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
                valor = BigDecimal.TEN,
                conta = ContaEntity(
                    idOrigem = "d0d32142-74b7-4aca-9c68-838aeacef96b",
                    idDestino = "41313d7b-bd75-4c75-9dea-1f4be434007f"
                )
            )
        }

        fun generateTransferenciaEntityBalanceOneHundred(): TransferenciaEntity {
            return TransferenciaEntity(
                idCliente = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
                valor = BigDecimal.valueOf(1000),
                conta = ContaEntity(
                    idOrigem = "d0d32142-74b7-4aca-9c68-838aeacef96b",
                    idDestino = "41313d7b-bd75-4c75-9dea-1f4be434007f"
                )
            )
        }

        fun generateAccountEntity(): AccountEntity {
            return AccountEntity(
                id = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
                saldo = BigDecimal.TEN,
                ativo = true,
                limiteDiario = BigDecimal.TEN
            )
        }

        fun generateAccountEntityDeactivate(): AccountEntity {
            return AccountEntity(
                id = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
                saldo = BigDecimal.TEN,
                ativo = false,
                limiteDiario = BigDecimal.TEN
            )
        }

        fun generateAccountEntityDailyLimitZero(): AccountEntity {
            return AccountEntity(
                id = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
                saldo = BigDecimal.TEN,
                ativo = true,
                limiteDiario = BigDecimal.ZERO
            )
        }

        fun generateClientEntity(): ClientEntity {
            return ClientEntity(
                id = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
                nome = "Artur Carneiro",
                telefone = "987651234",
                tipoPessoa = "Fisica"
            )
        }
    }
}