package com.example.tpa_mobile

import Util.ActivityTemplate
import Util.ActivtyHelper
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
class MainActivity : AppCompatActivity(), ActivityTemplate {
    lateinit var emailField: EditText;
    lateinit var passwordField:EditText;
    lateinit var regisRedirect:TextView;
    lateinit var loginButton: Button;
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
        regisRedirect.paintFlags = Paint.UNDERLINE_TEXT_FLAG
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

            }
        }
        regisRedirect.setOnClickListener{
            finish()
            ActivtyHelper.changeToRegis(this)
        }
    }
}