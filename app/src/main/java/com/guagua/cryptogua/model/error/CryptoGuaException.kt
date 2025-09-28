package com.guagua.cryptogua.model.error

sealed class CryptoGuaException : Throwable() {
    data class Api(val code: Int, override val message: String) : CryptoGuaException()
    data class Connection(val reason: String, val code: Int?) : CryptoGuaException()
}