package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
import repository.AuthenticationRepository

class UpdatePasswordViewModel : ViewModel() {
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult
    fun validateUpdatePassword(oldPass:String, newPass:String, context:Context){
        var msg:String? = when {
            oldPass.isEmpty() -> context.getString(R.string.old_password_empty)
            newPass.isEmpty() -> context.getString(R.string.new_password_empty)
            newPass.length < 6 -> context.getString(R.string.new_password_must_be_6)
            else -> null
        }
        if(msg != null){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }
        checkOldPass(oldPass,newPass,context)
    }
    fun checkOldPass(oldPass: String,newPass: String,context: Context){
        AuthenticationRepository.checkUserPassword(oldPass){ res->
            if(res == true){
                updatePass(newPass,context)
            }else{
                Toast.makeText(context, context.getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun updatePass(newPass: String,context: Context){
        AuthenticationRepository.updatePassword(
            newPass,
            context
        ) { res->
            Toast.makeText(context,res,Toast.LENGTH_SHORT).show()
        }
    }
}