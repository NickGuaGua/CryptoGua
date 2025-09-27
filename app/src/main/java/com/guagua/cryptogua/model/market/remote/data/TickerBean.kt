package com.guagua.cryptogua.model.market.remote.data

import kotlinx.serialization.Serializable

@Serializable
data class TickerBean(
    val id: String?,
    val name: String?,
    val type: Int?,
    val price: Double?,
    val gains: Double?,
    val open: Double?,
    val high: Double?,
    val low: Double?,
    val volume: Double?,
    val amount: Double?
)