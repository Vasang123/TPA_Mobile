package viewmodel


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.AuthenticationRepository
import repository.UserRepository

class EmailRegisterViewModel : ViewModel(){
    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> = _signUpSuccess
    fun validateRegis(username:String,email:String,password:String,confirm:String,context: Context){
        var msg:String = ""
        var check = 0
        if(username.isEmpty()){
            msg = "Username can't be empty"
        }else if(email.isEmpty()){
            msg = "Email can't be empty"
        }else if(password.isEmpty()){
            msg = "Password can't be empty"
        }else if(confirm.isEmpty()){
            msg = "Confirm Password can't be empty"
        }else if(confirm.toString() != password.toString()){
            msg = "Password doesn't match"
        }else{
            check = 1
            AuthenticationRepository.createAccount(
                username,
                email,
                password
            ) { result ->
                msg = result ?: ""
                if(msg.equals("Success")){
                    _signUpSuccess.postValue(true)
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
        if(check == 0){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}