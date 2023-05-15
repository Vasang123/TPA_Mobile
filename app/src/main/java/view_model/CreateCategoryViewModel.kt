package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
import model.Category
import repository.CategoryRepository

class CreateCategoryViewModel : ViewModel() {
    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> = _createSuccess
    fun validateCreate(content : String, context: Context) {
        var msg: String? = when {
            content.isEmpty() -> context.getString(R.string.category_empty)
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        insertDB(content, context){res->
            if(res == context.getString(R.string.success)){
                Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                _createSuccess.postValue(true)
            }
        }

    }
    private fun insertDB(content: String, context: Context, completion: (String?) -> Unit){
        var category =  Category()
        category.name = content
        CategoryRepository.addCategory(category, context){ res->
            if(res !=  null){
                completion(res)
            }
        }
    }

}