package com.example.tpa_mobile

import Controller.FirebaseController
import Util.ActivityTemplate
import Util.ActivtyHelper
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.time.Duration

class RegisterActivity : AppCompatActivity(),ActivityTemplate {
    lateinit var loginRedirect:TextView
    lateinit var usernameField:EditText
    lateinit var emailField:EditText
    lateinit var passField:EditText
    lateinit var confirmField:EditText
    lateinit var regisButton:Button
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
        loginRedirect.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    override fun onAction() {
        regisButton.setOnClickListener{
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
                        ActivtyHelper.changeToLogin(this)
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }

            }
            if(check == 0){
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        }
        loginRedirect.setOnClickListener{
            finish()
            ActivtyHelper.changeToLogin(this)
        }
    }
}