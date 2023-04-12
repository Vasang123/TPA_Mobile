package view_model


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.AuthenticationRepository

class EmailRegisterViewModel : ViewModel(){
    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> = _signUpSuccess
    fun validateRegis(username:String,email:String,password:String,confirm:String,context: Context){
        var msg: String? = when {
            username.isEmpty() -> "Username can't be empty"
            email.isEmpty() -> "Email can't be empty"
            password.isEmpty() -> "Password can't be empty"
            confirm.isEmpty() -> "Confirm Password can't be empty"
            confirm.toString() != password.toString() -> "Password doesn't match"
            else -> null
        }
        if(msg != null){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }
        AuthenticationRepository.createAccount(
            username,
            email,
            password
        ) { result ->
            msg = result ?: ""
            if(msg == "Success"){
                _signUpSuccess.postValue(true)
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

    }
}