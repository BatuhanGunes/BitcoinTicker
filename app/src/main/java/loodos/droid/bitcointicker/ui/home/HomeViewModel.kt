package loodos.droid.bitcointicker.ui.home

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import loodos.droid.bitcointicker.data.repositories.UserRepository
import loodos.droid.bitcointicker.util.startLoginActivity

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    val user: FirebaseUser by lazy {
        repository.currentUser()
    }

    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }
}