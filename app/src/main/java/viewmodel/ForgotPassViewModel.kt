package viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dialogfragment.EmailResetDialogFragment
import repository.AuthenticationRepository

class ForgotPassViewModel : ViewModel() {
    private val _sendSuccess = MutableLiveData<Boolean>()
    val sendSuccess: LiveData<Boolean> = _sendSuccess
    fun validateForgotPassword(email:String, context: Context) {
        if (email.isNotEmpty()) {
            AuthenticationRepository.resetPassword(email){ result ->
                var msg = result ?: ""
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                _sendSuccess.postValue(true)
            }
        } else {
            Toast.makeText(context, "Please enter an email", Toast.LENGTH_SHORT).show()
        }
    }
}