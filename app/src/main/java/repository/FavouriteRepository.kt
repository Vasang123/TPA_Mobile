package repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import model.Review
import java.util.concurrent.CountDownLatch

object FavouriteRepository {
    val db = FirebaseFirestore.getInstance()

    fun addReviewToFavorites(userId: String, reviewId: String,
                             onSuccess: () -> Unit,
                             onFailure: (String) -> Unit) {
        val favoriteRef = db.collection("users").document(userId).collection("favorites").document(reviewId)
        val currentTime = System.currentTimeMillis()
        favoriteRef.set(hashMapOf("id" to reviewId, "timestamp" to currentTime))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error adding review to favorites")
            }
    }

    fun removeReviewFromFavorites(userId: String, reviewId: String,
                                  onSuccess: () -> Unit,
                                  onFailure: (String) -> Unit) {
        val favoriteRef = db.collection("users").document(userId).collection("favorites").document(reviewId)
        favoriteRef.delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error removing review from favorites")
            }
    }

    fun isReviewFavorite(userId: String, reviewId: String?, onSuccess: (Boolean) -> Unit, onFailure: (String) -> Unit) {
        if(reviewId.isNullOrEmpty()) {
            onFailure("ReviewId is null or empty")
            return
        }

//        Log.e("User Id  in isReviewFavorite", userId)
//        Log.e("Review Id  in isReviewFavorite", reviewId)

        val favoriteRef = db.collection("users").document(userId).collection("favorites").document(reviewId)
        favoriteRef.get()
            .addOnSuccessListener { document ->
                onSuccess(document.exists())
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error checking favorite status")
            }
    }


    fun updateCount(review : Review, increment : Boolean, onSuccess: (Long) -> Unit){
        val favoriteRef = db.collection("reviews").document(review.id)
        var newFavoriteCount : Long = 0

        db.runTransaction { transaction ->
            val currentReview = transaction.get(favoriteRef).toObject(Review::class.java)
            if (currentReview != null) {
                newFavoriteCount = if (increment) {
                    currentReview.totalFavorites + 1
                } else {
                    currentReview.totalFavorites - 1
                }
                transaction.update(favoriteRef, "totalFavorites", newFavoriteCount)

            }
        }.addOnSuccessListener {
            Log.d("success update count", "Review favorite count successfully updated!")
            onSuccess(newFavoriteCount)
        }.addOnFailureListener { e ->
            Log.w("failed update count", "Error updating review favorite count",e)
        }
    }

}