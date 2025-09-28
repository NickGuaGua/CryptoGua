package com.guagua.cryptogua.ui.market

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MarketsScreenUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val items: ImmutableList<MarketItemUiState> = persistentListOf(),
    val selectedTab: MarketTab = MarketTab.Spot
)

data class MarketItemUiState(
    val id: String,
    val name: String,
    val symbol: String,
    val price: Double,
    val multiplier: Int,
    val scale: Int
)