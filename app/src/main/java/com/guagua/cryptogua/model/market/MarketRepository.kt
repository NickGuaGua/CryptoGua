package com.guagua.cryptogua.model.market

import com.guagua.cryptogua.model.market.data.Market
import com.guagua.cryptogua.model.market.data.Ticker
import com.guagua.cryptogua.model.market.remote.MarketRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRepository @Inject constructor(
    private val remoteDataSource: MarketRemoteDataSource
) {
    val tickerFlow: Flow<Map<String, Ticker>> = remoteDataSource.tickerFlow.mapNotNull { map ->
        map.mapValues { Ticker.from(it.value) }
    }

    suspend fun getMarkets() = remoteDataSource.getMarkets().mapNotNull { Market.from(it) }
}