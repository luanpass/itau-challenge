package br.com.itau.challenge.config.web.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider

@Configuration
class WebClientConfig {

    @Bean
    fun webClientElasticSearch(): WebClient {
        val connectionProvider = ConnectionProvider.builder("webClientProvider")
            .maxConnections(10000)
            .build()
        val clientHttpConnector = ReactorClientHttpConnector(HttpClient.create(connectionProvider))
        return WebClient.builder().clientConnector(clientHttpConnector).build()
    }

}
