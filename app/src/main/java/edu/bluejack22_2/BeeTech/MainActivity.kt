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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity(), ActivityTemplate {
    lateinit var emailField: EditText
    lateinit var passwordField:EditText
    lateinit var regisRedirect:TextView
    lateinit var forgotPassword:TextView
    lateinit var loginButton: Button
    lateinit var gso:GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    lateinit var loginGoogle:Button
    lateinit var signInLauncher : ActivityResultLauncher<Intent>
    val PERMISSIONS_REQUEST_INTERNET = 100
    val INTERNET = android.Manifest.permission.INTERNET
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        onAction()
    }

    override fun init() {
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        loginButton = findViewById(R.id.loginButton)
        regisRedirect = findViewById(R.id.regisRedirect)
        forgotPassword = findViewById(R.id.forgotPassword)
        loginGoogle = findViewById(R.id.googleSiginin)
        regisRedirect.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        forgotPassword.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this,gso)
        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                // Result is OK
                val data: Intent? = result.data
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                // Handle the intent data here
            } else {
                val error = GoogleSignIn.getSignedInAccountFromIntent(result.data).exception
                Log.e("123",error.toString())
                Toast.makeText(this, "Failed: ${error?.localizedMessage}", Toast.LENGTH_SHORT).show()
                // Result is not OK
            }
        }
    }

    override fun onAction() {
        loginButton.setOnClickListener{
            val email = emailField.text.trim().toString()
            val password = passwordField.text.trim().toString()
            if (email.isEmpty()){
                Toast.makeText(this, "Email Can't be empty", Toast.LENGTH_SHORT).show()
            }else if (password.isEmpty()){
                Toast.makeText(this, "Password Can't be empty", Toast.LENGTH_SHORT).show()
            }else{
                FirebaseController.login(email,password){ result ->
                    var msg = result ?: ""
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
        regisRedirect.setOnClickListener{
            finish()
            ActivtiyHelper.changePage(this, RegisterActivity::class.java)
        }
        loginGoogle.setOnClickListener{
            signInGoogle()
        }
    }
    fun signInGoogle(){
        val signInIntent:Intent =  gsc.signInIntent
        signInLauncher.launch(signInIntent)
    }
}