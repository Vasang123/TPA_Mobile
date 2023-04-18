package repository


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import model.User
import java.util.*

object UserRepository {
    val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
    fun insertUserData(user:User, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val userRef = db.collection("users").document(uid)
        userRef.set(user)
            .addOnSuccessListener {
                completion("Success")
            }
            .addOnFailureListener {
                Log.e("createAccount", it.message.toString())
                completion("Error creating user record in database")
            }
    }
    fun getLoggedUser(completion: (User?) -> Unit) {
        val email = AuthenticationRepository.auth.currentUser?.email.toString()
        val query = db.collection("users").whereEqualTo("email", email)
        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val user = document.toObject<User>()
                user?.id = document.id
                completion(user)
            } else {
                completion(null)
            }
        }.addOnFailureListener {
            completion(null)
        }
    }
    fun updateUser(
        userId:String,
        data: String,
        email: String,
        field: String,
        completion: (String?) -> Unit
    ) {
        val query = db.collection("users").whereEqualTo("email", email)
        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val documentId = document.id
                userCollection.document(documentId).update(field, data)
                    .addOnSuccessListener {
                        ReviewRepository.updateReviewUsername(userId,data){
                            completion(it)
                        }
                    }.addOnFailureListener { exception ->
                        completion(exception.localizedMessage)

                    }
            } else {
                completion("Failed to Update Username")
            }
        }.addOnFailureListener {
            completion("Failed to Update Username")
        }
    }


}