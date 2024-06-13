package br.com.itau.challenge.dataprovider.http.account

import br.com.itau.challenge.usecase.entity.AccountEntity

interface AccountHttpProvider {
    suspend fun getAccountById(id: String): AccountEntity
    suspend fun updateBalance()
}