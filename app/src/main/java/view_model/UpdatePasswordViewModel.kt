package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.AuthenticationRepository

class UpdatePasswordViewModel : ViewModel() {
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult
    fun validateUpdatePassword(oldPass:String, newPass:String, context:Context){
        var msg:String? = when {
            oldPass.isEmpty() -> "Old Password can't be empty"
            newPass.isEmpty() -> "New Password can't be empty"
            newPass.length < 6 -> "New Password must be at least 6 characters"
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
                Toast.makeText(context, "Incorret Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun updatePass(newPass: String,context: Context){
        AuthenticationRepository.updatePassword(newPass){ res->
            Toast.makeText(context,res,Toast.LENGTH_SHORT).show()
        }
    }
}