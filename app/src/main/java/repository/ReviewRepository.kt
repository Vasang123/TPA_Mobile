package repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack22_2.BeeTech.R
import model.Review
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
    fun insertReview(review: Review,  context: Context, completion: (String?) -> Unit){
        val uid = UUID.randomUUID().toString()
        val reviewRef = db.collection("reviews").document(uid)
        reviewRef.set(review)
            .addOnSuccessListener {
                completion(context.getString(R.string.success))
            }
            .addOnFailureListener {
                completion(context.getString(R.string.error_creating_review))
            }

    }
    fun deleteReview(reviewId:String, context: Context, completion: (String?) -> Unit){
        val reviewRef = db.collection("reviews").document(reviewId)
        reviewRef.delete()
            .addOnSuccessListener {
                completion(context.getString(R.string.success))
            }
            .addOnFailureListener {
                completion(context.getString(R.string.failed_to_delete_review))
            }

    }
    fun fetchReviews(
        configureQuery: (Query) -> Query,
        onSuccess: (List<Review>, DocumentSnapshot?, Boolean) -> Unit,
        onFailure: (String) -> Unit,
        lastDocumentSnapshot: DocumentSnapshot? = null,
        ref : String = "reviews"
    ) {
        val pageSize: Long = 5
        var reviewsRef = db.collection(ref)
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
    fun fetchUserSpesificReviews(
        configureQuery: (Query) -> Query,
        onSuccess: (List<Review>, DocumentSnapshot?, Boolean) -> Unit,
        onFailure: (String) -> Unit,
        lastDocumentSnapshot: DocumentSnapshot? = null,
        ref : String = "reviews",
        userId: String
    ) {
        val pageSize: Long = 5
        var reviewsRef = db.collection(ref)
            .whereEqualTo("status", "active")
            .whereEqualTo("userId", userId)
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
    fun getUserSpesficReviews(onSuccess: (List<Review>, DocumentSnapshot?, Boolean) -> Unit,
                       onFailure: (String) -> Unit,
                       lastDocumentSnapshot: DocumentSnapshot? = null,
                        userId: String) {
        fetchUserSpesificReviews(
            configureQuery = { query ->
                query.orderBy("createdAt", Query.Direction.DESCENDING)
            },
            onSuccess = onSuccess,
            onFailure = onFailure,
            lastDocumentSnapshot = lastDocumentSnapshot,
            userId = userId
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

    fun fetchFavoriteReviews(
        userId: String,
        lastTimestamp: Long? = null,
        onSuccess: (List<Review>, Long?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val pageSize = 5
        Log.e("User Id inside fetchFavoriteReviews ", userId)

        var favoritesRef: Query = db.collection("users").document(userId).collection("favorites")

        if (lastTimestamp != null) {
            favoritesRef = favoritesRef.whereGreaterThan("timestamp", lastTimestamp)
        }

        favoritesRef.orderBy("timestamp").limit(pageSize.toLong()).get()
            .addOnSuccessListener { favoritesSnapshot ->
                val reviewIds = favoritesSnapshot.documents.map { it.id }
                Log.e("Review Ids inside fetchFavoriteReviews", reviewIds.toString())

                // If there are no more favorite reviews, call onSuccess with an empty list and null for the last review ID
                if (reviewIds.isEmpty()) {
                    onSuccess(listOf(), null)
                    return@addOnSuccessListener
                }

                val reviewsRef = db.collection("reviews")

                reviewsRef.whereIn(FieldPath.documentId(), reviewIds).get()
                    .addOnSuccessListener { reviewsSnapshot ->
                        val reviews = reviewsSnapshot.documents.mapNotNull { document ->
                            val review = document.toObject(Review::class.java)
                            review?.id = document.id
                            review
                        }
                        val newLastTimestamp = favoritesSnapshot.documents.lastOrNull()?.getLong("timestamp")
                        onSuccess(reviews, newLastTimestamp)
                    }
                    .addOnFailureListener { e ->
                        onFailure(e.message ?: "Error fetching reviews")
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching favorite reviews")
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
    fun updateReviewCategory(categoryId: String, data: String, completion: (String?) -> Unit) {
        val reviewsRef = db.collection("reviews").whereEqualTo("categoryId", categoryId)
        reviewsRef.get().addOnSuccessListener { querySnapshot ->
            val batch = db.batch()
            for (doc in querySnapshot.documents) {
                val reviewRef = db.collection("reviews").document(doc.id)
                batch.update(reviewRef, "categoryName", data)
            }
            batch.commit().addOnSuccessListener {
                completion("Done")
            }.addOnFailureListener { exception ->
                completion(exception.localizedMessage)
            }
        }.addOnFailureListener { exception ->
            completion(exception.localizedMessage)
        }
    }
    fun updateReviewStatus(userId: String, data: String, completion: (String?) -> Unit) {
        val reviewsRef = db.collection("reviews").whereEqualTo("userId", userId)
        reviewsRef.get().addOnSuccessListener { querySnapshot ->
            val batch = db.batch()
            for (doc in querySnapshot.documents) {
                val reviewRef = db.collection("reviews").document(doc.id)
                batch.update(reviewRef, "status", data)
            }
            batch.commit().addOnSuccessListener {
                completion("Done")
            }.addOnFailureListener { exception ->
                completion(exception.localizedMessage)
            }
        }.addOnFailureListener { exception ->
            completion(exception.localizedMessage)
        }
    }
    fun updateReview(
        review: Review,
        completion: (String?) -> Unit
    ) {
        val documentRef = db.collection("reviews").document(review.id)
        documentRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                documentRef.update(
                    "title", review.title,
                    "description", review.description,
                    "categoryId", review.categoryId,
                    "categoryName", review.categoryName,
                    "imageURL", review.imageURL,
                    "updatedAt", Date()
                ).addOnSuccessListener {
                    completion("Success")
                }.addOnFailureListener { exception ->
                    completion("Failed to update review: ${exception.message}")
                }
            } else {
                completion("Review document with ID '${review.id}' not found")
            }
        }.addOnFailureListener {
            completion("Failed to Update Review")
        }
    }

    fun getAllReviewsByUsername(
        username : String,
        onSuccess: (List<Review>) -> Unit,
        onFailure: (String) -> Unit) {

        var reviewsRef = db.collection("reviews")
            .whereEqualTo("username", username)
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
                onFailure(e.message ?: "Error fetching reviews")
            }
    }

}