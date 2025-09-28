package com.guagua.cryptogua.ui.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute

fun NavGraphBuilder.settingsScreen(
    back: () -> Unit
) {
    composable<SettingsRoute> {
        SettingsScreen(back = back)
    }
}

fun NavHostController.navigateToSettingsScreen() = navigate(SettingsRoute)