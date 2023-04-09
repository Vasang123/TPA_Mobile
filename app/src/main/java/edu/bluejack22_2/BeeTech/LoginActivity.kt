package edu.bluejack22_2.BeeTech

import Controller.FirebaseController
import Util.ActivityTemplate
import Util.ActivtiyHelper
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity(), ActivityTemplate {
    lateinit var emailField: EditText
    lateinit var passwordField:EditText
    lateinit var regisRedirect:TextView
    lateinit var forgotPassword:TextView
    lateinit var loginButton: Button
    lateinit var gso:GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    lateinit var loginGoogle:Button
    lateinit var signInLauncher : ActivityResultLauncher<Intent>
    var RC_SIGN_IN = 2
    val PERMISSIONS_REQUEST_INTERNET = 100
    val INTERNET = android.Manifest.permission.INTERNET
    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseController.auth.currentUser
        if (currentUser != null) {
            finish()
            ActivtiyHelper.changePage(this,MainActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
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
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this,gso)
        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data: Intent? = result.data
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                ActivtiyHelper.changePage(this,MainActivity::class.java)
                finish()

            } else {
                val error = GoogleSignIn.getSignedInAccountFromIntent(result.data).exception
                Log.e("123",error.toString())
                Toast.makeText(this, "Failed: ${error?.localizedMessage}", Toast.LENGTH_SHORT).show()
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
                    if(msg.toString() == "Success"){
                        ActivtiyHelper.changePage(this,MainActivity::class.java);
                        finish()
                    }
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
        forgotPassword.setOnClickListener {
            showEmailPopup();
        }

    }
    fun signInGoogle(){
        val signInIntent:Intent =  gsc.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            var task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken!!
                FirebaseController.firebaseWithGoogleAuth(idToken,this, this)

            }catch (e: ApiException){
                Toast.makeText(this,e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun showEmailPopup() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.popup_email, null)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val sendButton = dialogView.findViewById<Button>(R.id.resetButton)
        val emailResetField = dialogView.findViewById<EditText>(R.id.emailSendField)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
        val dialog = builder.show()
        sendButton.setOnClickListener {
            val email = emailResetField.text.toString()
            if (email.isNotEmpty()) {
                FirebaseController.resetPassword(email){ result ->
                    var msg = result ?: ""
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

}