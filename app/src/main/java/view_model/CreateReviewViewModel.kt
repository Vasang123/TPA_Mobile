package view_model


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
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
            file == null -> context.getString(R.string.file_empty)
            file == Uri.EMPTY -> context.getString(R.string.file_empty)
            file.path.isNullOrEmpty() -> context.getString(R.string.invalid_path)
            title.isEmpty() -> context.getString(R.string.title_empty)
            description.isEmpty() -> context.getString(R.string.description_empty)
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
                insertDB(imageUrl,title,description,category,context){res->
                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                    if(res == context.getString(R.string.success)){
                        _createSuccess.postValue(true)
                    }
                }
            }
        }

    }
    private fun insertDB(imageUrl: String, title: String, description: String, category : Category, context: Context, completion: (String?) -> Unit){
        var review =  Review()
        UserRepository.getLoggedUser(){ user ->
            if(user != null){
                review.userId = user.id
                review.username = user.username
                review.createdAt= Date()
                review.updatedAt= Date()
                review.imageURL= imageUrl
                review.title= title
                review.description= description
                review.status = user.status
                review.categoryId = category.id
                review.categoryName = category.name
                ReviewRepository.insertReview(
                    review,
                    context

                ) { res->
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