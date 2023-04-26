package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Comment
import repository.CommentRepository
import repository.UserRepository
import java.util.*

class UpdateCommentViewModel : ViewModel() {
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess
    fun validateUpdate(content : String, commentId : String, context: Context) {
        var msg: String? = when {
            content.isEmpty() -> "Comment can't be empty"
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        insertDB(content, commentId){res->
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
            if(res == "Success"){
                _updateSuccess.postValue(true)
            }
        }

    }
    private fun insertDB(content: String, commentId : String, completion: (String?) -> Unit){
        var comment =  Comment()
        CommentRepository.updateComment(content,commentId){ res->
            if(res !=  null){
                completion(res)
            }
        }
    }
}