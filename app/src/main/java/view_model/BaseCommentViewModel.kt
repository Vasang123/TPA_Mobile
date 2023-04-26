package view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import model.Comment

abstract class BaseCommentViewModel : ViewModel() {
    protected val _commentList = MutableLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>> = _commentList
    val isLoading = MutableLiveData(false)
    val isLastPage = MutableLiveData(false)
    protected val lastDocumentSnapshot = MutableLiveData<DocumentSnapshot?>(null)

    abstract fun loadComments(reviewId: String, context: Context)

    fun loadMoreComments(reviewId: String, context: Context, loadMore: (Context, DocumentSnapshot?, MutableLiveData<List<Comment>>, MutableLiveData<DocumentSnapshot?>, MutableLiveData<Boolean>, MutableLiveData<Boolean>) -> Unit) {
        if (isLoading.value == true || isLastPage.value == true) {
            return
        }

        isLoading.value = true

        loadMore(context, lastDocumentSnapshot.value, _commentList, lastDocumentSnapshot, isLoading, isLastPage)
    }
}
