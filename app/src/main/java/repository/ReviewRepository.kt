package repository

import android.net.Uri
import android.provider.Settings.Global
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import model.Review
import model.User
import java.util.*


object ReviewRepository {
    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun insertImage(file:Uri?, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val storageRef = storage.getReference("images/$uid")
        val uploadTask = file?.let { storageRef.putFile(it) }
        if (uploadTask != null) {
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                        if (urlTask.isSuccessful) {
                            val downloadUrl = urlTask.result
                            completion(downloadUrl.toString())
                        } else {
                            completion(task.exception.toString())
                        }
                    }
                } else {
                    completion(task.exception.toString())
                }
            }
        }
    }
    fun insertReview(review:Review,completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val reviewRef = db.collection("reviews").document(uid)
        reviewRef.set(review)
            .addOnSuccessListener {
                completion("Success")
            }
            .addOnFailureListener {
                Log.e("createReview", it.message.toString())
                completion("Error creating review record in database")
            }

    }

    fun getHomeReviews(onSuccess: (List<Review>, DocumentSnapshot?, Boolean) -> Unit,
                       onFailure: (String) -> Unit,
                       lastDocumentSnapshot: DocumentSnapshot? = null) {
        val pageSize : Long = 5
        var reviewsRef = db.collection("reviews")
            .whereEqualTo("status", "active")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(pageSize)
        if (lastDocumentSnapshot != null) {
            Log.e("doc", lastDocumentSnapshot.toString())
            reviewsRef = reviewsRef.startAfter(lastDocumentSnapshot)
        }
        reviewsRef.limit(pageSize).get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>()
                for (doc in querySnapshot.documents) {
                    val review = doc.toObject(Review::class.java)!!
                    reviews.add(review)
                }
                val newLastDocumentSnapshot = if (reviews.size < pageSize) {
                    null
                } else {
                    querySnapshot.documents.last()
                }

                val isEndOfList = reviews.size < pageSize
                onSuccess(reviews, newLastDocumentSnapshot,isEndOfList)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching reviews")
            }
    }

    fun updateReviewUsername(userId: String, data: String, completion: (String?) -> Unit) {
        val reviewsRef = UserRepository.db.collection("reviews").whereEqualTo("userId", userId)
        reviewsRef.get().addOnSuccessListener { querySnapshot ->
            val batch = UserRepository.db.batch()
            for (doc in querySnapshot.documents) {
                val reviewRef = UserRepository.db.collection("reviews").document(doc.id)
                batch.update(reviewRef, "username", data)
            }
            batch.commit().addOnSuccessListener {
                completion("Success")
            }.addOnFailureListener { exception ->
                completion(exception.localizedMessage)
            }
        }.addOnFailureListener { exception ->
            completion(exception.localizedMessage)
        }
    }
}