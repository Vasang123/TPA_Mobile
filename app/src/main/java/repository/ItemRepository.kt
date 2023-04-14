package repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import model.Review
import java.util.*


object ItemRepository {
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

    fun getReviews(onSuccess: (List<Review>, DocumentSnapshot?, Boolean) -> Unit,
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

        // Limit the query to the given page size.
        reviewsRef.limit(pageSize).get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>()

                for (doc in querySnapshot.documents) {
                    val review = doc.toObject(Review::class.java)!!
                    reviews.add(review)
                }

                Log.e("Fetched reviews", reviews.toString())

                // If the number of results is less than the page size, it means we have reached the end of the results.
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
}