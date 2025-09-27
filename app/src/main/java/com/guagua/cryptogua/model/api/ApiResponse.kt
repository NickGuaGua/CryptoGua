package com.guagua.cryptogua.model.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val code: Int,
    @SerialName("msg")
    val message: String,
    val time: Long,
    val data: T,
    val success: Boolean
)