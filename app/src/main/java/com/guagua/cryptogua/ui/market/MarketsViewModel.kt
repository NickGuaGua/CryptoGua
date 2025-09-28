package com.guagua.cryptogua.ui.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guagua.cryptogua.domain.market.GetMarketsUseCase
import com.guagua.cryptogua.domain.market.GetTickerMapUseCase
import com.guagua.cryptogua.model.market.data.Market
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarketsViewModel @Inject constructor(
    private val getMarketsUseCase: GetMarketsUseCase,
    private val getTickerMapUseCase: GetTickerMapUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MarketsScreenUiState())
    val state = _state.asStateFlow()

    private val marketsFlow = MutableStateFlow<List<Market>?>(null)

    private val tabFlow = MutableStateFlow(_state.value.selectedTab)

    private val marketItemsFlow = combine(
        marketsFlow.filterNotNull(),
        getTickerMapUseCase(),
        tabFlow
    ) { markets, tickerMap, tab ->
        markets
            .filter { it.future == (tab == MarketTab.Futures) }
            .mapNotNull { market ->
                val ticker = tickerMap[market.symbol] ?: return@mapNotNull null

                MarketItemUiState(
                    id = market.symbol,
                    name = market.symbol,
                    symbol = market.symbol,
                    price = ticker.price,
                    multiplier = market.multiplier,
                    scale = market.scale
                )
            }
            .sortedBy { it.name }
    }

    init {
        viewModelScope.launch {
            marketItemsFlow
                .collect { items ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            items = items.toImmutableList(),
                            selectedTab = tabFlow.value
                        )
                    }
                }
        }
        loadMarkets()
    }

    fun retry() = loadMarkets()

    fun onTabClick(tab: MarketTab) {
        tabFlow.value = tab
    }

    private fun loadMarkets() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            val markets = getMarketsUseCase()
                .onFailure {
                    Timber.e("Load markets error")
                    _state.update { it.copy(isLoading = false, isError = true) }
                }
                .getOrNull() ?: return@launch
            marketsFlow.emit(markets)
        }
    }
}