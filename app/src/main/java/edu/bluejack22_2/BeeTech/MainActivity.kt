package edu.bluejack22_2.BeeTech

import Controller.FirebaseController
import Util.ActivityTemplate
import Util.ActivtiyHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity(), ActivityTemplate {
    lateinit var logout:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        onAction()
    }

    override fun init() {
        logout = findViewById(R.id.logout_button)
    }

    override fun onAction() {
        logout.setOnClickListener{
            FirebaseController.signOut()
            finish()
            ActivtiyHelper.changePage(this,LoginActivity::class.java)

        }
    }
}