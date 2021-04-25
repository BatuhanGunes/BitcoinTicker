package loodos.droid.bitcointicker.data.repositories.projectProfile

import loodos.droid.bitcointicker.api.Result
import loodos.droid.bitcointicker.api.BaseRemoteDataSource
import loodos.droid.bitcointicker.api.ApiInterface
import loodos.droid.bitcointicker.api.models.HistoricalPriceResponse
import javax.inject.Inject

class ProjectProfileRemoteDataSource @Inject constructor(private val service: ApiInterface) :
    BaseRemoteDataSource() {

    /**
     * fetch historical price from backend
     */
    suspend fun historicalPrice(
        symbol: String,
        days: Int,
        targetCurrency: String
    ): Result<HistoricalPriceResponse> =
        getResult { service.historicalPrice(symbol, targetCurrency, days) }

}