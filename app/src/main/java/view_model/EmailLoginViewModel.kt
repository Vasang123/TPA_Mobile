package view_model


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.AuthenticationRepository

class EmailLoginViewModel : ViewModel(){
    private val _signInSuccess = MutableLiveData<Boolean>()
    val signInSuccess: LiveData<Boolean> = _signInSuccess
    fun validateEmailLogin(email:String,password:String, context: Context){
        var msg : String? = when {
            email.isEmpty() -> "Email Can't be empty"
            password.isEmpty() -> "Password Can't be empty"
            else -> null
        }
        if(msg != null){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }
        AuthenticationRepository.login(email,password){ result ->
            var msg = result ?: ""
            if(msg.toString() == "Success"){
                _signInSuccess.postValue(true)
            } else {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

}