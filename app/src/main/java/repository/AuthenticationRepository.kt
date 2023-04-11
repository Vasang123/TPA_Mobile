package repository


import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack22_2.BeeTech.MainActivity
import model.User
import util.ActivtiyHelper
import java.util.*

object AuthenticationRepository {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun login(email: String, password: String, completion: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{res ->
                if(res.isSuccessful){
                    completion("Success")
                }else{
                    completion("Login Error")
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
                                UserRepository.insertUserData(user = User(username,email)){ res ->
                                    Toast.makeText(context,res,Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                ActivtiyHelper.changePage(context, MainActivity::class.java)
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

    fun createAccount(username: String, email: String, password: String, completion: (String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { res ->
                if (res.isSuccessful) {
                    UserRepository.insertUserData(user = User(username,email)){ res ->
                        completion(res)
                    }
                }else {
                    Log.e("createAccount", res.exception?.message ?: "Unknown error")
                    completion(res.exception?.message)
                }
            }
    }
    fun signOut() {
        auth.signOut()
    }
    fun resetPassword(email:String, completion: (String?) -> Unit){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    completion("Email Successfuly Sent")
                } else {
                    completion("Error Sending Email")
                }
            }
    }

}