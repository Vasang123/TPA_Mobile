package view_model

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Category
import model.Review
import repository.ReviewRepository
import repository.UserRepository
import java.util.*

class UpdateReviewViewModel : ViewModel() {
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess
    fun checkImage(
                file: Uri?,
                selectedImage : ImageView,
                oldUrl: String) : String{
        if((file == null || file == Uri.EMPTY || file.path.isNullOrEmpty() ) ){
            if(selectedImage != null){
                return "Success"
            }
            return "Failed"
        }else{
            return "Success"
        }

    }
    fun validateUpdate(file: Uri?,
                       title: String,
                       description: String,
                       context: Context,
                       category : Category,
                       currId: String,
                       dialog: Dialog,
                       selectedImage : ImageView,
                       oldUrl: String) {
        if(checkImage(file, selectedImage, oldUrl) == "Success"){
            var msg: String? = when {
                title.isEmpty() -> "Title can't be empty"
                description.isEmpty() -> "Description can't be empty"
                else -> null
            }
            if (msg != null) {
                Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
                return
            }
            if(file == null){
                insertDB(oldUrl,title,description,category, currId){res->
                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                    if(res == "Success"){
                        dialog.dismiss()
                        _updateSuccess.postValue(true)
                    }
                }
            }else{
                var imageUrl: String = ""
                insertImage(file){ res->
                    if (res != null) {
                        imageUrl = res
                        insertDB(imageUrl,title,description,category, currId){res->
                            Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                            if(res == "Success"){
                                dialog.dismiss()
                                _updateSuccess.postValue(true)
                            }
                        }
                    }
                }
            }

        }else{
            Toast.makeText(context, "File can't be empty", Toast.LENGTH_SHORT).show()
        }
    }
    private fun insertDB(imageUrl: String, title: String, description: String, category : Category, currId: String, completion: (String?) -> Unit){
        var review =  Review()
        review.updatedAt= Date()
        review.id= currId
        review.imageURL= imageUrl
        review.title= title
        review.description= description
        review.categoryId = category.id
        review.categoryName = category.name
        ReviewRepository.updateReview(review){ res->
            if(res !=  null){
                completion(res)
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