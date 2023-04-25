package view_model


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Category
import model.Review
import repository.ReviewRepository
import repository.UserRepository
import java.util.Date

class CreateReviewViewModel : ViewModel(){
    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> = _createSuccess

    fun validateCreate(file: Uri?, title: String, description: String, context: Context, category : Category) {

        var msg: String? = when {
            file == null -> "File can't be empty"
            file == Uri.EMPTY -> "No file selected"
            file.path.isNullOrEmpty() -> "Invalid file path"
            title.isEmpty() -> "Title can't be empty"
            description.isEmpty() -> "Description can't be empty"
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
            return
        }
        var imageUrl: String = ""
        insertImage(file){ res->
            if (res != null) {
                imageUrl = res
                insertDB(imageUrl,title,description,category){res->
                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                    if(res == "Success"){
                        _createSuccess.postValue(true)
                    }
                }
            }
        }

    }
    private fun insertDB(imageUrl: String, title: String, description: String, category : Category, completion: (String?) -> Unit){
        var review =  Review()
        UserRepository.getLoggedUser(){ user ->
            if(user != null){
                review.userId = user.id
                review.username = ""
                review.createdAt= Date()
                review.updatedAt= Date()
                review.imageURL= imageUrl
                review.title= title
                review.description= description
                review.status = user.status
                review.categoryId = category.id
                review.categoryName = category.name
                ReviewRepository.insertReview(review){ res->
                    if(res !=  null){
                        completion(res)
                    }
                }
            }
        }
    }
    private fun insertImage(file: Uri?, completion: (String?) -> Unit){
        ReviewRepository.insertImage(file){ res->
            if(res != null){
                completion(res)
            }
        }
    }


}