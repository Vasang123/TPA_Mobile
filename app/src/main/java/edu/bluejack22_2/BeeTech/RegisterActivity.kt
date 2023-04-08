package edu.bluejack22_2.BeeTech

import Controller.FirebaseController
import Util.ActivityTemplate
import Util.ActivtiyHelper
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import edu.bluejack22_2.BeeTech.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider

class RegisterActivity : AppCompatActivity(),ActivityTemplate {
    lateinit var loginRedirect:TextView
    lateinit var usernameField:EditText
    lateinit var emailField:EditText
    lateinit var passField:EditText
    lateinit var confirmField:EditText
    lateinit var regisButton:Button
    lateinit var regisGoogle:Button
    lateinit var gso:GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    lateinit var signInLauncher : ActivityResultLauncher<Intent>
    var RC_SIGN_IN = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
        onAction()
    }

    override fun init() {
        usernameField = findViewById(R.id.usernameField)
        emailField = findViewById(R.id.emailRegisField)
        passField = findViewById(R.id.passwordRegisField)
        confirmField = findViewById(R.id.confirmPassword)
        loginRedirect = findViewById(R.id.loginRedirect)
        regisButton = findViewById(R.id.regisButton)
        regisGoogle = findViewById(R.id.googleSiginup)
        loginRedirect.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this,gso)
    }

    override fun onAction() {
        regisButton.setOnClickListener{
            validateRegis()
        }
        loginRedirect.setOnClickListener{
            finish()
            ActivtiyHelper.changePage(this, MainActivity::class.java)
        }
        regisGoogle.setOnClickListener{
            signUpGoogle()
        }
    }
    fun signUpGoogle(){
        val signInIntent:Intent =  gsc.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            var task:Task<GoogleSignInAccount>  = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken!!
                FirebaseController.firebaseWithGoogleAuth(idToken,this, this)

            }catch (e:ApiException){
                Toast.makeText(this,e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun validateRegis(){
        var msg:String = ""
        var check = 0
        if(usernameField.text.isEmpty()){
            msg = "Username can't be empty"
        }else if(emailField.text.isEmpty()){
            msg = "Email can't be empty"
        }else if(passField.text.isEmpty()){
            msg = "Password can't be empty"
        }else if(confirmField.text.isEmpty()){
            msg = "Confirm Password can't be empty"
        }else if(!confirmField.text.toString().equals(passField.text.toString())){
            msg = "Password doesn't match"
        }else{
            check = 1
            FirebaseController.createAccount(
                usernameField.text.toString(),
                emailField.text.toString(),
                passField.text.toString()
            ) { result ->
                // Handle the result of the createAccount() function here
                msg = result ?: ""
                if(msg.equals("Success")){
                    finish()
                    ActivtiyHelper.changePage(this, MainActivity::class.java)
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }

        }
        if(check == 0){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}