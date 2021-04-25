package loodos.droid.bitcointicker.ui.home

import android.view.View
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import loodos.droid.bitcointicker.data.repositories.UserRepository
import loodos.droid.bitcointicker.util.UIHelper.startLoginActivity

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    /* TODO Bring current user information with FirebaseUser.
    val user: FirebaseUser by lazy {
        repository.currentUser()
    }
     */

    /* TODO fetch the firebaseUser object and perform the user logout.
    fun logout(view: View) {
        repository.logout()
        view.context.startLoginActivity()
    }
     */
}