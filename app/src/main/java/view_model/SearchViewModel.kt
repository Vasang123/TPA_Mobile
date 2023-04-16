package view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Review
import repository.ReviewRepository

class SearchViewModel : ViewModel(){
    private val _reviewlist = MutableLiveData<List<Review>>()
    val reviewList: LiveData<List<Review>> = _reviewlist
    val isLoading = MutableLiveData(false)
    fun loadReviews(context: Context, searchQuery: String?) {

        if (searchQuery != null) {
            if (searchQuery.isEmpty()) {
                ReviewRepository.getAllReviews(
                    onSuccess = { reviews->
                        _reviewlist.value = reviews
                    },
                    onFailure = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }else{

                ReviewRepository.getSearchReviews(
                    onSuccess = { reviews->
                        _reviewlist.value = reviews
                    },
                    onFailure = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    },
                    searchQuery
                )
            }
        }
    }

}