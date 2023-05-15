package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.CategoryRepository

class DeleteCategoryViewModel : ViewModel() {
    private val _success = MutableLiveData<String>()
    public val success: LiveData<String> = _success
    fun deleteCategory(context: Context, categoryId: String) {
        CategoryRepository.deleteCategory(
            categoryId,
            context
        ) { res ->
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
            _success.value = res
        }
    }
}