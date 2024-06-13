package br.com.itau.challenge.config.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    private fun apiInfo(): Info {
        return Info().apply {
            this.title = "Itau Challenge"
            this.description = "Api do Desafio Itau de Engenharia "
            this.contact = Contact().apply {
                this.url = "https://github.com/luanpass/itau-challenge"
                this.name = "GitHub"
            }
            this.license = License().apply {
                this.name = "ItauChallenge"
            }
        }
    }

    @Bean
    fun openApi(): OpenAPI = OpenAPI().apply { this.info = apiInfo() }
}