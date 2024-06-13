package br.com.itau.challenge.extensions

import org.springframework.core.ParameterizedTypeReference

inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}