package edu.bluejack22_2.BeeTech


import util.ActivityTemplate
import util.ActivtiyHelper
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dialog_fragment.EmailResetDialog
import repository.AuthenticationRepository
import view_model.ForgotPassViewModel
import view_model.EmailLoginViewModel
import view_model.GoogleLoginViewModel

class LoginActivity : AppCompatActivity(), ActivityTemplate,EmailResetDialog.EmailResetDialogListener {
    lateinit var emailField: EditText
    lateinit var passwordField:EditText
    lateinit var regisRedirect:TextView
    lateinit var forgotPassword:TextView
    lateinit var loginButton: Button
    lateinit var loginGoogle:Button
    lateinit var emailLoginViewModel: EmailLoginViewModel
    lateinit var googleLoginViewModel: GoogleLoginViewModel
    lateinit var forgotPassViewModel: ForgotPassViewModel
    override fun onStart() {
        super.onStart()
        val currentUser = AuthenticationRepository.auth.currentUser
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
        emailLoginViewModel = ViewModelProvider(this)[EmailLoginViewModel::class.java]
        googleLoginViewModel= ViewModelProvider(this)[GoogleLoginViewModel::class.java]
        forgotPassViewModel = ViewModelProvider(this)[ForgotPassViewModel::class.java]
        googleLoginViewModel.init(this)
    }

    override fun onAction() {
        loginButton.setOnClickListener{
            val email = emailField.text.trim().toString()
            val password = passwordField.text.trim().toString()
            emailLoginViewModel.validateEmailLogin(email, password, applicationContext)
        }
        regisRedirect.setOnClickListener{
            finish()
            ActivtiyHelper.changePage(this, RegisterActivity::class.java)
        }
        loginGoogle.setOnClickListener{
            googleLoginViewModel.signInGoogle(this)
        }
        forgotPassword.setOnClickListener {
            showEmailPopup();
        }
        emailLoginViewModel.signInSuccess.observe(this, Observer { success ->
            if (success) {
                ActivtiyHelper.changePage(this, MainActivity::class.java);
                finish()
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       googleLoginViewModel.handleGoogleSignInResult(requestCode,resultCode,data,this)
    }
    private fun showEmailPopup() {
        val emailResetDialog = EmailResetDialog()
        emailResetDialog.show(supportFragmentManager, "EmailResetDialogFragment")
    }
    override fun onEmailReset(email: String) {
        forgotPassViewModel.validateForgotPassword(email,applicationContext)
    }
}