package com.guagua.cryptogua.model.market.remote

import com.guagua.cryptogua.model.api.ApiResponse
import com.guagua.cryptogua.model.market.remote.data.MarketBean
import retrofit2.http.GET

interface BtseAPI {

    @GET("futures/api/inquire/initial/market")
    suspend fun getMarkets(): ApiResponse<List<MarketBean>>
}