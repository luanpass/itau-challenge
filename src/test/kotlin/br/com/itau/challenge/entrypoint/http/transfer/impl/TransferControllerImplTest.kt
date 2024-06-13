package br.com.itau.challenge.entrypoint.http.transfer.impl

import br.com.itau.challenge.entrypoint.http.handler.response.ErrorResponse
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.TransferenciaResponse
import br.com.itau.challenge.entrypoint.http.transfer.impl.data.toEntity
import br.com.itau.challenge.exception.InsufficientBalanceException
import br.com.itau.challenge.factory.FactoryDTO.Companion.generateTransferenciaRequestDTO
import br.com.itau.challenge.usecase.TransferUseCase
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@DisplayName(value = "TransferController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransferUseCaseTest {

    @Autowired
    lateinit var webClient: WebTestClient
    @MockkBean
    lateinit var useCase: TransferUseCase

    @Test
    fun `should get 2xx when transfer has success`() {
        val mock = generateTransferenciaRequestDTO()

        coEvery { useCase.transferClientToClient(mock.toEntity()) } returns TransferenciaResponse(UUID.randomUUID())

        webClient.post()
            .uri("/transferencia")
            .bodyValue(mock)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(TransferenciaResponse::class.java)
    }

    @Test
    fun `should get 4xx when transfer no has success`() {
        val mock = generateTransferenciaRequestDTO()

        coEvery { useCase.transferClientToClient(mock.toEntity()) } throws InsufficientBalanceException("Test")

        webClient.post()
            .uri("/transferencia")
            .bodyValue(mock)
            .exchange()
            .expectStatus().is4xxClientError
            .expectBody(ErrorResponse::class.java)
            .isEqualTo(ErrorResponse("Test"))
    }
}
