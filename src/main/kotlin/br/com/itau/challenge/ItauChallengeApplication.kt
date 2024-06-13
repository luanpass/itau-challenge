package br.com.itau.challenge

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["br"])
@ConfigurationPropertiesScan
class ItauChallengeApplication

fun main(args: Array<String>) {
    runApplication<ItauChallengeApplication>(*args)
}
