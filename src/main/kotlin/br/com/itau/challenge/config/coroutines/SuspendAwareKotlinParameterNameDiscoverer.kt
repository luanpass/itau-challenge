package br.com.itau.challenge.config.coroutines

import java.lang.reflect.Constructor
import java.lang.reflect.Method
import kotlin.reflect.jvm.kotlinFunction
import org.springframework.core.KotlinReflectionParameterNameDiscoverer
import org.springframework.core.ParameterNameDiscoverer

class SuspendAwareKotlinParameterNameDiscoverer : ParameterNameDiscoverer {

    private val defaultProvider = KotlinReflectionParameterNameDiscoverer()

    override fun getParameterNames(constructor: Constructor<*>): Array<String>? =
        defaultProvider.getParameterNames(constructor)

    override fun getParameterNames(method: Method): Array<String>? {
        val defaultNames = defaultProvider.getParameterNames(method) ?: return null
        val function = method.kotlinFunction
        return if (function != null && function.isSuspend) {
            defaultNames + ""
        } else defaultNames
    }
}