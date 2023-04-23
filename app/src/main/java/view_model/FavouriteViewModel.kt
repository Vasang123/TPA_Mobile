package view_model

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import edu.bluejack22_2.BeeTech.R
import model.Review
import repository.FavouriteRepository

class FavouriteViewModel {
    val favoriteStatus = MutableLiveData<Pair<String, Boolean>>()
    val updatedFavoriteCount = MutableLiveData<Pair<String, Long>>()
    val operationError = MutableLiveData<String>()


    fun isReviewFavorited(userId: String, reviewId: String, onResult: (Boolean) -> Unit) {
        FavouriteRepository.isReviewFavorite(userId, reviewId, { isFavorited ->
            onResult(isFavorited)
        }, { errorMessage ->
            Log.e("Fav Error", errorMessage)
        })
    }

    fun updateFavoriteIndicator(favouriteButton : ImageView, isFavorited: Boolean) {
        val heartIcon = if (isFavorited) {
            R.drawable.ic_baseline_favorite_24
        } else {
            R.drawable.ic_baseline_favorite_border_24
        }
        favouriteButton.setImageResource(heartIcon)
    }

    fun removeReviewFromFavorites(userId: String, reviewId: String) {
        FavouriteRepository.removeReviewFromFavorites(
            userId, reviewId,
            onSuccess = {
                favoriteStatus.value = Pair(reviewId, true)
            },
            onFailure = { error ->
                operationError.value = error
            }
        )
    }

    fun addReviewToFavorites(userId: String, reviewId: String) {
        FavouriteRepository.addReviewToFavorites(
            userId, reviewId,
            onSuccess = {
                favoriteStatus.value = Pair(reviewId, false)
            },
            onFailure = { error ->
                operationError.value = error
            }
        )
    }

    fun updateFavoriteCount(review : Review, option : Boolean, onResult: (Long) -> Unit){
        FavouriteRepository.updateCount(review, option) {
                newCount -> updatedFavoriteCount.value = Pair(review.id, newCount)
                onResult(newCount)
        }
    }




}