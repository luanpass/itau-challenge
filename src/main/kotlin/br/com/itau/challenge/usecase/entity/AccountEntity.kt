package br.com.itau.challenge.usecase.entity

import java.math.BigDecimal

data class AccountEntity(
    val id: String,
    val saldo: BigDecimal,
    val ativo: Boolean,
    val limiteDiario: BigDecimal
)
