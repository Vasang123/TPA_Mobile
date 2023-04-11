package repository


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import model.User
import java.util.*

object UserRepository {
    val db = FirebaseFirestore.getInstance()

    fun insertUserData(user:User, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val userRef = db.collection("users").document(uid)
        val userData = user
        userRef.set(userData)
            .addOnSuccessListener {
                completion("Success")
            }
            .addOnFailureListener {
                Log.e("createAccount", it.message.toString())
                completion("Error creating user record in database")
            }
    }
}