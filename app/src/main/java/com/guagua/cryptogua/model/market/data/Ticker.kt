package com.guagua.cryptogua.model.market.data

import com.guagua.cryptogua.model.market.remote.data.TickerBean

data class Ticker(
    val id: String,
    val name: String,
    val type: Int,
    val price: Double,
    val gains: Double,
    val open: Double,
    val high: Double,
    val low: Double,
    val volume: Double,
    val amount: Double
) {
    companion object {
        fun from(bean: TickerBean) = with(bean) {
            require(id != null)
            require(type != null)
            Ticker(
                id = id,
                name = name.orEmpty(),
                type = type,
                price = price ?: 0.0,
                gains = gains ?: 0.0,
                open = open ?: 0.0,
                high = high ?: 0.0,
                low = low ?: 0.0,
                volume = volume ?: 0.0,
                amount = amount ?: 0.0
            )
        }
    }
}