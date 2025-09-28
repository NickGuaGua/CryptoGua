package com.guagua.cryptogua.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guagua.cryptogua.model.connection.ConnectionRepository
import com.guagua.cryptogua.model.ws.WsStatus
import com.guagua.cryptogua.model.ws.WsTopic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val connectionRepository: ConnectionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainScreenUiState())
    val state = _state.asStateFlow()

    private var isConnected = false

    init {
        collectConnectionStatus()
        connect()
    }

    private fun collectConnectionStatus() {
        viewModelScope.launch {
            connectionRepository.connectionStatusFlow.collect {
                if (it is WsStatus.Connected) {
                    connectionRepository.sendMessage(
                        """{"op": "subscribe", "args": [${
                            WsTopic.entries.joinToString(
                                ","
                            ) { "\"${it.key}\"" }
                        }]}"""
                    )
                }

                _state.update {
                    it.copy(isConnecting = it == WsStatus.Connecting)
                }
            }
        }
    }

    private fun connect() {
        viewModelScope.launch {
            connectionRepository.connect()
        }
    }
}