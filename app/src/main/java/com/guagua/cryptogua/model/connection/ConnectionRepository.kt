package com.guagua.cryptogua.model.connection

import com.guagua.cryptogua.model.ws.WebSocketClient
import com.guagua.cryptogua.model.ws.WsStatus
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionRepository @Inject constructor(
    private val webSocketClient: WebSocketClient
) {

    val connectionStatusFlow: StateFlow<WsStatus> = webSocketClient.statusFlow

    fun connect() = webSocketClient.connect()

    fun sendMessage(message: String) = webSocketClient.sendMessage(message)
}