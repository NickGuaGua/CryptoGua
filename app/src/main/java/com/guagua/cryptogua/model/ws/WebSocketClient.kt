package com.guagua.cryptogua.model.ws

import android.util.Log
import com.guagua.cryptogua.model.error.CryptoGuaException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
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

    private val _statusFlow = MutableStateFlow<WsStatus>(WsStatus.Idle)
    val statusFlow = _statusFlow.asStateFlow()

    private val _messageFlow: MutableSharedFlow<WsResponse> =
        MutableSharedFlow(extraBufferCapacity = 256)
    val messageFlow = _messageFlow.asSharedFlow()

    private val connectionFlow = callbackFlow {
        releaseWebSocket()
        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                _statusFlow.value = WsStatus.Connected
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.w("Nick", "onClosing")
                _statusFlow.value = WsStatus.Close(code, reason)
                releaseWebSocket()
                close(
                    cause = CryptoGuaException.Connection(
                        reason = reason,
                        code = code
                    )
                )
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.w("Nick", "onClosed")
                _statusFlow.value = WsStatus.Close(code, reason)
                releaseWebSocket()
                close(
                    cause = CryptoGuaException.Connection(
                        reason = reason,
                        code = code
                    )
                )
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                super.onFailure(webSocket, t, response)
                t.printStackTrace()
                Log.w("Nick", "onFailure: ${response?.code} / ${response?.message}", t)
                _statusFlow.value = WsStatus.Failure(t)
                releaseWebSocket()

                close(
                    cause = CryptoGuaException.Connection(
                        reason = response?.message ?: t.message ?: "Unknown error",
                        code = response?.code
                    )
                )
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                try {
                    _messageFlow.tryEmit(json.decodeFromString<WsResponse>(text))
                } catch (e: Exception) {
                    // Ignore parse error
                    Log.w("Nick", "ws message parse error: $text", e)
                }
            }
        }
        _statusFlow
            .onStart { webSocket = okHttpClient.newWebSocket(request, listener) }
            .collect { send(it) }
    }

    fun connect(): Flow<WsStatus> = connectionFlow

    fun sendMessage(message: String): Boolean {
        Log.i("Nick", "sendMessage: $message")
        return webSocket?.send(message) ?: false
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