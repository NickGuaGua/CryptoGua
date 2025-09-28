package com.guagua.cryptogua.model.api

import android.util.Log
import com.guagua.cryptogua.model.error.CryptoGuaException
import javax.inject.Inject

class ApiRequester @Inject constructor() {

    suspend inline fun <reified T> request(crossinline block: suspend () -> ApiResponse<T>): T {
        return block().let {
            if (it.success) {
                it.data
            } else {
                Log.e("Nick", "api error: ${it.code} / ${it.message}")
                throw CryptoGuaException.Api(it.code, it.message)
            }
        }
    }
}