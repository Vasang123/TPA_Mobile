package view_model


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
import repository.AuthenticationRepository

class EmailRegisterViewModel : ViewModel(){
    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> = _signUpSuccess
    fun validateRegis(username:String,email:String,password:String,confirm:String,context: Context){
        var msg: String? = when {
            username.isEmpty() -> context.getString(R.string.username_empty)
            email.isEmpty() -> context.getString(R.string.email_empty)
            password.isEmpty() -> context.getString(R.string.password_empty)
            confirm.isEmpty() -> context.getString(R.string.confirm_empty)
            confirm.toString() != password.toString() -> context.getString(R.string.password_not_match)
            else -> null
        }
        if(msg != null){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }
        AuthenticationRepository.createAccount(
            username,
            email,
            password,
            context
        ) { result ->
            msg = result ?: ""
            if(msg == context.getString(R.string.success)){
                _signUpSuccess.postValue(true)
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

    }
}