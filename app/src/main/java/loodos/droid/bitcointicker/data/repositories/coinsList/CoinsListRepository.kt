package loodos.droid.bitcointicker.data.repositories.coinsList

import javax.inject.Inject
import loodos.droid.bitcointicker.api.succeeded
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import loodos.droid.bitcointicker.data.local.prefs.PreferenceStorage
import loodos.droid.bitcointicker.util.Constants
import loodos.droid.bitcointicker.api.Result
import java.util.*

class CoinsListRepository @Inject constructor(
    private val coinsListRemoteDataSource: CoinsListRemoteDataSource,
    private val coinsListDataSource: CoinsListDataSource,
    private val preferenceStorage: PreferenceStorage
) {
    val allCoinsLD = coinsListDataSource.allCoinsLD

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

    fun loadData(): Boolean {
        val lastLoadedDate = preferenceStorage.timeLoadedAt
        val currentDataMillis = Date().time
        return currentDataMillis - lastLoadedDate > 10 * 1000
    }
}