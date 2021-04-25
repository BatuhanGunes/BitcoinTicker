package loodos.droid.bitcointicker.ui.home.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import loodos.droid.bitcointicker.core.common.BaseViewModel
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import loodos.droid.bitcointicker.data.repositories.favorites.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import loodos.droid.bitcointicker.api.Result

class FavoritesViewModel @ViewModelInject constructor(private val repository: FavoritesRepository) :
    BaseViewModel() {

    val favoriteCoinsList: LiveData<List<CoinsListEntity>> = repository.favoriteCoins

    private val _favouriteStock = MutableLiveData<CoinsListEntity>()
    val favouriteStock: LiveData<CoinsListEntity> = _favouriteStock

    private val _favouritesEmpty = MutableLiveData<Boolean>()
    val favouritesEmpty: LiveData<Boolean> = _favouritesEmpty

    fun updateFavouriteStatus(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.updateFavouriteStatus(symbol)) {
                is Result.Success -> _favouriteStock.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }

    fun isFavouritesEmpty(empty: Boolean) {
        _favouritesEmpty.postValue(empty)
    }
}