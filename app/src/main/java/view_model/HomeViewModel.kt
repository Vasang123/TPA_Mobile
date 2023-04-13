package view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Review
import repository.ItemRepository

class HomeViewModel : ViewModel() {
    private val _reviewlist = MutableLiveData<List<Review>>()
    val reviewList: LiveData<List<Review>> = _reviewlist


    fun loadReviews(context: Context) {
        ItemRepository.getReviews({ reviewList ->
            _reviewlist.value = reviewList
        }, { errorMessage ->
            Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show()
            Log.e("Home Error", errorMessage)
        })
    }

}