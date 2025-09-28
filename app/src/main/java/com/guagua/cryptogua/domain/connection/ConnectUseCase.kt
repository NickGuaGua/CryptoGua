package com.guagua.cryptogua.domain.connection

import com.guagua.cryptogua.model.connection.ConnectionRepository
import com.guagua.cryptogua.model.ws.WsStatus
import com.guagua.cryptogua.model.ws.WsTopic
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ConnectUseCase @Inject constructor(
    private val repository: ConnectionRepository
) {

    operator fun invoke() = repository.connect()
        .distinctUntilChanged()
        .onEach {
            if (it is WsStatus.Connected) {
                repository.sendMessage(
                    """{"op": "subscribe", "args": [${
                        WsTopic.entries.joinToString(
                            ","
                        ) { "\"${it.key}\"" }
                    }]}"""
                )
            }
        }
}