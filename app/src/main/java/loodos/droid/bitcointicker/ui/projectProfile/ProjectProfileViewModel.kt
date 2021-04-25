package loodos.droid.bitcointicker.ui.projectProfile

import android.widget.SeekBar
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import loodos.droid.bitcointicker.api.Result
import loodos.droid.bitcointicker.core.common.BaseViewModel
import loodos.droid.bitcointicker.data.local.database.CoinsListEntity
import loodos.droid.bitcointicker.data.repositories.projectProfile.ProjectProfileRepository


class ProjectProfileViewModel @ViewModelInject constructor(private val repository: ProjectProfileRepository) :
    BaseViewModel() {

    fun projectBySymbol(symbol: String) = repository.projectBySymbol(symbol)

    private val _dataError = MutableLiveData<Boolean>()
    val dataError: LiveData<Boolean> = _dataError

    private val _historicalData = MutableLiveData<List<DoubleArray>>()
    val historicalData: LiveData<List<DoubleArray>> = _historicalData

    //LiveData to show add/remove status as toast message
    private val _favouriteStock = MutableLiveData<CoinsListEntity?>()
    val favouriteStock: LiveData<CoinsListEntity?> = _favouriteStock

    fun historicalData(symbol: String?, days: Int) {
        symbol?.let {
            days.let {
                viewModelScope.launch(Dispatchers.IO) {
                    _isLoading.postValue(true)
                    val result = repository.historicalPrice(symbol, it)
                    _isLoading.postValue(false)

                    when (result) {
                        is Result.Success -> {
                            _historicalData.postValue(result.data.prices)
                            _dataError.postValue(false)
                        }
                        is Result.Error -> _dataError.postValue(true)
                    }
                }
            }
        }
    }

    fun updateFavouriteStatus(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.updateFavouriteStatus(symbol)) {
                is Result.Success -> _favouriteStock.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }
}