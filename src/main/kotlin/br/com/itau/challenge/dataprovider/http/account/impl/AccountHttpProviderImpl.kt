package br.com.itau.challenge.dataprovider.http.account.impl

import br.com.itau.challenge.dataprovider.http.account.AccountHttpProvider
import br.com.itau.challenge.dataprovider.http.account.impl.response.AccountResponse
import br.com.itau.challenge.dataprovider.http.account.impl.response.toEntity
import br.com.itau.challenge.exception.BadRequestException
import br.com.itau.challenge.exception.NotFoundException
import br.com.itau.challenge.extensions.typeReference
import br.com.itau.challenge.usecase.entity.AccountEntity
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono

@Component
class AccountHttpProviderImpl(
    @Value("\${services.url}")
    private val url: String
) : AccountHttpProvider {

    private val log = LoggerFactory.getLogger(javaClass)

    override suspend fun getAccountById(id: String): AccountEntity {
        log.info("m=getAccountById, stage=init, clientId=$id")

        return WebClient.create("$url/contas/$id")
            .get()
            .retrieve()
            .onStatus({ status -> status == HttpStatus.NOT_FOUND }, {
                Mono.error(NotFoundException("Not found"))
            })
            .bodyToMono(typeReference<AccountResponse>())
            .map { it.toEntity() }
            .awaitSingle()
    }

    override suspend fun updateBalance() {
        log.info("m=updateBalance, stage=init")

        return WebClient.create("$url/contas/saldos")
            .put()
            .retrieve()
            .onStatus({ status -> status.is4xxClientError || status.is5xxServerError}, {
                Mono.error(BadRequestException("Error genérico"))
            })
            .awaitBody()
    }
}