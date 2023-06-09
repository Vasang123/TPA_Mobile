package repository


import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import edu.bluejack22_2.BeeTech.LoginActivity
import edu.bluejack22_2.BeeTech.R
import model.User
import util.ActivityHelper
import java.util.*

object UserRepository {
    val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")
    fun insertUserData(user:User, context:Context, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val userRef = db.collection("users").document(uid)
        userRef.set(user)
            .addOnSuccessListener {
                completion(context.getString(R.string.success))
            }
            .addOnFailureListener {
                Log.e("createAccount", it.message.toString())
                completion(context.getString(R.string.error_creating_user))
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

    fun fetchUsers(
        onSuccess: (List<User>, DocumentSnapshot?, Boolean) -> Unit,
        onFailure: (String) -> Unit,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ) {
        val pageSize: Long = 5
        var usersRef = db.collection("users").orderBy("status", Query.Direction.DESCENDING)

        if (lastDocumentSnapshot != null) {
            usersRef = usersRef.startAfter(lastDocumentSnapshot)
        }

        usersRef.limit(pageSize).get()
            .addOnSuccessListener { querySnapshot ->
                val users = mutableListOf<User>()
                for (doc in querySnapshot.documents) {
                    val user = doc.toObject(User::class.java)!!
                    user.id = doc.id
                    users.add(user)
                }
                val newLastDocumentSnapshot = if (users.size < pageSize) {
                    null
                } else {
                    querySnapshot.documents.last()
                }

                val isEndOfList = users.size < pageSize
                onSuccess(users, newLastDocumentSnapshot, isEndOfList)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching users")
            }
    }

    fun updateUser(
        userId:String,
        data: String,
        email: String,
        field: String,
        context :Context,
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
                completion(context.getString(R.string.failed_to_update_username))
            }
        }.addOnFailureListener {
            completion(context.getString(R.string.failed_to_update_username))
        }
    }

    fun updateUserStatus(userId: String, status: String, onFailure: (String) -> Unit) {
        db.collection("users").document(userId).update("status", status)
            .addOnSuccessListener {
                ReviewRepository.updateReviewStatus(userId,status){

                }
            }
            .addOnFailureListener { e -> onFailure(e.message ?: "An error occurred") }
    }





}