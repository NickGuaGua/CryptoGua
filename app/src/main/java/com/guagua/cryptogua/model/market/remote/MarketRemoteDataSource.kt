package com.guagua.cryptogua.model.market.remote

import com.guagua.cryptogua.model.api.ApiRequester
import com.guagua.cryptogua.model.market.remote.data.TickerBean
import com.guagua.cryptogua.model.ws.WebSocketClient
import com.guagua.cryptogua.model.ws.WsTopic
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRemoteDataSource @Inject constructor(
    val api: BtseAPI,
    val webSocketClient: WebSocketClient,
    val requester: ApiRequester,
    val json: Json
) {
    val tickerFlow = webSocketClient.messageFlow
        .filter { it.topic == WsTopic.CoinIndex.key }
        .map {
            json.decodeFromJsonElement<Map<String, TickerBean>>(it.data)
                .filter { it.value.type == 1 && it.value.id != null }
                .values
                .associateBy { it.id!! }
        }

    suspend fun getMarkets() = requester.request { api.getMarkets() }
}