package edu.bluejack22_2.BeeTech


import util.ActivityTemplate
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dialog_fragment.ChangePasswordDialog
import dialog_fragment.ChangeUsernameDialog
import dialog_fragment.UpdateReviewDialog
import edu.bluejack22_2.BeeTech.databinding.ActivityMainBinding
import navigation_strategy.NavigationMap
import navigation_strategy.SearchStrategy
import navigation_strategy.Strategy
import util.FragmentHelper
import view_model.UpdatePasswordViewModel
import view_model.UpdateUsernameViewModel
import view_model.UserViewModel

class MainActivity : AppCompatActivity(),
    ActivityTemplate,
    ChangeUsernameDialog.UpdateUserListener,
    ChangePasswordDialog.UpdatePasswordListener,
    UpdateReviewDialog.UpdateReviewDialogListener,
    HomeFragment.OnSearchQueryListener{
    lateinit var binding:ActivityMainBinding
    lateinit var userViewModel: UserViewModel
    lateinit var updateUsernameViewModel: UpdateUsernameViewModel
    lateinit var updatePasswordViewModel: UpdatePasswordViewModel
    lateinit var searchQuery: String
    lateinit var strategy : Strategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        onAction()
        setContentView(binding.root)
    }
    override fun init() {
        searchQuery = ""
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        updateUsernameViewModel = ViewModelProvider(this)[UpdateUsernameViewModel::class.java]
        updatePasswordViewModel = ViewModelProvider(this)[UpdatePasswordViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        lateinit var fragment: Fragment
        userViewModel.currentUser.observe(this, Observer {user->
            if(user.role == "admin"){
                fragment = AdminHomeFragment()
            }else if(user.role == "member"){
                fragment = HomeFragment()
                (fragment as HomeFragment).setOnSearchQueryListener(this)
            }
            FragmentHelper.replaceFragment(fragment, supportFragmentManager)
        })

    }
    private fun navigateToScreen(itemId: Int ) {
        userViewModel.currentUser.observe(this, Observer {user->
            if(user.role == "admin"){
                strategy = NavigationMap.adminMap[itemId]!!
            }else if(user.role == "member"){
                strategy = NavigationMap.userMap[itemId]!!
            }
        })
        if (itemId == R.id.search_btn && !searchQuery.isNullOrEmpty()) {
            val searchStrategy = SearchStrategy()
            searchStrategy.navigateWithQuery(supportFragmentManager, searchQuery)
        } else {
            strategy?.navigate(supportFragmentManager)
        }
    }
    override fun onAction() {
        binding.bottomNavigationView.setOnItemSelectedListener{item ->
            navigateToScreen(item.itemId)
            true
        }
        updateUsernameViewModel.updateResult.observe(this, Observer { result->
            if (result) {
                FragmentHelper.replaceFragment(ProfileFragment(),supportFragmentManager)
            }
        })

    }
    fun showUpdateReview(){
        val updateReviewDialog = UpdateReviewDialog()
        updateReviewDialog.show(supportFragmentManager, "ChangeReviewDialogFragment")
    }
    fun showChangeUsernamePopup() {
        val updateUserDialog = ChangeUsernameDialog()
        updateUserDialog.show(supportFragmentManager, "ChangeUsernameDialogFragment")
    }
    fun showChangePasswordPopup(){
        val updateUserPassDialog = ChangePasswordDialog()
        updateUserPassDialog.show(supportFragmentManager, "ChangePasswordDialogFragment")
    }
    override fun onUserUpdate(userId: String, username: String, email:String) {
        updateUsernameViewModel.validateUpdateUser(userId, username,email,"username",this)
    }

    override fun onPasswordUpdate(oldPassword:String,newPassword: String) {
        updatePasswordViewModel.validateUpdatePassword(oldPassword,newPassword,this)
    }

    override fun onSearchQuery(query: String) {
        searchQuery = query
        binding.bottomNavigationView.post {
            binding.bottomNavigationView.selectedItemId = R.id.search_btn
        }

    }
    override fun onReviewUpdate() {
        TODO("Not yet implemented")
    }
}