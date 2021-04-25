package loodos.droid.bitcointicker.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import loodos.droid.bitcointicker.data.repositories.UserRepository

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    // email and password for the input
    var email: String? = null
    var password: String? = null

    // auth listener
    var authListener: AuthListener? = null

    // disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val user by lazy {
        repository.currentUser()
    }

    /**
     * function to perform login
     */
    fun login() {
        // validating email and password
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password")
            return
        }

        //authentication started
        authListener?.onStarted()

        // calling login from repository to perform the actual authentication
        val disposable = repository.login(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    /**
     * function to perform signup
     */
    fun signup() {
        // validating email and password
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Please input all values")
            return
        }

        //authentication started
        authListener?.onStarted()

        // calling login from repository to perform the actual authentication
        val disposable = repository.register(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    /**
     * Opens the Signup Activity with a request from the user.
     */
    fun goToSignup(view: View) {
        Intent(view.context, SignupActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    /**
     * Opens the Login Activity with a request from the user.
     */
    fun goToLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

    /**
     * disposing the disposables
     */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}