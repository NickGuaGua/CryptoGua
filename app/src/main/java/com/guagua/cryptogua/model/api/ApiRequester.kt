package com.guagua.cryptogua.model.api

import com.guagua.cryptogua.model.error.CryptoGuaException
import javax.inject.Inject

class ApiRequester @Inject constructor() {

    suspend inline fun <reified T> request(crossinline block: suspend () -> ApiResponse<T>): T {
        return block().let {
            if (it.success) {
                it.data
            } else {
                throw CryptoGuaException.Api(it.code, it.message)
            }
        }
    }
}