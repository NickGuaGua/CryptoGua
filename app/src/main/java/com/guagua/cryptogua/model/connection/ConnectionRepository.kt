package com.guagua.cryptogua.model.connection

import com.guagua.cryptogua.model.ws.WebSocketClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionRepository @Inject constructor(
    private val webSocketClient: WebSocketClient
) {

    val connectionStatusFlow = webSocketClient.statusFlow

    suspend fun connect() = webSocketClient.connect()
}