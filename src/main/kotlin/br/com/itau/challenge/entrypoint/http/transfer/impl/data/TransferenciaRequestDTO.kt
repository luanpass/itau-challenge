package br.com.itau.challenge.entrypoint.http.transfer.impl.data

import br.com.itau.challenge.usecase.entity.ContaEntity
import br.com.itau.challenge.usecase.entity.TransferenciaEntity
import java.math.BigDecimal

data class TransferenciaRequestDTO(
    val idCliente: String,
    val valor: BigDecimal,
    val conta: ContaRequestDTO
)

data class ContaRequestDTO(
    val idOrigem: String,
    val idDestino: String
)

fun TransferenciaRequestDTO.toEntity() = TransferenciaEntity(
    idCliente = this.idCliente,
    valor = this.valor,
    conta = ContaEntity(
        idOrigem = this.conta.idOrigem,
        idDestino = this.conta.idDestino
    )
)
