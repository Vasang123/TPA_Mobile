package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Category
import model.Review
import repository.CategoryRepository
import repository.ReviewRepository

class CategoryViewModel : ViewModel() {
    private val _categorylist = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categorylist
    fun loadCategory(context: Context,) {
        CategoryRepository.getAllCategory(
            onSuccess = { categories->
                _categorylist.value = categories
            },
            onFailure = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        )
    }
}