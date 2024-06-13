package br.com.itau.challenge.dataprovider.http.client.impl

import br.com.itau.challenge.config.BaseWireMockTest
import br.com.itau.challenge.exception.ClientNotFoundException
import br.com.itau.challenge.factory.FactoryResponse.Companion.generateClientJson
import com.github.tomakehurst.wiremock.client.WireMock
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus

class ClientHttpProviderImplTest : BaseWireMockTest() {

    private val provider = ClientHttpProviderImpl(
        MOCK_HOST_SERVER
    )

    @Test
    fun `should get 2xx status when exists client`() {
        val id = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f"
        mockGet(
            "/clientes/$id",
            HttpStatus.OK,
            generateClientJson()
        )

        runBlocking {
            val result = provider.getClientById(id)
            Assertions.assertEquals(
                id,
                result.id
            )
        }

        mockServer.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/clientes/$id")))
    }

    @Test
    fun `should get 4xx status when don't exists client`() {
        val id = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f"
        mockGet(
            "/clientes/$id",
            HttpStatus.NOT_FOUND,
            generateClientJson()
        )

        runBlocking {
            assertThrows<ClientNotFoundException> {
                val result = provider.getClientById(id)
                Assertions.assertEquals(
                    id,
                    result.id
                )
            }
        }

        mockServer.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/clientes/$id")))
    }
}