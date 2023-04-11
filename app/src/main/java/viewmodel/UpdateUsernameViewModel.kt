package viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.UserRepository

class UpdateUsernameViewModel : ViewModel() {
    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult

    fun validateUpdateUser(username:String, email:String,field: String, context:Context){
        var msg:String = ""
        var check = 0
        if (username.isEmpty()){
            msg = "Username can't be empty"
        }else{
            check = 1
            UserRepository.updateUser(username,email,field){result ->
                msg = result ?: ""
                if(msg.equals("Success")){
                    _updateResult.postValue(true)
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
        if(check == 0){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}