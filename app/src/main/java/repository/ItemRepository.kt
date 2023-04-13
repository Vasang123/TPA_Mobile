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
    var storage = FirebaseStorage.getInstance()
    var db = FirebaseFirestore.getInstance()
    private var lastDocumentSnapshot: DocumentSnapshot? = null
    fun insertImage(file:Uri?, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        var storageRef = storage.getReference("images/$uid")
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

    fun getReviews(onSuccess: (List<Review>) -> Unit, onFailure: (String) -> Unit) {
        val reviewsRef = db.collection("reviews")
            .whereEqualTo("status", "active")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(10)

        if (lastDocumentSnapshot != null) {
            reviewsRef.startAfter(lastDocumentSnapshot!!)
        }

        reviewsRef.addSnapshotListener { value, error ->
            if (error != null) {
                onFailure(error.message!!)
                return@addSnapshotListener
            }

            val reviews = mutableListOf<Review>()

            for (doc in value!!.documents) {
                val review = doc.toObject(Review::class.java)!!
                reviews.add(review)
            }
            if (reviews.isNotEmpty()) {
                lastDocumentSnapshot = value.documents[value.size() - 1]
            }
            onSuccess(reviews)
        }
    }
}