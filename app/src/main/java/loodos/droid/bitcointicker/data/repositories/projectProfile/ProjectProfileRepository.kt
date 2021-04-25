package loodos.droid.bitcointicker.data.repositories.projectProfile

import loodos.droid.bitcointicker.api.Result
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import loodos.droid.bitcointicker.data.repositories.coinsList.CoinsListDataSource
import loodos.droid.bitcointicker.util.Constants
import javax.inject.Inject

class ProjectProfileRepository @Inject constructor(
    private val localDataSource: ProjectProfileDataSource,
    private val remoteDataSource: ProjectProfileRemoteDataSource,
    private val coinsListDataSource: CoinsListDataSource
) {

    fun projectBySymbol(symbol: String) = localDataSource.projectBySymbol(symbol)

    suspend fun historicalPrice(symbol: String, days: Int, targetCur: String = "usd") =
        remoteDataSource.historicalPrice(symbol, days, targetCur)

    suspend fun updateFavouriteStatus(symbol: String): Result<CoinsListEntity> {
        val result = coinsListDataSource.updateFavouriteStatus(symbol)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }
}