package edu.bluejack22_2.BeeTech


import android.content.Context
import util.ActivityTemplate
import util.ActivityHelper
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dialog_fragment.EmailResetDialog
import edu.bluejack22_2.BeeTech.databinding.ActivityLoginBinding
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
    lateinit var binding: ActivityLoginBinding
    override fun onStart() {
        super.onStart()
        val currentUser = AuthenticationRepository.auth.currentUser
        if (currentUser != null) {
            finish()
            ActivityHelper.changePage(this,MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        onAction()
        setContentView(binding.root)
    }

    override fun init() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        emailField = binding.emailField
        passwordField = binding.passwordField
        loginButton = binding.loginButton
        regisRedirect = binding.regisRedirect
        forgotPassword = binding.forgotPassword
        loginGoogle = binding.googleSiginin
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
            ActivityHelper.changePage(this, RegisterActivity::class.java)
        }
        loginGoogle.setOnClickListener{
            googleLoginViewModel.signInGoogle(this)
        }
        forgotPassword.setOnClickListener {
            showEmailPopup();
        }
        emailLoginViewModel.signInSuccess.observe(this, Observer { success ->
            if (success) {
                ActivityHelper.changePage(this, MainActivity::class.java);
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