package edu.bluejack22_2.BeeTech

import util.ActivityTemplate
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dialog_fragment.ChangePasswordDialog
import dialog_fragment.ChangeUsernameDialog
import edu.bluejack22_2.BeeTech.databinding.ActivityMainBinding
import navigation_strategy.NavigationMap
import util.FragmentHelper
import view_model.UpdatePasswordViewModel
import view_model.UpdateUsernameViewModel
import view_model.UserViewModel

class MainActivity : AppCompatActivity(),
    ActivityTemplate,
    ChangeUsernameDialog.UpdateUserListener,
    ChangePasswordDialog.UpdatePasswordListener {
    lateinit var binding:ActivityMainBinding
    lateinit var userViewModel: UserViewModel
    lateinit var updateUsernameViewModel: UpdateUsernameViewModel
    lateinit var updatePasswordViewModel: UpdatePasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        onAction()
        setContentView(binding.root)
    }

    override fun init() {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        updateUsernameViewModel = ViewModelProvider(this)[UpdateUsernameViewModel::class.java]
        updatePasswordViewModel = ViewModelProvider(this)[UpdatePasswordViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        FragmentHelper.replaceFragment(HomeFragment(),supportFragmentManager)
    }

    override fun onAction() {
        binding.bottomNavigationView.setOnItemSelectedListener{item ->
            val strategy = NavigationMap.map[item.itemId]
            strategy?.navigate(supportFragmentManager)
            true
        }
        updateUsernameViewModel.updateResult.observe(this, Observer { result->
            if (result) {
                FragmentHelper.replaceFragment(ProfileFragment(),supportFragmentManager)
            }
        })
    }
    fun showChangeUsernamePopup() {
        val updateUserDialog = ChangeUsernameDialog()
        updateUserDialog.show(supportFragmentManager, "ChangeUsernameDialogFragment")
    }
    fun showChangePasswordPopup(){
        val updateUserPassDialog = ChangePasswordDialog()
        updateUserPassDialog.show(supportFragmentManager, "ChangePasswordDialogFragment")
    }
    override fun onUserUpdate(username: String, email:String) {
        updateUsernameViewModel.validateUpdateUser(username,email,"username",this)
    }

    override fun onPasswordUpdate(oldPassword:String,newPassword: String) {
        updatePasswordViewModel.validateUpdatePassword(oldPassword,newPassword,this)
    }
}