package com.guagua.cryptogua.ui.market

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object MarketRoute

fun NavGraphBuilder.marketScreen() {
    composable<MarketRoute> {
        MarketsScreen()
    }
}