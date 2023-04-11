package viewmodel


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import edu.bluejack22_2.BeeTech.R
import repository.AuthenticationRepository

class GoogleLoginViewModel: ViewModel() {
    private val _signInSuccess = MutableLiveData<Boolean>()
    val signInSuccess: LiveData<Boolean> = _signInSuccess
    lateinit var gso:GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    var RC_SIGN_IN = 2

    fun init(context: Context) {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(context,gso)
    }
    fun signInGoogle(activity: Activity){
        val signInIntent:Intent =  gsc.signInIntent
        activity.startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    fun handleGoogleSignInResult(requestCode: Int, resultCode: Int, data: Intent?,context: Context) {
        if(requestCode == RC_SIGN_IN){
            var task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken!!
                AuthenticationRepository.firebaseWithGoogleAuth(idToken,context, context as Activity)
            }catch (e: ApiException){
                Toast.makeText(context,e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}