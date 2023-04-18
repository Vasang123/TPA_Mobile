package repository

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




}