package br.com.itau.challenge.dataprovider.http.bacen.impl

import br.com.itau.challenge.config.BaseWireMockTest
import br.com.itau.challenge.exception.BacenMultiplesRequestsException
import com.github.tomakehurst.wiremock.client.WireMock
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus

internal class BacenHttpProviderImplTest : BaseWireMockTest() {

    private val provider = BacenHttpProviderImpl(
        MOCK_HOST_SERVER
    )

    @Test
    fun `should return 2xx status when the notification has success`() {
        mockPost(
            "/notify",
            HttpStatus.OK,
            null
        )

        runBlocking {
            provider.notifyPostTransfer()
        }

        mockServer.verify(WireMock.postRequestedFor(WireMock.urlEqualTo("/notify")))
    }

    @Test
    fun `should return 429 status when bacen has recieve multiples requests`() {
        mockPost(
            "/notify",
            HttpStatus.TOO_MANY_REQUESTS,
            null
        )

        runBlocking {
            assertThrows<BacenMultiplesRequestsException> {
                provider.notifyPostTransfer()
            }
        }

        mockServer.verify(WireMock.postRequestedFor(WireMock.urlEqualTo("/notify")))
    }
}