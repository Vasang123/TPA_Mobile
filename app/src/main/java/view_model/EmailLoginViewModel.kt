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
        if (email.isEmpty()){
            Toast.makeText(context, "Email Can't be empty", Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Toast.makeText(context, "Password Can't be empty", Toast.LENGTH_SHORT).show()
        }else{
            AuthenticationRepository.login(email,password){ result ->
                var msg = result ?: ""
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                if(msg.toString() == "Success"){
                    _signInSuccess.postValue(true)
                }
            }
        }
    }

}