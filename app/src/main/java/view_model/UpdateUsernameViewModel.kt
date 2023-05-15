package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
import repository.UserRepository

class UpdateUsernameViewModel : ViewModel() {
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult

    fun validateUpdateUser(userId: String, username:String, email:String,field: String, context:Context){
        var msg:String? = when {
            username.isEmpty() -> context.getString(R.string.username_empty)
            else -> null
        }
        if(msg != null){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }
        UserRepository.updateUser(userId,username,email,field,context){result ->
            msg = result ?: ""
            if(msg.equals(context.getString(R.string.success))){
                _updateResult.postValue(true)
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}