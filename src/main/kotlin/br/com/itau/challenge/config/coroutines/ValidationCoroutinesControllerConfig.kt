package br.com.itau.challenge.config.coroutines

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.validation.MessageInterpolatorFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Role
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class ValidationCoroutinesControllerConfig {

    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    fun defaultValidator(): LocalValidatorFactoryBean {
        val factoryBean = KotlinCoroutinesLocalValidatorFactoryBean()
        factoryBean.messageInterpolator = MessageInterpolatorFactory().getObject()
        return factoryBean
    }
}