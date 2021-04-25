package loodos.droid.bitcointicker.data.repositories.projectProfile

import loodos.droid.bitcointicker.data.local.database.CoinsDatabase
import javax.inject.Inject

class ProjectProfileDataSource @Inject constructor(private val db: CoinsDatabase) {

    fun projectBySymbol(symbol: String) = db.coinsListDao().projectLiveDataFromSymbol(symbol)

}