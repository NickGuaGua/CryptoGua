package com.guagua.cryptogua.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guagua.cryptogua.domain.connection.ConnectUseCase
import com.guagua.cryptogua.model.error.CryptoGuaException
import com.guagua.cryptogua.model.ws.WsStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectUseCase: ConnectUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenUiState())
    val state = _state.asStateFlow()

    init {
        connect()
    }

    private fun connect() {
        viewModelScope.launch {
            connectUseCase().retryWhen { throwable, attempt ->
                Log.w("Nick", "Retry, attempt: $attempt")
                delay(3000L)
                throwable is CryptoGuaException
            }.collect { status ->
                _state.update {
                    it.copy(isConnecting = status.isConnecting())
                }
            }
        }
    }

    fun WsStatus.isConnecting() =
        this == WsStatus.Connecting || this is WsStatus.Close || this is WsStatus.Failure
}