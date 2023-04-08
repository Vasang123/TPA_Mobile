package edu.bluejack22_2.BeeTech

import Controller.FirebaseController
import Util.ActivityTemplate
import Util.ActivtiyHelper
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import edu.bluejack22_2.BeeTech.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

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
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this,gso)
        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                FirebaseController.createAccountWIthGoogle(this)
            } else {
                val error = GoogleSignIn.getSignedInAccountFromIntent(result.data).exception
                Toast.makeText(this, "Failed: ${error?.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
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
        signInLauncher.launch(signInIntent)
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