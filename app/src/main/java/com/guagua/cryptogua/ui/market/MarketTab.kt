package com.guagua.cryptogua.ui.market

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.guagua.cryptogua.R

enum class MarketTab {
    Spot,
    Futures
}

@Composable
fun MarketTab.title(): String {
    return when (this) {
        MarketTab.Spot -> stringResource(R.string.market_tab_spot)
        MarketTab.Futures -> stringResource(R.string.market_tab_futures)
    }
}