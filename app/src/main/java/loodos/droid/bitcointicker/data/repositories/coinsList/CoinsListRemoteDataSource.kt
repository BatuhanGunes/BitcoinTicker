package loodos.droid.bitcointicker.data.repositories.coinsList

import loodos.droid.bitcointicker.api.ApiInterface
import loodos.droid.bitcointicker.api.BaseRemoteDataSource
import javax.inject.Inject
import loodos.droid.bitcointicker.api.Result
import loodos.droid.bitcointicker.api.models.Coin

class CoinsListRemoteDataSource @Inject constructor(private val service: ApiInterface) :
    BaseRemoteDataSource() {

    suspend fun coinsList(targetCur: String): Result<List<Coin>> =
        getResult {
            service.coinsList(targetCur)
        }
}