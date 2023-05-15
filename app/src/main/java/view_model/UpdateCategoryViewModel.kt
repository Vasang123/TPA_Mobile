package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
import repository.CategoryRepository

class UpdateCategoryViewModel : ViewModel() {
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess
    fun validateUpdate(content : String, categoryId : String, context: Context) {
        var msg: String? = when {
            content.isEmpty() -> context.getString(R.string.category_empty)
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        insertDB(content, categoryId, context){res->
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
            if(res == context.getString(R.string.success)){
                _updateSuccess.postValue(true)
            }
        }

    }
    private fun insertDB(content: String, categoryId: String, context: Context, completion: (String?) -> Unit){
        CategoryRepository.updateCategory(
            content, categoryId, context
        ){ res->
            if(res !=  null){
                completion(res)
            }
        }
    }
}