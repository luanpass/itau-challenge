package br.com.itau.challenge.dataprovider.http.account.impl

import br.com.itau.challenge.config.BaseWireMockTest
import br.com.itau.challenge.exception.BadRequestException
import br.com.itau.challenge.exception.NotFoundException
import br.com.itau.challenge.factory.FactoryResponse.Companion.generateAccountJson
import com.github.tomakehurst.wiremock.client.WireMock
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus

internal class AccountHttpProviderImplTest : BaseWireMockTest() {

    private val provider = AccountHttpProviderImpl(
        MOCK_HOST_SERVER
    )

    @Test
    fun `should get 2xx status when exists account`() {
        val id = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f"
        mockGet(
            "/contas/$id",
            HttpStatus.OK,
            generateAccountJson()
        )

        runBlocking {
            val result = provider.getAccountById(id)
            assertEquals(
                id,
                result.id
            )
        }

        mockServer.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/contas/$id")))
    }

    @Test
    fun `should get 4xx status when don't exists client`() {
        val id = "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f"
        mockGet(
            "/contas/$id",
            HttpStatus.NOT_FOUND,
            generateAccountJson()
        )

        runBlocking {
            assertThrows<NotFoundException> {
                val result = provider.getAccountById(id)
                assertEquals(
                    id,
                    result.id
                )
            }
        }

        mockServer.verify(WireMock.getRequestedFor(WireMock.urlEqualTo("/contas/$id")))
    }

    @Test
    fun `should return 2xx status update balance`() {
        mockPut(
            "/contas/saldos",
            HttpStatus.OK,
            null
        )

        runBlocking {
            provider.updateBalance()
        }

        mockServer.verify(WireMock.putRequestedFor(WireMock.urlEqualTo("/contas/saldos")))
    }

    @Test
    fun `should return 4xx status update balance`() {
        mockPut(
            "/contas/saldos",
            HttpStatus.BAD_REQUEST,
            null
        )

        runBlocking {
            assertThrows<BadRequestException> {
                provider.updateBalance()
            }
        }

        mockServer.verify(WireMock.putRequestedFor(WireMock.urlEqualTo("/contas/saldos")))
    }

    @Test
    fun `should return 5xx status update balance`() {
        mockPut(
            "/contas/saldos",
            HttpStatus.BAD_GATEWAY,
            null
        )

        runBlocking {
            assertThrows<BadRequestException> {
                provider.updateBalance()
            }
        }

        mockServer.verify(WireMock.putRequestedFor(WireMock.urlEqualTo("/contas/saldos")))
    }
}