package br.com.itau.challenge.dataprovider.http.client

import br.com.itau.challenge.usecase.entity.ClientEntity

interface ClientHttpProvider {
    suspend fun getClientById(clientId: String): ClientEntity
}