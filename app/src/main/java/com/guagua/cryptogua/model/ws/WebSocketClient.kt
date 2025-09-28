package com.guagua.cryptogua.model.ws

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor(
    val okHttpClient: OkHttpClient,
    val request: Request,
    val json: Json
) : WebSocketListener() {

    private var webSocket: WebSocket? = null
    private val mutex = Mutex()

    val statusFlow = MutableStateFlow<WsStatus>(WsStatus.Idle)

    val messageFlow: MutableSharedFlow<WsResponse> = MutableSharedFlow(extraBufferCapacity = 256)

    suspend fun connect() = mutex.withLock {
        statusFlow.value = WsStatus.Connecting
        releaseWebSocket()
        webSocket = okHttpClient.newWebSocket(request, this)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        statusFlow.value = WsStatus.Connected
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.w("Nick", "onClosing")
        statusFlow.value = WsStatus.Close(code, reason)
        releaseWebSocket()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.w("Nick", "onClosed")
        statusFlow.value = WsStatus.Close(code, reason)
        releaseWebSocket()
    }

    override fun onFailure(
        webSocket: WebSocket,
        t: Throwable,
        response: Response?
    ) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        Log.w("Nick", "onFailure: ${response?.code} / ${response?.message}", t)
        statusFlow.value = WsStatus.Failure(t)
        releaseWebSocket()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        try {
            messageFlow.tryEmit(json.decodeFromString<WsResponse>(text))
        } catch (e: Exception) {
            // Ignore parse error
            Log.w("Nick", "ws message parse error: $text", e)
        }
    }

    fun sendMessage(message: String): Boolean {
        Log.i("Nick", "sendMessage: $message")
        return webSocket?.send(message) ?: false
    }

    fun close() {
        releaseWebSocket()
        statusFlow.value = WsStatus.Idle
    }

    private fun releaseWebSocket() {
        webSocket?.cancel()
        webSocket = null
    }
}

sealed class WsStatus {
    data object Idle : WsStatus()
    data object Connecting : WsStatus()
    data object Connected : WsStatus()
    data class Failure(val error: Throwable? = null) : WsStatus()
    data class Close(val code: Int, val reason: String) : WsStatus()
}