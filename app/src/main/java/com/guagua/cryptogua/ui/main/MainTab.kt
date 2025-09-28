package com.guagua.cryptogua.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Autorenew
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.MonetizationOn
import androidx.compose.material.icons.twotone.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.guagua.cryptogua.R

enum class MainTab {
    MARKET,
    Convert,
    Earn,
    Wallet
}

@Composable
fun MainTab.title(): String = when (this) {
    MainTab.MARKET -> R.string.tab_market
    MainTab.Convert -> R.string.tab_convert
    MainTab.Earn -> R.string.tab_earn
    MainTab.Wallet -> R.string.tab_wallet
}.let { stringResource(it) }

@Composable
fun MainTab.icon() = when (this) {
    MainTab.MARKET -> Icons.TwoTone.Home
    MainTab.Convert -> Icons.TwoTone.Autorenew
    MainTab.Earn -> Icons.TwoTone.MonetizationOn
    MainTab.Wallet -> Icons.TwoTone.Wallet
}