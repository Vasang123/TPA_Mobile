package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Category
import model.Comment
import repository.CategoryRepository
import repository.CommentRepository

class UpdateCategoryViewModel : ViewModel() {
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess
    fun validateUpdate(content : String, categoryId : String, context: Context) {
        var msg: String? = when {
            content.isEmpty() -> "Category can't be empty"
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        insertDB(content, categoryId){res->
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
            if(res == "Success"){
                _updateSuccess.postValue(true)
            }
        }

    }
    private fun insertDB(content: String, categoryId: String, completion: (String?) -> Unit){
        CategoryRepository.updateCategory(content,categoryId){ res->
            if(res !=  null){
                completion(res)
            }
        }
    }
}