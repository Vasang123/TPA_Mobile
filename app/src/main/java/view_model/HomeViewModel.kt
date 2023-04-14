package view_model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import model.Review
import repository.ItemRepository

class HomeViewModel : ViewModel() {
    private val _reviewlist = MutableLiveData<List<Review>>()
    val reviewList: LiveData<List<Review>> = _reviewlist
    val isLoading = MutableLiveData(false)
    val isLastPage = MutableLiveData(false)
    private val lastDocumentSnapshot = MutableLiveData<DocumentSnapshot?>(null)

    fun loadReviews(context: Context) {
        ItemRepository.getReviews({ reviewList, newLastDocumentSnapshot, isEndOfList ->
            _reviewlist.value = reviewList
            lastDocumentSnapshot.value = newLastDocumentSnapshot
            isLoading.value = false
            isLastPage.value = isEndOfList
        }, { errorMessage ->
            Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show()
            Log.e("Home Error", errorMessage)
            isLoading.value = false
        })
    }


    fun loadMoreReviews(context: Context) {
        if (isLoading.value == true || isLastPage.value == true) {
            return
        }

        isLoading.value = true

        ItemRepository.getReviews({ reviewList, newLastDocumentSnapshot, isEndOfList ->
            _reviewlist.value = _reviewlist.value?.plus(reviewList)
            lastDocumentSnapshot.value = newLastDocumentSnapshot
            isLoading.value = false
            isLastPage.value = isEndOfList
        }, { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            Log.e("Home Error 2", errorMessage)
            isLoading.value = false
        }, lastDocumentSnapshot.value)
    }
}