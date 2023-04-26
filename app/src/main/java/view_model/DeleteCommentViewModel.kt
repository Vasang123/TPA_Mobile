package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.CommentRepository
import repository.ReviewRepository

class DeleteCommentViewModel : ViewModel() {
    private val _success = MutableLiveData<String>()
    public val success: LiveData<String> = _success
    fun deleteComment(context: Context, commentId: String) {
        CommentRepository.deleteComment(commentId){ res ->
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
            _success.value = res
        }
    }
}