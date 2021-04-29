package loodos.droid.bitcointicker.data.firebase

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import loodos.droid.bitcointicker.util.CustomException
import javax.inject.Inject

class FirebaseSource @Inject constructor(){

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    /**
     * It tries to login the FirebaseAuth system with
     * the information received from the user and reports the result.
     *
     * @param email address to login
     * @param password of the user to login
     */
    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null && currentUser.isEmailVerified) {
                        emitter.onComplete()
                    } else {
                        if (it.exception != null) {
                            emitter.onError(it.exception!!)
                        } else {
                            emitter.onError(CustomException("Please Confirm Your E-Mail Address"))
                        }
                    }
                } else emitter.onError(it.exception!!)
            }
        }
    }

    /**
     * It tries to register the FirebaseAuth system with
     * the information received from the user and reports the result.
     *
     * @param email address to register
     * @param password of the user to register
     */
    fun register(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    sendMail(emitter)
                } else {
                    emitter.onError(it.exception!!)
                }
            }
        }
    }

    /**
     * An e-mail is sent for the user's approval.
     *
     * @param emitter notifies the result up
     */
    private fun sendMail(emitter: CompletableEmitter) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                logout()
                emitter.onComplete()
            } else {
                emitter.onError(it.exception!!)
            }
        }
    }

    /**
     * logout FirebaseAuth
     */
    fun logout() = firebaseAuth.signOut()

    /**
     * Returns information of the current user
     */
    fun currentUser() = firebaseAuth.currentUser
}