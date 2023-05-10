package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Category
import repository.CategoryRepository

class CreateCategoryViewModel : ViewModel() {
    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> = _createSuccess
    fun validateCreate(content : String, context: Context) {
        var msg: String? = when {
            content.isEmpty() -> "Category can't be empty"
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        insertDB(content){res->
            if(res == "Success"){
                Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                _createSuccess.postValue(true)
            }
        }

    }
    private fun insertDB(content: String, completion: (String?) -> Unit){
        var category =  Category()
        category.name = content
        CategoryRepository.addCategory(category){ res->
            if(res !=  null){
                completion(res)
            }
        }
    }

}