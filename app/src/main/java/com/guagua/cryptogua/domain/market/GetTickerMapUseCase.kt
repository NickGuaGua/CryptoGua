package com.guagua.cryptogua.domain.market

import com.guagua.cryptogua.model.market.MarketRepository
import javax.inject.Inject

class GetTickerMapUseCase @Inject constructor(
    private val repository: MarketRepository
) {

    operator fun invoke() = repository.tickerFlow
}