package br.com.itau.challenge.factory

import br.com.itau.challenge.entrypoint.http.transfer.impl.data.ContaRequestDTO
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.TransferenciaRequestDTO
import java.math.BigDecimal

class FactoryDTO {

    companion object {
        fun generateTransferenciaRequestDTO() = TransferenciaRequestDTO(
            idCliente = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f",
            valor = BigDecimal.TEN,
            conta = ContaRequestDTO(
                idOrigem = "d0d32142-74b7-4aca-9c68-838aeacef96b",
                idDestino = "41313d7b-bd75-4c75-9dea-1f4be434007f"
            ),
        )
    }
}