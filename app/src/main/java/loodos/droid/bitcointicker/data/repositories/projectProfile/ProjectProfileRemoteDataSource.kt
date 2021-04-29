package loodos.droid.bitcointicker.data.repositories.projectProfile

import androidx.lifecycle.LiveData
import loodos.droid.bitcointicker.api.Result
import loodos.droid.bitcointicker.api.BaseRemoteDataSource
import loodos.droid.bitcointicker.api.ApiInterface
import loodos.droid.bitcointicker.api.models.HistoricalPriceResponse
import loodos.droid.bitcointicker.data.local.database.CoinsDatabase
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import javax.inject.Inject

class ProjectProfileRemoteDataSource @Inject constructor(
    private val service: ApiInterface,
    private val db: CoinsDatabase
) : BaseRemoteDataSource() {

    val favoriteCoins: LiveData<List<CoinsListEntity>> = db.coinsListDao().favouriteCoins()

    suspend fun favouriteSymbols(): List<String> = db.coinsListDao().favouriteSymbols()

    suspend fun updateFavouriteStatus(symbol: String): CoinsListEntity? {
        val project = db.coinsListDao().projectFromSymbol(symbol)
        project?.let {
            val coinsListEntity = CoinsListEntity(
                it.symbol,
                it.id,
                it.name,
                it.price,
                it.changePercent,
                it.image,
                it.isFavourite.not()
            )

            if (db.coinsListDao().updateCoinsListEntity(coinsListEntity) > 0) {
                return coinsListEntity
            }
        }
        return null
    }

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