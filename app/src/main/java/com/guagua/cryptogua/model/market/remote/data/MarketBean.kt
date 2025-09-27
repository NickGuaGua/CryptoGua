package com.guagua.cryptogua.model.market.remote.data

import kotlinx.serialization.Serializable

@Serializable
data class MarketBean(
    val marketName: String?,
    val active: Boolean?,
    val marketClosed: Boolean?,
    val matchingDisabled: Boolean?,
    val future: Boolean?,
    val timeBasedContract: Boolean?,
    val openTime: Long?,
    val closeTime: Long?,
    val startMatching: Long?,
    val inactiveTime: Long?,
    val sortId: Int?,
    val lastUpdate: Long?,
    val symbol: String?,
    val quoteCurrency: String?,
    val baseCurrency: String?,
    val coin: String?,
    val multiplier: Int?,
    val scale: Int?,
    val priceDecimalPoints: Int?,
    val fundingRate: Double?,
    val openInterest: Long?,
    val openInterestUSD: Double?,
    val display: Boolean?,
    val displayQuote: String?,
    val globalDisplayQuote: String?,
    val displayOrder: Int?,
    val isFavorite: Boolean?,
    val availableQuotes: List<QuoteBean>?,
    val initialMarginPercentage: Double?,
    val maintenanceMarginPercentage: Double?,
    val prediction: Boolean?,
    val fundingIntervalMinutes: Int?,
    val fundingTime: Long?,
    val userCustomize: Boolean?,
    val favorite: Boolean?
)

@Serializable
data class QuoteBean(
    val id: Int?,
    val sortId: Int?,
    val name: String?,
    val shortName: String?,
    val symbol: String?,
    val type: Int?,
    val status: Int?,
    val gmtCreate: Long?,
    val gmtModified: Long?,
    val decimals: Int?,
    val isDefault: Int?,
    val minSize: Double?,
    val maxSize: Double?,
    val increment: Double?,
    val isSettlement: Int?,
    val depositMin: Double?,
    val isStable: Boolean?,
    val isQuote: Boolean?,
    val isSupportAddressExtension: Boolean?,
    val multiplier: Int?,
    val scale: Int?,
    val tier: String?,
    val coinFuncSwitch: CoinFuncSwitchBean?,
    val fiat: Boolean?,
    val typeEnum: String?,
    val crypto: Boolean?,
    val logo: String?
)

@Serializable
data class CoinFuncSwitchBean(
    val listedAsSpotQuote: Boolean?,
    val walletDeposit: Boolean?,
    val walletWithdraw: Boolean?,
    val walletTransferToUser: Boolean?,
    val walletConvert: Boolean?,
    val walletConvertFrom: Boolean?,
    val walletTransferToFutures: Boolean?,
    val walletOtc: Boolean?,
    val walletExpressBuy: Boolean?,
    val walletExpressSell: Boolean?
)
