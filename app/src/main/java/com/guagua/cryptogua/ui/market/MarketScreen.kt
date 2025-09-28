package com.guagua.cryptogua.ui.market

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun MarketsScreen(
    modifier: Modifier = Modifier,
    viewModel: MarketsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MarketsScreen(
        modifier = modifier.fillMaxSize(),
        state = state,
        onRetry = viewModel::retry,
        onTabClick = viewModel::onTabClick
    )
}

@Composable
fun MarketsScreen(
    modifier: Modifier = Modifier,
    state: MarketsScreenUiState,
    onRetry: () -> Unit = { },
    onTabClick: (MarketTab) -> Unit
) {
    Box(modifier = modifier) {
        when {
            state.isLoading -> LoadingContent(modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding())
            state.isError -> ErrorContent(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                onRetryClick = onRetry
            )

            else -> MarketListContent(
                modifier = Modifier.fillMaxSize(),
                items = state.items,
                selectedTab = state.selectedTab,
                onTabClick = onTabClick
            )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit = { }
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.TwoTone.Warning,
                contentDescription = Icons.TwoTone.Warning.name,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            TextButton(
                onClick = onRetryClick
            ) {
                Text(text = "Retry", color = LocalContentColor.current)
            }
        }
    }
}

@Composable
private fun MarketListContent(
    modifier: Modifier = Modifier,
    items: List<MarketItemUiState>,
    selectedTab: MarketTab,
    onTabClick: (MarketTab) -> Unit = { _ -> }
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                SecondaryTabRow(
                    modifier = Modifier.wrapContentHeight(),
                    selectedTabIndex = selectedTab.ordinal
                ) {
                    MarketTab.entries.forEachIndexed { i, tab ->
                        Tab(
                            selected = tab == selectedTab,
                            onClick = { onTabClick(tab) },
                            text = { Text(text = tab.title(), color = LocalContentColor.current) }
                        )
                    }
                }
            }
        }
    ) { contentPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 12.dp,
                bottom = 12.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items, key = { it.id }) { item ->
                MarketItem(
                    modifier = Modifier.fillMaxWidth(),
                    state = item
                )
            }
        }
    }
}

@Composable
fun MarketItem(
    modifier: Modifier = Modifier,
    state: MarketItemUiState
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier.weight(1f), text = state.name)
        Text(text = state.price.formatValue(scale = 2))
    }
}

fun Double.formatValue(multiplier: Int = 1, scale: Int): String {
    // 防呆處理，避免 multiplier <= 0
    val safeMultiplier = if (multiplier <= 0) 1 else multiplier
    if (scale == 0) return this.toString()

    // 先把 value * multiplier 轉成 BigDecimal (避免浮點誤差)
    val raw = BigDecimal.valueOf(this).multiply(BigDecimal.valueOf(safeMultiplier.toLong()))

    // 依 scale 設定小數位
    return raw.divide(BigDecimal.valueOf(safeMultiplier.toLong()), scale, RoundingMode.DOWN)
        .setScale(scale, RoundingMode.DOWN)
        .stripTrailingZeros() // 去掉多餘的 0
        .toPlainString()
}