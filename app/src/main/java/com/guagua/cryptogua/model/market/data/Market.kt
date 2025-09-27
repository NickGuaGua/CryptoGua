package com.guagua.cryptogua.model.market.data

import com.guagua.cryptogua.model.market.remote.data.MarketBean
import com.guagua.cryptogua.model.market.remote.data.QuoteBean

data class Market(
    val marketName: String,
    val active: Boolean,
    val marketClosed: Boolean,
    val matchingDisabled: Boolean,
    val future: Boolean,
    val timeBasedContract: Boolean,
    val openTime: Long,
    val closeTime: Long,
    val startMatching: Long,
    val inactiveTime: Long,
    val sortId: Int,
    val lastUpdate: Long,
    val symbol: String,
    val quoteCurrency: String,
    val baseCurrency: String,
    val coin: String,
    val multiplier: Int,
    val scale: Int,
    val priceDecimalPoints: Int,
    val fundingRate: Double,
    val openInterest: Long,
    val openInterestUSD: Double,
    val display: Boolean,
    val displayQuote: String,
    val globalDisplayQuote: String,
    val displayOrder: Int,
    val isFavorite: Boolean,
    val availableQuotes: List<QuoteBean>,
    val initialMarginPercentage: Double,
    val maintenanceMarginPercentage: Double,
    val prediction: Boolean,
    val fundingIntervalMinutes: Int,
    val fundingTime: Long,
    val userCustomize: Boolean,
    val favorite: Boolean
) {
    companion object {
        fun from(bean: MarketBean): Market? = with(bean) {
            Market(
                marketName = marketName ?: return@with null,
                active = active ?: false,
                marketClosed = marketClosed ?: false,
                matchingDisabled = matchingDisabled ?: false,
                future = future ?: false,
                timeBasedContract = timeBasedContract ?: false,
                openTime = openTime ?: 0L,
                closeTime = closeTime ?: 0L,
                startMatching = startMatching ?: 0L,
                inactiveTime = inactiveTime ?: 0L,
                sortId = sortId ?: 0,
                lastUpdate = lastUpdate ?: 0L,
                symbol = symbol.orEmpty(),
                quoteCurrency = quoteCurrency.orEmpty(),
                baseCurrency = baseCurrency.orEmpty(),
                coin = coin.orEmpty(),
                multiplier = multiplier ?: 0,
                scale = scale ?: 0,
                priceDecimalPoints = priceDecimalPoints ?: 0,
                fundingRate = fundingRate ?: 0.0,
                openInterest = openInterest ?: 0L,
                openInterestUSD = openInterestUSD ?: 0.0,
                display = display ?: false,
                displayQuote = displayQuote.orEmpty(),
                globalDisplayQuote = globalDisplayQuote.orEmpty(),
                displayOrder = displayOrder ?: 0,
                isFavorite = isFavorite ?: false,
                availableQuotes = availableQuotes ?: emptyList(),
                initialMarginPercentage = initialMarginPercentage ?: 0.0,
                maintenanceMarginPercentage = maintenanceMarginPercentage ?: 0.0,
                prediction = prediction ?: false,
                fundingIntervalMinutes = fundingIntervalMinutes ?: 0,
                fundingTime = fundingTime ?: 0L,
                userCustomize = userCustomize ?: false,
                favorite = favorite ?: false
            )
        }
    }
}