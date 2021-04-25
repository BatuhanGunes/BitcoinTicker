package loodos.droid.bitcointicker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import loodos.droid.bitcointicker.data.firebase.FirebaseSource
import loodos.droid.bitcointicker.data.repositories.UserRepository
import loodos.droid.bitcointicker.ui.auth.AuthViewModelFactory
import loodos.droid.bitcointicker.ui.home.HomeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import timber.log.Timber

@HiltAndroidApp
class TickerApplication : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@TickerApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton { UserRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
