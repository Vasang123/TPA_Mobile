package edu.bluejack22_2.BeeTech

import util.ActivityTemplate
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import edu.bluejack22_2.BeeTech.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ActivityTemplate {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        onAction()
        setContentView(binding.root)
    }

    override fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        replaceFragment(HomeFragment())
    }

    override fun onAction() {
        binding.bottomNavigationView.setOnItemSelectedListener{item ->
            when(item.itemId){
                R.id.home_btn -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.search_btn -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.add_btn -> {
                    replaceFragment(CreateFragment())
                    true
                }
                R.id.list_btn -> {
                    replaceFragment(ListFragment())
                    true
                }
                R.id.profile_btn -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }
    fun replaceFragment(fragment:Fragment){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}