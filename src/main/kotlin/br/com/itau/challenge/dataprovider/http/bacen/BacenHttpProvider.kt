package br.com.itau.challenge.dataprovider.http.bacen

interface BacenHttpProvider {

    suspend fun notifyPostTransfer()
}