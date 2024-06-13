package br.com.itau.challenge.config

import br.com.itau.challenge.config.web.client.WebClientConfig
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

abstract class BaseWireMockTest {

    lateinit var mockServer: WireMockServer

    companion object {
        const val MOCK_SERVER_PORT: Int = 9090
        const val MOCK_HOST_SERVER = "http://localhost:$MOCK_SERVER_PORT"
        val WEBCLIENT = WebClientConfig().webClientElasticSearch()
    }

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val mockServerOptions = WireMockConfiguration.options().port(MOCK_SERVER_PORT)
        mockServer = WireMockServer(mockServerOptions)
        mockServer.start()
    }

    @AfterEach
    fun tearsDown() {
        mockServer.stop()
    }

    fun mockGet(url: String, status: HttpStatus, body: String?) {
        mockServer.stubFor(
            WireMock.get(WireMock.urlEqualTo(url))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(status.value())
                        .withHeader("content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            body
                        )
                )
        )
    }

    fun mockPost(url: String, status: HttpStatus, body: String? = null) {
        mockServer.stubFor(
            WireMock.post(WireMock.urlEqualTo(url))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(status.value())
                        .withHeader("content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            body ?: "{}"
                        )
                )
        )
    }

    fun mockPut(url: String, status: HttpStatus, body: String?) {
        mockServer.stubFor(
            WireMock.put(WireMock.urlEqualTo(url))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(status.value())
                        .withHeader("content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            body
                        )
                )
        )
    }

    fun mockDelete(url: String, status: HttpStatus, body: String?) {
        mockServer.stubFor(
            WireMock.delete(WireMock.urlEqualTo(url))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(status.value())
                        .withHeader("content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                            body
                        )
                )
        )
    }

    fun mockPatch(url: String, status: HttpStatus) {
        mockServer.stubFor(
            WireMock.patch(WireMock.urlEqualTo(url))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(status.value())
                        .withHeader("content-type", MediaType.APPLICATION_JSON_VALUE)
                )
        )
    }
}