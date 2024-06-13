package br.com.itau.challenge.dataprovider.http.client.impl.response

import br.com.itau.challenge.usecase.entity.ClientEntity

data class ClientResponse(
    val id: String,
    val nome: String,
    val telefone: String,
    val tipoPessoa: String
)

fun ClientResponse.toEntity() = ClientEntity(
    id = this.id,
    nome = this.nome,
    telefone = this.telefone,
    tipoPessoa = this.tipoPessoa
)
