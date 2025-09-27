package com.guagua.cryptogua.model.error

sealed class CryptoGuaException : Throwable() {
    data class Api(val code: Int, override val message: String) : CryptoGuaException()
}