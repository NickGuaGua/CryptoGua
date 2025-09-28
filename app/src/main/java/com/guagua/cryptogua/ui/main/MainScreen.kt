package com.guagua.cryptogua.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guagua.cryptogua.ui.WipScreen
import com.guagua.cryptogua.ui.market.MarketsScreen
import com.guagua.cryptogua.ui.wallet.WalletScreen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MainScreen(
        modifier = Modifier.fillMaxSize(),
        state = state,
        navigateToSettings = navigateToSettings
    )
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainScreenUiState,
    navigateToSettings: () -> Unit
) {
    val pagerState = rememberPagerState { 4 }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomBar(
                currentTab = MainTab.entries[pagerState.currentPage],
                isConnecting = state.isConnecting,
                hasConnectionIssue = state.hasConnectionIssue,
                onTabClick = {
                    scope.launch {
                        pagerState.scrollToPage(it.ordinal)
                    }
                }
            )
        }
    ) { contentPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = contentPadding.calculateBottomPadding()
                )
        ) { pageIndex ->
            val tab = MainTab.entries[pageIndex]

            when (tab) {
                MainTab.MARKET -> MarketsScreen()
                MainTab.Convert,
                MainTab.Earn -> {
                    WipScreen(
                        modifier = Modifier.fillMaxSize(),
                        text = tab.title()
                    )
                }

                MainTab.Wallet -> {
                    WalletScreen(
                        modifier = Modifier.fillMaxSize(),
                        navigateToSettings = navigateToSettings
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    currentTab: MainTab,
    isConnecting: Boolean,
    hasConnectionIssue: Boolean,
    onTabClick: (MainTab) -> Unit = {}
) {
    Column(modifier = modifier) {
        AnimatedVisibility(visible = hasConnectionIssue) {
            ConnectionIssue(modifier = Modifier.fillMaxWidth())
        }
        if (isConnecting) {
            LinearProgressIndicator(
                modifier = Modifier
                    .height(3.dp)
                    .fillMaxWidth()
            )
        }
        BottomAppBar {
            BottomAppBarContent(
                selected = currentTab,
                onClick = onTabClick
            )
        }
    }
}

@Composable
private fun ConnectionIssue(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.error)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Trying to reconnect…", color = MaterialTheme.colorScheme.onError)
    }
}

@Composable
private fun BottomAppBarContent(
    modifier: Modifier = Modifier,
    selected: MainTab = MainTab.MARKET,
    onClick: (MainTab) -> Unit = { _ -> }
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MainTab.entries.forEach { tab ->
            val icon = tab.icon()
            NavigationBarItem(
                selected = tab == selected,
                icon = {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        imageVector = icon,
                        contentDescription = icon.name,
                        tint = LocalContentColor.current
                    )
                },
                label = {
                    Text(
                        text = tab.title(),
                        color = LocalContentColor.current
                    )
                },
                onClick = { onClick(tab) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    BottomBar(
        modifier = Modifier.fillMaxWidth(),
        currentTab = MainTab.MARKET,
        isConnecting = true,
        hasConnectionIssue = true
    )
}