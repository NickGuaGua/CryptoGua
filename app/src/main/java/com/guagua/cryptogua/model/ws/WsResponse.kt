package com.guagua.cryptogua.model.ws

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class WsResponse(
    val topic: String,
    val data: JsonElement
)