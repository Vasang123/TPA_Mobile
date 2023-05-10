package edu.bluejack22_2.BeeTech

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack22_2.BeeTech.databinding.CategoryListBinding
import edu.bluejack22_2.BeeTech.databinding.UserListBinding

class CategoryListActivity : AppCompatActivity() {

    lateinit var binding : CategoryListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)
    }
}