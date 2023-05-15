package view_model

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
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
                oldUrl: String,
                context: Context) : String{
        if((file == null || file == Uri.EMPTY || file.path.isNullOrEmpty() ) ){
            if(selectedImage != null){
                return context.getString(R.string.success)
            }
            return context.getString(R.string.failed)
        }else{
            return context.getString(R.string.success)
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
        if(checkImage(file, selectedImage, oldUrl, context) == context.getString(R.string.success)){
            var msg: String? = when {
                title.isEmpty() -> context.getString(R.string.title_empty)
                description.isEmpty() -> context.getString(R.string.description_empty)
                else -> null
            }
            if (msg != null) {
                Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show()
                return
            }
            if(file == null){
                insertDB(oldUrl,title,description,category, currId){res->
                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
                    if(res == context.getString(R.string.success)){
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
                            if(res == context.getString(R.string.success)){
                                dialog.dismiss()
                                _updateSuccess.postValue(true)
                            }
                        }
                    }
                }
            }

        }else{
            Toast.makeText(context, context.getString(R.string.file_empty), Toast.LENGTH_SHORT).show()
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