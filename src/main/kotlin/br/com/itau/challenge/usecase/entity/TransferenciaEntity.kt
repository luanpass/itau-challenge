package br.com.itau.challenge.usecase.entity

import java.math.BigDecimal

data class TransferenciaEntity(
    val idCliente: String,
    val valor: BigDecimal,
    val conta: ContaEntity
)

data class ContaEntity(
    val idOrigem: String,
    val idDestino: String
)