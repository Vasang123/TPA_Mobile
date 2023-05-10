package view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import model.Review

abstract class BaseUserSpesificViewModel : ViewModel() {
    protected val _reviewList = MutableLiveData<List<Review>>()
    val reviewList: LiveData<List<Review>> = _reviewList
    val isLoading = MutableLiveData(false)
    val isLastPage = MutableLiveData(false)
    protected val lastDocumentSnapshot = MutableLiveData<DocumentSnapshot?>(null)

    abstract fun loadReviews(context: Context, userId :String)

    fun loadMoreReviews(context: Context, userId: String,
                        loadMore: (Context, DocumentSnapshot?,
                                   MutableLiveData<List<Review>>,
                                   MutableLiveData<DocumentSnapshot?>,
                                   MutableLiveData<Boolean>,
                                   MutableLiveData<Boolean>,
                                   userId: String) -> Unit
    ) {
        if (isLoading.value == true || isLastPage.value == true) {
            return
        }

        isLoading.value = true

        loadMore(context, lastDocumentSnapshot.value, _reviewList, lastDocumentSnapshot, isLoading, isLastPage, userId)
    }

}