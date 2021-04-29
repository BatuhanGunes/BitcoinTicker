package loodos.droid.bitcointicker.data.repositories.projectProfile

import androidx.lifecycle.LiveData
import loodos.droid.bitcointicker.api.Result
import loodos.droid.bitcointicker.api.succeeded
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import loodos.droid.bitcointicker.data.local.prefs.PreferenceStorage
import loodos.droid.bitcointicker.data.repositories.coinsList.CoinsListDataSource
import loodos.droid.bitcointicker.data.repositories.coinsList.CoinsListRemoteDataSource
import loodos.droid.bitcointicker.util.Constants
import java.util.*
import javax.inject.Inject

class ProjectProfileRepository @Inject constructor(
    private val projectProfileDataSource: ProjectProfileRemoteDataSource,
    private val localDataSource: ProjectProfileDataSource,
    private val remoteDataSource: ProjectProfileRemoteDataSource,
    private val coinsListDataSource: CoinsListDataSource,
    private val preferenceStorage: PreferenceStorage,
    private val coinsListRemoteDataSource: CoinsListRemoteDataSource
) {

    val favoriteCoins: LiveData<List<CoinsListEntity>> = projectProfileDataSource.favoriteCoins

    suspend fun favoriteSymbols(): List<String> = projectProfileDataSource.favouriteSymbols()

    fun projectBySymbol(symbol: String) = localDataSource.projectBySymbol(symbol)

    suspend fun historicalPrice(symbol: String, days: Int, targetCur: String = "usd") =
        remoteDataSource.historicalPrice(symbol, days, targetCur)

    suspend fun updateFavouriteStatus(symbol: String): Result<CoinsListEntity> {
        val result = coinsListDataSource.updateFavouriteStatus(symbol)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }

    fun loadData(): Boolean {
        val lastLoadedDate = preferenceStorage.timeLoadedAt
        val currentDataMillis = Date().time
        return currentDataMillis - lastLoadedDate > 10 * 1000
    }

    suspend fun coinsList(targetCur: String) {
        when (val result = coinsListRemoteDataSource.coinsList(targetCur)) {
            is Result.Success -> {
                if (result.succeeded) {
                    val favSymbols = coinsListDataSource.favouriteSymbols()

                    val customStockList = result.data.let {
                        it.filter { item -> item.symbol.isNullOrEmpty().not() }
                                .map { item ->
                                    CoinsListEntity(
                                            item.symbol ?: "",
                                            item.id,
                                            item.name,
                                            item.price,
                                            item.changePercent,
                                            item.image,
                                            favSymbols.contains(item.symbol)
                                    )
                                }
                    }

                    coinsListDataSource.insertCoinsIntoDB(customStockList)

                    preferenceStorage.timeLoadedAt = Date().time

                    Result.Success(true)
                } else {
                    Result.Error(Constants.GENERIC_ERROR)
                }
            }
            else -> result as Result.Error
        }
    }
}