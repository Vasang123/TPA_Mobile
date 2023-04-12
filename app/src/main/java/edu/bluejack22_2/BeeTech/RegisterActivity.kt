package edu.bluejack22_2.BeeTech


import util.ActivityTemplate
import util.ActivityHelper
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import view_model.EmailRegisterViewModel
import view_model.GoogleLoginViewModel

class RegisterActivity : AppCompatActivity(),ActivityTemplate {
    lateinit var loginRedirect:TextView
    lateinit var usernameField:EditText
    lateinit var emailField:EditText
    lateinit var passField:EditText
    lateinit var confirmField:EditText
    lateinit var regisButton:Button
    lateinit var regisGoogle:Button
    lateinit var googleLoginViewModel: GoogleLoginViewModel
    lateinit var emailRegisterViewModel: EmailRegisterViewModel
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
        googleLoginViewModel = ViewModelProvider(this)[GoogleLoginViewModel::class.java]
        emailRegisterViewModel = ViewModelProvider(this)[EmailRegisterViewModel::class.java]
        googleLoginViewModel.init(this)
    }

    override fun onAction() {
        regisButton.setOnClickListener{
            var username = usernameField.text.toString()
            var email = emailField.text.toString()
            var password = passField.text.toString()
            var confirm = confirmField.text.toString()
            emailRegisterViewModel.validateRegis(username,email,password,confirm,this)
        }
        emailRegisterViewModel.signUpSuccess.observe(this, Observer {success->
            if(success){
                finish()
                ActivityHelper.changePage(this, LoginActivity::class.java)
            }
        })
        loginRedirect.setOnClickListener{
            finish()
            ActivityHelper.changePage(this, LoginActivity::class.java)
        }
        regisGoogle.setOnClickListener{
            googleLoginViewModel.signInGoogle(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleLoginViewModel.handleGoogleSignInResult(requestCode,resultCode,data,this)
    }
}