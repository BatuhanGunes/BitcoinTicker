package loodos.droid.bitcointicker.ui.home

import androidx.lifecycle.ViewModel
import loodos.droid.bitcointicker.data.repositories.UserRepository

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    /* TODO Bring current user information with FirebaseUser.
    val user: FirebaseUser by lazy {
        repository.currentUser()
    }
     */
}