package view_model


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack22_2.BeeTech.R
import repository.AuthenticationRepository

class EmailLoginViewModel : ViewModel(){
    private val _signInSuccess = MutableLiveData<Boolean>()
    val signInSuccess: LiveData<Boolean> = _signInSuccess
    fun validateEmailLogin(email:String,password:String, context: Context){
        var msg : String? = when {
            email.isEmpty() -> ""
            password.isEmpty() -> context.getString(R.string.email_empty)
            else -> null
        }
        if(msg != null){
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }
        AuthenticationRepository.login(email,password,context){ result ->
            var msg = result ?: ""
            if(msg.toString() == context.getString(R.string.success)){
                _signInSuccess.postValue(true)
            } else {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

}