package view_model

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Category
import model.Comment
import model.Review
import repository.CommentRepository
import repository.ReviewRepository
import repository.UserRepository
import java.util.*

class CreateCommentViewModel : ViewModel() {
    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> = _createSuccess
    fun validateCreate(content : String, reviewId : String, context: Context) {
        var msg: String? = when {
            content.isEmpty() -> "Comment can't be empty"
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        insertDB(content, reviewId){res->
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
            if(res == "Success"){
                _createSuccess.postValue(true)
            }
        }

    }
    private fun insertDB(content: String, reviewId : String, completion: (String?) -> Unit){
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
                CommentRepository.insertComment(comment){ res->
                    if(res !=  null){
                        completion(res)
                    }
                }
            }
        }
    }

}