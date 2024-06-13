package br.com.itau.challenge.dataprovider.http.bacen.impl

import br.com.itau.challenge.dataprovider.http.bacen.BacenHttpProvider
import br.com.itau.challenge.exception.BacenMultiplesRequestsException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono

@Component
class BacenHttpProviderImpl(
    @Value("\${services.url}")
    private val url: String
) : BacenHttpProvider {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun notifyPostTransfer() {
        log.info("m=notifyPostTransfer, stage=init")

        return WebClient.create("$url/notify")
            .post()
            .retrieve()
            .onStatus({ status -> status == HttpStatus.TOO_MANY_REQUESTS }, {
                log.error("m=notifyPostTransfer, stage=error")
                Mono.error(BacenMultiplesRequestsException("Erro 429, rate limit atingido"))
            })
            .awaitBody()
    }
}
