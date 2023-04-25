package repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
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
                completion("Error creating review record in database")
            }

    }

    fun fetchReviews(
        configureQuery: (Query) -> Query,
        onSuccess: (List<Review>, DocumentSnapshot?, Boolean) -> Unit,
        onFailure: (String) -> Unit,
        lastDocumentSnapshot: DocumentSnapshot? = null
    ) {
        val pageSize: Long = 5
        var reviewsRef = db.collection("reviews")
            .whereEqualTo("status", "active")
        reviewsRef = configureQuery(reviewsRef)

        if (lastDocumentSnapshot != null) {
            reviewsRef = reviewsRef.startAfter(lastDocumentSnapshot)
        }

        reviewsRef.limit(pageSize).get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>()
                for (doc in querySnapshot.documents) {
                    val review = doc.toObject(Review::class.java)!!
                    review?.id = doc.id
                    Log.e("reviews fav count", review.totalFavorites.toString())
                    reviews.add(review)
                }
                val newLastDocumentSnapshot = if (reviews.size < pageSize) {
                    null
                } else {
                    querySnapshot.documents.last()
                }

                val isEndOfList = reviews.size < pageSize
                onSuccess(reviews, newLastDocumentSnapshot, isEndOfList)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching reviews")
            }
    }

    fun getHomeReviews(onSuccess: (List<Review>, DocumentSnapshot?, Boolean) -> Unit,
                       onFailure: (String) -> Unit,
                       lastDocumentSnapshot: DocumentSnapshot? = null) {
        fetchReviews(
            configureQuery = { query ->
                query.orderBy("createdAt", Query.Direction.DESCENDING)
            },
            onSuccess = onSuccess,
            onFailure = onFailure,
            lastDocumentSnapshot = lastDocumentSnapshot
        )
    }
    fun getUserReviews(userId: String, onSuccess: (List<Review>, DocumentSnapshot?, Boolean) -> Unit,
                       onFailure: (String) -> Unit,
                       lastDocumentSnapshot: DocumentSnapshot? = null, ) {
        fetchReviews(
            configureQuery = { query ->
                query
                    .whereEqualTo("userId", userId)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
            },
            onSuccess = onSuccess,
            onFailure = onFailure,
            lastDocumentSnapshot = lastDocumentSnapshot
        )
    }
    fun getAllReviews(
        onSuccess: (List<Review>) -> Unit,
        onFailure: (String) -> Unit) {

        var reviewsRef = db.collection("reviews")
            .whereEqualTo("status", "active")
        reviewsRef.get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>()
                for (doc in querySnapshot.documents) {
                    val review = doc.toObject(Review::class.java)
                    if (review != null) {
                        review?.id = doc.id
                        reviews.add(review)
                    }
                }
                reviews.shuffle()
                onSuccess(reviews)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching reviews")
            }
    }

    fun getReviewById(
        onSuccess: (Review?) -> Unit,
        onFailure: (String) -> Unit,
        reviewId : String) {
        var reviewsRef = db.collection("reviews")
            .document(reviewId)

        reviewsRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val review = documentSnapshot.toObject<Review>()
                review?.id = documentSnapshot.id
                onSuccess(review)
            }
        }.addOnFailureListener { e ->
            onFailure(e.message ?: "Error fetching review detail")
        }


    }

    fun getSearchReviews(
        onSuccess: (List<Review>) -> Unit,
        onFailure: (String) -> Unit,
        searchQuery: String) {

        var reviewsRef = db.collection("reviews")
            .whereEqualTo("status", "active")
            .whereGreaterThanOrEqualTo("title", searchQuery)
            .whereLessThanOrEqualTo("title", searchQuery + "\uf8ff")
        reviewsRef.get()
            .addOnSuccessListener { querySnapshot ->
                val reviews = mutableListOf<Review>()

                for (doc in querySnapshot.documents) {
                    val review = doc.toObject(Review::class.java)
                    if (review != null) {
                        review?.id = doc.id
                        reviews.add(review)
                    }
                }
                onSuccess(reviews)
            }
            .addOnFailureListener { e ->
                Log.e("123",e.message.toString())
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