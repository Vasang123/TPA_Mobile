package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import model.Category
import model.Comment
import model.Review
import repository.CategoryRepository
import repository.CommentRepository
import repository.ReviewRepository
import repository.UserRepository
import java.util.*

class CategoryViewModel : ViewModel() {
    private val _categorylist = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> = _categorylist
    private val _category = MutableLiveData<Category>()
    val category: LiveData<Category> = _category
    val isLoading = MutableLiveData(false)
    val isLastPage = MutableLiveData(false)
    protected val lastDocumentSnapshot = MutableLiveData<DocumentSnapshot?>(null)
    fun loadCategory(context: Context) {
        CategoryRepository.fetchCategory(
            onSuccess = {  categories, newLastSnapshot, isEndOfList ->
                _categorylist.value = categories
                lastDocumentSnapshot.value = newLastSnapshot
                isLastPage.value = isEndOfList
                isLoading.value = false
            },
            onFailure = { errorMessage ->
                isLoading.value = false
            },
            lastDocumentSnapshot = lastDocumentSnapshot.value
        )
    }
    fun loadMoreCategory(){
        if (isLastPage.value == true || isLoading.value == true) return

        isLoading.value = true

        CategoryRepository.fetchCategory(
            onSuccess = { categories, newLastSnapshot, isEndOfList ->
                val currentList = _categorylist.value ?: emptyList()
                _categorylist.value = currentList + categories
                lastDocumentSnapshot.value = newLastSnapshot
                isLastPage.value = isEndOfList
                isLoading.value = false
            },
            onFailure = { errorMessage ->
                isLoading.value = false
            },
            lastDocumentSnapshot = lastDocumentSnapshot.value
        )
    }
    fun getCategoryById(categoryId : String){
        CategoryRepository.getCategoryById(categoryId){ res ->
            if(res != null){
                _category.postValue(res)
            }
        }
    }
}