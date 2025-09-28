package com.guagua.cryptogua.ui.market

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.guagua.cryptogua.ui.theme.CryptoGuaTheme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MarketsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleMarketItems = persistentListOf(
        MarketItemUiState(
            id = "BTCUSDT",
            name = "BTCUSDT",
            symbol = "BTCUSDT",
            price = 45000.0,
            multiplier = 1,
            scale = 2
        ),
        MarketItemUiState(
            id = "ETHUSDT",
            name = "ETHUSDT",
            symbol = "ETHUSDT",
            price = 3000.0,
            multiplier = 1,
            scale = 2
        )
    )

    @Test
    fun marketsScreen_showsLoadingState() {
        composeTestRule.setContent {
            CryptoGuaTheme {
                MarketsScreen(
                    state = MarketsScreenUiState(isLoading = true),
                    onTabClick = {},
                    onRetry = {}
                )
            }
        }

        // Verify loading indicator is displayed
        composeTestRule
            .onNodeWithTag("Loading")
            .assertIsDisplayed()
    }

    @Test
    fun marketsScreen_showsErrorState() {
        var retryClicked = false

        composeTestRule.setContent {
            CryptoGuaTheme {
                MarketsScreen(
                    state = MarketsScreenUiState(isError = true),
                    onTabClick = {},
                    onRetry = { retryClicked = true }
                )
            }
        }

        // Verify error message is displayed
        composeTestRule
            .onNodeWithText("Something went wrong")
            .assertIsDisplayed()

        // Verify retry button is displayed and clickable
        composeTestRule
            .onNodeWithText("Retry")
            .assertIsDisplayed()
            .performClick()

        assertEquals(true, retryClicked)
    }

    @Test
    fun marketsScreen_showsMarketList() {
        composeTestRule.setContent {
            CryptoGuaTheme {
                MarketsScreen(
                    state = MarketsScreenUiState(
                        items = sampleMarketItems,
                        selectedTab = MarketTab.Spot
                    ),
                    onTabClick = {},
                    onRetry = {}
                )
            }
        }

        // Verify tab row is displayed
        composeTestRule
            .onNodeWithText("Spot")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Futures")
            .assertIsDisplayed()

        // Verify market items are displayed
        composeTestRule
            .onNodeWithText("BTCUSDT")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("ETHUSDT")
            .assertIsDisplayed()

        // Verify prices are displayed (formatted)
        composeTestRule
            .onNodeWithText("45000")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("3000")
            .assertIsDisplayed()
    }

    @Test
    fun marketsScreen_tabSwitching() {
        var selectedTab = MarketTab.Spot

        composeTestRule.setContent {
            CryptoGuaTheme {
                MarketsScreen(
                    state = MarketsScreenUiState(
                        items = sampleMarketItems,
                        selectedTab = selectedTab
                    ),
                    onTabClick = { tab -> selectedTab = tab },
                    onRetry = {}
                )
            }
        }

        // Initially Spot tab should be selected
        assertEquals(MarketTab.Spot, selectedTab)

        // Click on Futures tab
        composeTestRule
            .onNodeWithText("Futures")
            .performClick()

        assertEquals(MarketTab.Futures, selectedTab)
    }

    @Test
    fun marketsScreen_emptyState() {
        composeTestRule.setContent {
            CryptoGuaTheme {
                MarketsScreen(
                    state = MarketsScreenUiState(
                        items = persistentListOf(),
                        selectedTab = MarketTab.Spot
                    ),
                    onTabClick = {},
                    onRetry = {}
                )
            }
        }

        // Verify tabs are still displayed
        composeTestRule
            .onNodeWithText("Spot")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Futures")
            .assertIsDisplayed()

        // Verify no market items are displayed
        composeTestRule
            .onNodeWithText("BTCUSDT")
            .assertDoesNotExist()
    }

    @Test
    fun marketsScreen_futuresTabSelected() {
        composeTestRule.setContent {
            CryptoGuaTheme {
                MarketsScreen(
                    state = MarketsScreenUiState(
                        items = sampleMarketItems,
                        selectedTab = MarketTab.Futures
                    ),
                    onTabClick = {},
                    onRetry = {}
                )
            }
        }

        // Verify Futures tab appears selected
        composeTestRule
            .onNodeWithText("Futures")
            .assertIsDisplayed()
    }

    @Test
    fun marketsScreen_priceFormatting() {
        val itemWithDecimalPrice = MarketItemUiState(
            id = "ETHUSDT",
            name = "ETHUSDT",
            symbol = "ETHUSDT",
            price = 3456.789,
            multiplier = 1,
            scale = 2
        )

        composeTestRule.setContent {
            CryptoGuaTheme {
                MarketsScreen(
                    state = MarketsScreenUiState(
                        items = persistentListOf(itemWithDecimalPrice),
                        selectedTab = MarketTab.Spot
                    ),
                    onTabClick = {},
                    onRetry = {}
                )
            }
        }

        // Verify price is formatted according to scale (should show 2 decimal places)
        composeTestRule
            .onNodeWithText("3456.78")
            .assertIsDisplayed()
    }
}