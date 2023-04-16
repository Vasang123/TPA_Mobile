package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.UserRepository

class UpdateUsernameViewModel : ViewModel() {
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult

    fun validateUpdateUser(userId: String, username:String, email:String,field: String, context:Context){
        var msg:String? = when {
            username.isEmpty() -> "Username can't be empty"
            else -> null
        }
        if(msg != null){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }
        UserRepository.updateUser(userId,username,email,field){result ->
            msg = result ?: ""
            if(msg.equals("Success")){
                _updateResult.postValue(true)
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}