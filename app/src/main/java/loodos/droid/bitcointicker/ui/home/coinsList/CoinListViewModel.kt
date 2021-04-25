package loodos.droid.bitcointicker.ui.home.coinsList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import loodos.droid.bitcointicker.core.common.BaseViewModel
import loodos.droid.bitcointicker.data.repositories.coinsList.CoinsListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoinListViewModel @ViewModelInject constructor(private val repository: CoinsListRepository) :
    BaseViewModel() {

    val coinsListData = repository.allCoinsLD


    fun isListEmpty(): Boolean {
        return coinsListData.value?.isEmpty() ?: true
    }

    fun loadCoinsFromApi(targetCur: String = "usd") {
        if (repository.loadData()) {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.postValue(true)
                repository.coinsList(targetCur)
                _isLoading.postValue(false)
            }
        }
    }

}