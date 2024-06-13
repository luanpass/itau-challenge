package br.com.itau.challenge.factory

class FactoryResponse {
    companion object {

        fun generateClientJson() =
            """
               { "id": "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f", "nome": "Artur Carneiro", "telefone": "987651234", "tipoPessoa": "Fisica"}
            """.trimIndent()

        fun generateAccountJson() =
            """
               { "id": "2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f", "saldo": 10.00, "ativo": true, "limiteDiario": 1000.00 }
            """.trimIndent()
    }
}