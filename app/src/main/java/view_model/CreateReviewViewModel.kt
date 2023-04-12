package view_model


import android.content.ClipData.Item
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Review
import repository.ItemRepository
import repository.UserRepository
import java.io.File
import java.util.Date

class CreateReviewViewModel : ViewModel(){
    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> = _createSuccess

    fun validateCreate(file: Uri?, title: String, description: String, context: Context) {

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
                insertDB(imageUrl,title,description){res->
                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                    if(res == "Success"){
                        _createSuccess.postValue(true)
                    }
                }
            }
        }

    }
    private fun insertDB(imageUrl: String, title: String, description: String, completion: (String?) -> Unit){
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
                ItemRepository.insertReview(review){res->
                    if(res !=  null){
                        completion(res)
                    }
                }
            }
        }
    }
    private fun insertImage(file: Uri?, completion: (String?) -> Unit){
        ItemRepository.insertImage(file){res->
            if(res != null){
                completion(res)
            }
        }
    }


}