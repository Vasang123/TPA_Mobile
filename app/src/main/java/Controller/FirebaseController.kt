package Controller

import edu.Model.User
import Util.ActivtiyHelper
import Util.FirebaseInterface
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import edu.bluejack22_2.BeeTech.MainActivity
import java.util.UUID

object FirebaseController : FirebaseInterface {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    override fun createAccount(username: String, email: String, password: String, completion: (String?) -> Unit) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { res ->
                if (res.isSuccessful) {
                    val uid = UUID.randomUUID().toString()
                    val userRef = db.collection("users").document(uid)
                    val userData = User(uid, username, email)
                    userRef.set(userData)
                        .addOnSuccessListener {
                            completion("Success")
                        }
                        .addOnFailureListener {
                            Log.e("createAccount", it.message.toString())
                            completion("Error creating user record in database")
                        }
                }else {
                    Log.e("createAccount", res.exception?.message ?: "Unknown error")
                    completion(res.exception?.message)
                }
            }
    }

    override fun login(email: String, password: String, completion: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{res ->
                if(res.isSuccessful){
                    completion("Success")
                }else{
                    completion("Login Error")
                }
            }
    }

    override fun signOut() {
        auth.signOut()
    }

    fun firebaseWithGoogleAuth(idToken:String, context: Context, activity: Activity){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = UUID.randomUUID().toString()
                    val userRef = db.collection("users").document(uid)
                    val user = auth.currentUser
                    val email = user?.email.toString()
                    val username: String = email?.substringBefore("@") ?: ""
                    val query = db.collection("users").whereEqualTo("email", email)
                    query.get()
                        .addOnSuccessListener { documents ->
                            if (documents.isEmpty) {
                                // The email is unique, insert the user data into Firestore
                                val uid = UUID.randomUUID().toString()
                                val userRef = db.collection("users").document(uid)
                                val userData = User(uid, username, email)
                                userRef.set(userData)
                                    .addOnSuccessListener {
                                        ActivtiyHelper.changePage(context,MainActivity::class.java)
                                        activity.finish()
                                    }
                                    .addOnFailureListener {
                                        Log.e("createAccount", it.message.toString())
                                    }
                            } else {
                                ActivtiyHelper.changePage(context,MainActivity::class.java)
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
}