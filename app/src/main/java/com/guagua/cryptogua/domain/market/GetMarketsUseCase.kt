package com.guagua.cryptogua.domain.market

import com.guagua.cryptogua.model.market.MarketRepository
import javax.inject.Inject

class GetMarketsUseCase @Inject constructor(
    private val repository: MarketRepository
) {

    suspend operator fun invoke() = runCatching { repository.getMarkets() }
}