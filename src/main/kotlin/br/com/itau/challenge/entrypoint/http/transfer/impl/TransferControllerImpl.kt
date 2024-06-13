package br.com.itau.challenge.entrypoint.http.transfer.impl

import br.com.itau.challenge.entrypoint.http.transfer.TransferController
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.TransferenciaRequestDTO
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.TransferenciaResponse
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.toEntity
import br.com.itau.challenge.usecase.TransferUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/transferencia")
class TransferControllerImpl(
    private val useCase: TransferUseCase
) : TransferController {


    @PostMapping()
    override suspend fun transferCustomerToCustomer(@RequestBody transferenciaRequestDTO: TransferenciaRequestDTO): ResponseEntity<TransferenciaResponse> {
        return ResponseEntity.ok().body(useCase.transferClientToClient(transferenciaRequestDTO.toEntity()))
    }
}