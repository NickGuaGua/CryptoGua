package com.guagua.cryptogua.ui.market

import com.guagua.cryptogua.domain.market.GetMarketsUseCase
import com.guagua.cryptogua.domain.market.GetTickerMapUseCase
import com.guagua.cryptogua.model.market.data.Market
import com.guagua.cryptogua.model.market.data.Ticker
import com.guagua.cryptogua.ui.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MarketsViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    private lateinit var getMarketsUseCase: GetMarketsUseCase
    private lateinit var getTickerMapUseCase: GetTickerMapUseCase
    private lateinit var viewModel: MarketsViewModel

    // Market(marketName=SAHARA-USD, active=true, marketClosed=false, matchingDisabled=false, future=false, timeBasedContract=false, openTime=0, closeTime=0, startMatching=0, inactiveTime=0, sortId=0, lastUpdate=1751276274203, symbol=SAHARA, quoteCurrency=USD, baseCurrency=SAHARA, coin=SAHARA, multiplier=1, scale=8, priceDecimalPoints=0, fundingRate=0.0, openInterest=0, openInterestUSD=0.0, display=false, displayQuote=, globalDisplayQuote=, displayOrder=0, isFavorite=false, availableQuotes=[Quote(id=4, sortId=1, name=US Dollar, shortName=USD, symbol=$, type=1, status=1, gmtCreate=1517909896000, gmtModified=1700019166331, decimals=4, isDefault=1, minSize=0.01, maxSize=1000000.0, increment=0.01, isSettlement=1, depositMin=0.0, isStable=false, isQuote=true, isSupportAddressExtension=false, multiplier=1, scale=2, tier=, fiat=true, typeEnum=Fiat, crypto=false, logo=20180827653657.png), Quote(id=1, sortId=52, name=Ethereum, shortName=ETH, symbol=ETH, type=2, status=1, gmtCreate=1516112883000, gmtModified=1520939365000, decimals=4, isDefault=0, minSize=1.0E-5, maxSize=5000.0, increment=1.0E-5, isSettlement=0, depositMin=0.0, isStable=false, isQuote=true, isSupportAddressExtension=false, multiplier=1, scale=8, tier=, fiat=false, typeEnum=Crypto, crypto=true, logo=20180827654463.png), Quote(id=79, sortId=53, name=Tether USD, shortName=USDT, symbol=₮, type=2, status=1, gmtCreate=1534768098032, gmtModified=0, decimals=4, isDefault=0, minSize=10.0, maxSize=1000000.0, increment=1.0E-6, isSettlement=0, depositMin=0.0, isStable=true, isQuote=true, isSupportAddressExtension=false, multiplier=1, scale=8, tier=, fiat=false, typeEnum=Crypto, crypto=true, logo=20180904005430.png), Quote(id=97, sortId=55, name=USD Coin, shortName=USDC, symbol=USDC, type=2, status=1, gmtCreate=1562753917523, gmtModified=0, decimals=6, isDefault=0, minSize=1.0E-5, maxSize=20000.0, increment=0.0, isSettlement=0, depositMin=0.0, isStable=true, isQuote=false, isSupportAddressExtension=false, multiplier=1, scale=8, tier=, fiat=false, typeEnum=Crypto, crypto=true, logo=)], initialMarginPercentage=0.0, maintenanceMarginPercentage=0.0, prediction=false, fundingIntervalMinutes=0, fundingTime=0, userCustomize=false, favorite=false), Tickers: (ANT, Ticker(id=ANT, name=ANT, type=1, price=6.4428128015, gains=0.0, open=0.0, high=0.0, low=0.0, volume=0.0, amount=0.0))
    private val mockSpotMarket = Market(
        marketName = "SAHARA-USD",
        active = true,
        marketClosed = false,
        matchingDisabled = false,
        future = false,
        timeBasedContract = false,
        openTime = 0L,
        closeTime = 0L,
        startMatching = 0L,
        inactiveTime = 0L,
        sortId = 0,
        lastUpdate = 1751276274203L,
        symbol = "BTCUSDT",
        quoteCurrency = "USDT",
        baseCurrency = "BTC",
        coin = "BTC",
        multiplier = 1,
        scale = 2,
        priceDecimalPoints = 0,
        fundingRate = 0.0,
        openInterest = 0L,
        openInterestUSD = 0.0,
        display = false,
        displayQuote = "",
        globalDisplayQuote = "",
        displayOrder = 0,
        isFavorite = false,
        availableQuotes = emptyList(),
        initialMarginPercentage = 0.0,
        maintenanceMarginPercentage = 0.0,
        prediction = false,
        fundingIntervalMinutes = 0,
        fundingTime = 0L,
        userCustomize = false,
        favorite = false
    )

    private val mockFuturesMarket = mockSpotMarket.copy(future = true)

    private val mockTickerMap = listOf(
        Ticker(
            id = "BTCUSDT",
            name = "BTCUSDT",
            type = 1,
            price = 45000.0,
            gains = 0.0,
            open = 0.0,
            high = 0.0,
            low = 0.0,
            volume = 0.0,
            amount = 0.0
        ),
    ).associateBy { it.id }

    @Before
    fun setup() {
        getMarketsUseCase = mockk()
        getTickerMapUseCase = mockk()
        every { getTickerMapUseCase() } returns flowOf(mockTickerMap)
    }

    @Test
    fun `should load spot markets successfully`() = runTest {
        coEvery { getMarketsUseCase() } returns Result.success(listOf(mockSpotMarket))
        every { getTickerMapUseCase() } returns flowOf(mockTickerMap)

        viewModel = MarketsViewModel(getMarketsUseCase, getTickerMapUseCase)
        runCurrent()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(1, state.items.size)
        assertEquals(MarketTab.Spot, state.selectedTab)

        val item = state.items[0]
        assertEquals(mockSpotMarket.symbol, item.name)
        assertEquals(mockSpotMarket.symbol, item.symbol)
        assertEquals(mockTickerMap[mockSpotMarket.symbol]?.price ?: 0.0, item.price, 0.001)
        assertEquals(mockSpotMarket.multiplier, item.multiplier)
        assertEquals(mockSpotMarket.scale, item.scale)
    }

    @Test
    fun `should filter futures markets when tab is Futures`() = runTest {
        coEvery { getMarketsUseCase() } returns Result.success(
            listOf(
                mockSpotMarket,
                mockFuturesMarket
            )
        )
        every { getTickerMapUseCase() } returns flowOf(mockTickerMap)

        viewModel = MarketsViewModel(getMarketsUseCase, getTickerMapUseCase)
        advanceUntilIdle()

        // Switch to Futures tab
        viewModel.onTabClick(MarketTab.Futures)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MarketTab.Futures, state.selectedTab)
        assertEquals(1, state.items.size)
        assertEquals(mockFuturesMarket.symbol, state.items[0].name)
    }

    @Test
    fun `should filter spot markets when tab is Spot`() = runTest {
        coEvery { getMarketsUseCase() } returns Result.success(
            listOf(
                mockSpotMarket,
                mockFuturesMarket
            )
        )
        every { getTickerMapUseCase() } returns flowOf(mockTickerMap)

        viewModel = MarketsViewModel(getMarketsUseCase, getTickerMapUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(MarketTab.Spot, state.selectedTab)
        assertEquals(1, state.items.size)
        assertEquals(mockSpotMarket.symbol, state.items[0].symbol)
    }

    @Test
    fun `should handle error state when use case fails`() = runTest {
        coEvery { getMarketsUseCase() } returns Result.failure(RuntimeException("Network error"))
        every { getTickerMapUseCase() } returns flowOf(mockTickerMap)
        viewModel = MarketsViewModel(getMarketsUseCase, getTickerMapUseCase)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.isError)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun `should sort markets by name`() = runTest {
        val market1 = mockSpotMarket.copy(marketName = "Zebra Coin", symbol = "ZEB")
        val market2 = mockSpotMarket.copy(marketName = "Alpha Coin", symbol = "ALP")
        coEvery { getMarketsUseCase() } returns Result.success(listOf(market1, market2))

        val tickerMap = listOf(
            Ticker(
                id = "ZEB",
                name = "ZEB",
                type = 1,
                price = 2.0,
                gains = 0.0,
                open = 0.0,
                high = 0.0,
                low = 0.0,
                volume = 0.0,
                amount = 0.0
            ),
            Ticker(
                id = "ALP",
                name = "ALP",
                type = 1,
                price = 1.0,
                gains = 0.0,
                open = 0.0,
                high = 0.0,
                low = 0.0,
                volume = 0.0,
                amount = 0.0
            ),
        ).associateBy { it.id }
        every { getTickerMapUseCase() } returns flowOf(tickerMap)

        viewModel = MarketsViewModel(getMarketsUseCase, getTickerMapUseCase)

        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(2, state.items.size)
        assertEquals(market2.symbol, state.items[0].name)
        assertEquals(market1.symbol, state.items[1].name)
    }

    @Test
    fun `retry should call use case again`() = runTest {
        coEvery { getMarketsUseCase() } returns Result.failure(RuntimeException("Network error"))

        viewModel = MarketsViewModel(getMarketsUseCase, getTickerMapUseCase)
        advanceUntilIdle()

        coEvery { getMarketsUseCase() } returns Result.success(listOf(mockSpotMarket))

        viewModel.retry()
        advanceUntilIdle()

        coVerify(exactly = 2) { getMarketsUseCase() }

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertFalse(state.isError)
        assertEquals(1, state.items.size)
    }

    @Test
    fun `should update ticker prices in real time`() = runTest {
        coEvery { getMarketsUseCase() } returns Result.success(listOf(mockSpotMarket))
        val updatedTickerMap = mockTickerMap.mapValues { (_, value) -> value.copy(price = 46000.0) }
        every { getTickerMapUseCase() } returns flowOf(mockTickerMap, updatedTickerMap)

        viewModel = MarketsViewModel(getMarketsUseCase, getTickerMapUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(46000.0, state.items[0].price, 1000.0)
    }
}