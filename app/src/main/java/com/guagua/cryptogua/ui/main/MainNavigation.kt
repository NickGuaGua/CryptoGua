package com.guagua.cryptogua.ui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

fun NavGraphBuilder.mainScreen(
    navigateToSettings: () -> Unit
) {
    composable<MainRoute> {
        MainScreen(
            navigateToSettings = navigateToSettings
        )
    }
}