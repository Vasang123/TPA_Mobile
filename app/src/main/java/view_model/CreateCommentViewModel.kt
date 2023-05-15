package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
import model.Comment
import repository.CommentRepository
import repository.UserRepository
import java.util.*

class CreateCommentViewModel : ViewModel() {
    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> = _createSuccess
    fun validateCreate(content : String, reviewId : String, context: Context) {
        var msg: String? = when {
            content.isEmpty() -> context.getString(R.string.comment_empty)
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        insertDB(content, reviewId, context){res->
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
            if(res == context.getString(R.string.success)){
                _createSuccess.postValue(true)
            }
        }

    }
    private fun insertDB(content: String, reviewId : String, context: Context, completion: (String?) -> Unit){
        var comment =  Comment()
        UserRepository.getLoggedUser(){ user ->
            if(user != null){
                comment.userId = user.id
                comment.username = user.username
                comment.status = user.status
                comment.createdAt= Date()
                comment.updatedAt= Date()
                comment.content = content
                comment.reviewId = reviewId
                CommentRepository.insertComment(
                    comment,
                    context
                ){ res->
                    if(res !=  null){
                        completion(res)
                    }
                }
            }
        }
    }

}