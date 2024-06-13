package br.com.itau.challenge.dataprovider.http.account.impl.response

import br.com.itau.challenge.usecase.entity.AccountEntity
import java.math.BigDecimal

data class AccountResponse(
    val id: String,
    val saldo: BigDecimal,
    val ativo: Boolean,
    val limiteDiario: BigDecimal
)

fun AccountResponse.toEntity() = AccountEntity(
    id = this.id,
    saldo = this.saldo,
    ativo = this.ativo,
    limiteDiario = this.limiteDiario
)