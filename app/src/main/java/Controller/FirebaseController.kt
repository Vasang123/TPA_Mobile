package Controller

import Model.User
import Util.FirebaseInterface
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

object FirebaseController : FirebaseInterface {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
                    completion("Success")
                }else{
                    completion("Login Error")
                }
            }
    }
    override fun createAccountWIthGoogle(context:Context) {
        val signInAccount = GoogleSignIn.getLastSignedInAccount(context)
        if (signInAccount != null) {
            val credential = GoogleAuthProvider.getCredential(signInAccount.idToken, null)
            FirebaseController.auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseController.auth.currentUser
                        if (user != null) {
                            val email = user.email
                            val usernameField:String = ""
                            createAccount(
                                usernameField.toString(),
                                email ?: "",
                                ""
                            ) { result ->
                                Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Google sign in failed: ${task.exception?.localizedMessage}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(context, "Please sign in with Google first", Toast.LENGTH_SHORT).show()
        }
    }

    override fun signOut() {
        auth.signOut()
    }


}