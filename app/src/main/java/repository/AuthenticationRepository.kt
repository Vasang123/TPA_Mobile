package repository


import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import edu.bluejack22_2.BeeTech.MainActivity
import edu.bluejack22_2.BeeTech.R
import model.User
import util.ActivityHelper
import java.util.*

object AuthenticationRepository {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun login(email: String, password: String, context: Context, completion: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{res ->
                if(res.isSuccessful){
                    completion(context.getString(R.string.success))
                }else{
                    completion(context.getString(R.string.login_error))
                }
            }
    }
    fun checkUserPassword(newPass:String, completion: (Boolean?) -> Unit){
        val  user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(user?.email.toString(), newPass)
        user?.reauthenticate(credential)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                completion(true)
            } else {
                completion(false)
            }
        }
    }
    fun updatePassword(newPass: String, context: Context, completion: (String?) -> Unit){
        val  user = auth.currentUser
        user?.updatePassword(newPass)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(context.getString(R.string.success))
                } else {
                    completion(context.getString(R.string.failed_to_update_password))
                }
            }

    }
    fun firebaseWithGoogleAuth(idToken:String, context: Context, activity: Activity){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = UUID.randomUUID().toString()
                    val userRef = UserRepository.db.collection("users").document(uid)
                    val user = auth.currentUser
                    val email = user?.email.toString()
                    val username: String = email?.substringBefore("@") ?: ""
                    val query = UserRepository.db.collection("users").whereEqualTo("email", email)
                    query.get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                UserRepository.insertUserData(user = User(username,email), context){ res ->
                                    Toast.makeText(context,res,Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                ActivityHelper.changePage(context, MainActivity::class.java)
                                activity.finish()
                            }
                        }
                        .addOnFailureListener {
                            Log.e("createAccount", it.message.toString())
                        }
                } else {
                    Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("signup error",task.exception?.message.toString());
                }
            }
    }

    fun createAccount(username: String, email: String, password: String, context: Context, completion: (String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { res ->
                if (res.isSuccessful) {
                    UserRepository.insertUserData(user = User(username,email), context ){ res ->
                        completion(res)
                    }
                }else {
                    Log.e("createAccount", res.exception?.message ?: "Unknown error")
                    completion(res.exception?.message)
                }
            }
    }
    fun signOut(activity: Activity) {
        auth.signOut()
        val gsc = GoogleSignIn.getClient(activity, GoogleSignInOptions.DEFAULT_SIGN_IN)
        gsc.signOut()
    }
    fun resetPassword(email: String, context: Context, completion: (String?) -> Unit){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion(context.getString(R.string.success_sending_email))
                } else {
                    completion(context.getString(R.string.error_sending_email))
                }
            }
    }
    fun checkSignInMethod(): Boolean {
        val currentUser = auth.currentUser
        return currentUser?.providerData?.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }
            ?: false
    }

}