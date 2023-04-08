package edu.bluejack22_2.BeeTech

import Util.ActivityTemplate
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import edu.bluejack22_2.BeeTech.R

class HomeActivity : AppCompatActivity(), ActivityTemplate {
    lateinit var logout:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun init() {
        logout = findViewById(R.id.logout_button)
    }

    override fun onAction() {
        logout.setOnClickListener{

        }
    }
}