package br.com.itau.challenge.entrypoint.http.transfer

import br.com.itau.challenge.entrypoint.http.handler.response.ErrorResponse
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.TransferenciaRequestDTO
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.TransferenciaResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

interface TransferController {
    @Operation(
        tags = ["Transferência"],
        summary = "Realiza a transferência de saldo entre dois clientes válidos no banco Itau",
        description = """Para realizar essa transferência, validamos se o cliente de destino é valido. A conta origem precisa ter um saldo e um limite diario maior que o valor a ser transferido. Em caso de sucesso, notificaremos o BACEN""",
        responses = [
            ApiResponse(
                description = "Resposta com sucesso",
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = TransferenciaResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
            ApiResponse(
                description = "Resposta em caso de cliente não encontrado",
                responseCode = "404",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
            ApiResponse(
                description = "Resposta em caso de conta desativada",
                responseCode = "403",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
            ApiResponse(
                description = "Resposta em caso de saldo insuficiente ou limite diario atingido",
                responseCode = "402",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        mediaType = "application/json"
                    )
                ]
            )
        ]
    )
    suspend fun transferCustomerToCustomer(@RequestBody transferenciaRequestDTO: TransferenciaRequestDTO): ResponseEntity<TransferenciaResponse>
}