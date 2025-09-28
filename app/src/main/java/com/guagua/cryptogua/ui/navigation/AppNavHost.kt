package com.guagua.cryptogua.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.guagua.cryptogua.ui.main.MainRoute
import com.guagua.cryptogua.ui.main.mainScreen
import com.guagua.cryptogua.ui.market.marketScreen
import com.guagua.cryptogua.ui.settings.navigateToSettingsScreen
import com.guagua.cryptogua.ui.settings.settingsScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MainRoute,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        mainScreen(
            navigateToSettings = { navController.navigateToSettingsScreen() }
        )
        marketScreen()
        settingsScreen { navController.popBackStack() }
    }
}