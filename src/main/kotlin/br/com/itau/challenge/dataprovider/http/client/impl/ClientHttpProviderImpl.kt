package br.com.itau.challenge.dataprovider.http.client.impl

import br.com.itau.challenge.dataprovider.http.client.ClientHttpProvider
import br.com.itau.challenge.dataprovider.http.client.impl.response.ClientResponse
import br.com.itau.challenge.dataprovider.http.client.impl.response.toEntity
import br.com.itau.challenge.exception.ClientNotFoundException
import br.com.itau.challenge.extensions.typeReference
import br.com.itau.challenge.usecase.entity.ClientEntity
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ClientHttpProviderImpl(
    @Value("\${services.url}")
    private val url: String
) : ClientHttpProvider {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun getClientById(clientId: String): ClientEntity {
        log.info("m=getClientById, stage=init, clientId=$clientId")

        return WebClient.create("$url/clientes/$clientId")
            .get()
            .retrieve()
            .onStatus({ status -> status == HttpStatus.NOT_FOUND }, {
                Mono.error(ClientNotFoundException("Cliente não encontrado com id=$clientId"))
            })
            .bodyToMono(typeReference<ClientResponse>())
            .map { it.toEntity() }
            .awaitSingle()
    }

}
