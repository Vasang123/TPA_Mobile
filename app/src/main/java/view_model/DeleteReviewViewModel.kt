package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Category
import repository.CategoryRepository
import repository.ReviewRepository

class DeleteReviewViewModel : ViewModel() {
    private val _success = MutableLiveData<String>()
    public val success: LiveData<String> = _success
    fun deleteReview(context: Context, reviewId: String) {
        ReviewRepository.deleteReview(reviewId){ res ->
            Toast.makeText(context, res,Toast.LENGTH_SHORT).show()
            _success.value = res
        }
    }
}