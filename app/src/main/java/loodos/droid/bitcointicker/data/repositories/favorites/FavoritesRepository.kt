package loodos.droid.bitcointicker.data.repositories.favorites

import androidx.lifecycle.LiveData
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import javax.inject.Inject
import loodos.droid.bitcointicker.util.Constants
import loodos.droid.bitcointicker.api.Result

class FavoritesRepository @Inject constructor(private val favoritesDataSource: FavoritesDataSource) {

    val favoriteCoins: LiveData<List<CoinsListEntity>> = favoritesDataSource.favoriteCoins

    suspend fun favoriteSymbols(): List<String> = favoritesDataSource.favouriteSymbols()

    /**
     * update favourites in local database
     */
    suspend fun updateFavouriteStatus(symbol: String): Result<CoinsListEntity> {
        val result = favoritesDataSource.updateFavouriteStatus(symbol)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }
}