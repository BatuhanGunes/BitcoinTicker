package loodos.droid.bitcointicker.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import loodos.droid.bitcointicker.R
import loodos.droid.bitcointicker.core.common.BaseActivity
import loodos.droid.bitcointicker.core.common.NavigationHost


@AndroidEntryPoint
class HomeActivity : BaseActivity(), NavigationHost {

    //private val viewModel: HomeViewModel by viewModels() // TODO

    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_coins_list,
            R.id.navigation_favourites
        )
    }

    private lateinit var navController: NavController
    private var navHostFragment: NavHostFragment? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.homeNavHostFragment) as? NavHostFragment
                ?: return

        navController = findNavController(R.id.homeNavHostFragment)
        appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)

        homeBottomNavView.setupWithNavController(navController)
    }

    /**
     * Callback method to update the toolbar's title based on the selected bottom tab
     */
    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}