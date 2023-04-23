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
        val favoriteRef = db.collection("favorites").document(userId)
        favoriteRef.get()
            .addOnSuccessListener { document ->
                val currentFavorites = document.data?.toMutableMap() ?: mutableMapOf()
                currentFavorites[reviewId] = true
                favoriteRef.set(currentFavorites)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e.message ?: "Error adding review to favorites")
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching favorites")
            }
    }

    fun removeReviewFromFavorites(userId: String, reviewId: String,
                                  onSuccess: () -> Unit,
                                  onFailure: (String) -> Unit) {
        val favoriteRef = db.collection("favorites").document(userId)
        favoriteRef.get()
            .addOnSuccessListener { document ->
                val currentFavorites = document.data?.toMutableMap() ?: mutableMapOf()
                currentFavorites.remove(reviewId)
                favoriteRef.set(currentFavorites)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e.message ?: "Error removing review from favorites")
                    }
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching favorites")
            }
    }

    fun isReviewFavorite(userId: String, reviewId: String, onSuccess: (Boolean) -> Unit, onFailure: (String) -> Unit) {
        val favoriteRef = db.collection("favorites").document(userId)
        favoriteRef.get()
            .addOnSuccessListener { document ->
                val currentFavorites = document.data ?: mapOf<String, Any>()
                onSuccess(currentFavorites.containsKey(reviewId))
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error checking favorite status")
            }
    }


    fun getFavoriteReviews(userId: String, onSuccess: (List<Review>) -> Unit, onFailure: (String) -> Unit) {
        val favoriteRef = db.collection("favorites").document(userId)
        favoriteRef.get()
            .addOnSuccessListener { document ->
                val favoriteIds = document.data?.keys ?: emptySet<String>()
                val reviews = mutableListOf<Review>()
                val countDownLatch = CountDownLatch(favoriteIds.size)

                favoriteIds.forEach { reviewId ->
                    db.collection("reviews").document(reviewId).get()
                        .addOnSuccessListener { reviewDoc ->
                            val review = reviewDoc.toObject(Review::class.java)
                            if (review != null) {
                                reviews.add(review)
                            }
                            countDownLatch.countDown()
                        }
                        .addOnFailureListener { e ->
                            onFailure(e.message ?: "Error fetching favorite reviews")
                            countDownLatch.countDown()
                        }
                }

                countDownLatch.await()
                onSuccess(reviews)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error fetching favorites")
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