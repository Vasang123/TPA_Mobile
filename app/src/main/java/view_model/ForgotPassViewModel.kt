package view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.AuthenticationRepository

class ForgotPassViewModel : ViewModel() {
    private val _sendSuccess = MutableLiveData<Boolean>()
    val sendSuccess: LiveData<Boolean> = _sendSuccess
    fun validateForgotPassword(email:String, context: Context) {
        var msg : String? = when{
            email.isEmpty() -> "Please enter an email"
            else -> null
        }
        if (msg != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            return
        }
        AuthenticationRepository.resetPassword(email){ result ->
            msg = result ?: ""
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            _sendSuccess.postValue(true)
        }
    }
}