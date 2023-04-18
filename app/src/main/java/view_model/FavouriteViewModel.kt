package view_model

import android.util.Log
import android.widget.ImageView
import edu.bluejack22_2.BeeTech.R
import repository.FavouriteRepository

class FavouriteViewModel {

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


}