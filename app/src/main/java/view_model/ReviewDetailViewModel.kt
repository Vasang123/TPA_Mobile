package view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.ReviewDetailActivity
import model.Review
import repository.ReviewRepository
import util.ActivityHelper

class ReviewDetailViewModel : ViewModel() {
    private val _review = MutableLiveData<Review>()
    val review: LiveData<Review> = _review

    fun viewDetail(context: Context, reviewId : String){
        ReviewRepository.getReviewById(
            onSuccess = { review ->
                _review.postValue(review)
            },
            onFailure = { errorMessage ->
                Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show()
            },
            reviewId
        )

    }
    fun resetReview() {
        _review.value = null
    }
}