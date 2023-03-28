package Controller

import Model.User
import Util.FirebaseInterface
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

object FirebaseController : FirebaseInterface {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun createAccount(username: String, email: String, password: String, completion: (String?) -> Unit) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { res ->
                if (res.isSuccessful) {
                    val uid = UUID.randomUUID().toString()
                    val db = FirebaseFirestore.getInstance()
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
                    completion("Sucess")
                }else{
                    completion("Login Error")
                }
            }
    }


}