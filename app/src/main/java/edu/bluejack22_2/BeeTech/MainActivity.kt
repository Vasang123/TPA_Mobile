package edu.bluejack22_2.BeeTech


import android.Manifest
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import util.ActivityTemplate
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dialog_fragment.ChangePasswordDialog
import dialog_fragment.ChangeUsernameDialog
import dialog_fragment.DeleteReviewDialog
import dialog_fragment.UpdateReviewDialog
import edu.bluejack22_2.BeeTech.databinding.ActivityMainBinding
import model.Category
import navigation_strategy.NavigationMap
import navigation_strategy.SearchStrategy
import navigation_strategy.Strategy
import repository.AuthenticationRepository
import repository.UserRepository
import util.ActivityHelper
import util.FragmentHelper
import view_model.*

class MainActivity : AppCompatActivity(),
    ActivityTemplate,
    ChangeUsernameDialog.UpdateUserListener,
    ChangePasswordDialog.UpdatePasswordListener,
    UpdateReviewDialog.UpdateReviewDialogListener,
    DeleteReviewDialog.DeleteReviewDialogListener,
    HomeFragment.OnSearchQueryListener{
    lateinit var binding:ActivityMainBinding
    lateinit var userViewModel: UserViewModel
    lateinit var updateUsernameViewModel: UpdateUsernameViewModel
    lateinit var updatePasswordViewModel: UpdatePasswordViewModel
    lateinit var deleteReviewViewModel: DeleteReviewViewModel
    lateinit var updateReviewViewModel: UpdateReviewViewModel
    lateinit var searchQuery: String
    lateinit var strategy : Strategy
    lateinit var itemId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        onAction()
        setContentView(binding.root)
        createNotificationChannel()
    }
    override fun init() {
        searchQuery = ""
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        updateUsernameViewModel = ViewModelProvider(this)[UpdateUsernameViewModel::class.java]
        updatePasswordViewModel = ViewModelProvider(this)[UpdatePasswordViewModel::class.java]
        deleteReviewViewModel = ViewModelProvider(this)[DeleteReviewViewModel::class.java]
        updateReviewViewModel = ViewModelProvider(this)[UpdateReviewViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        lateinit var fragment: Fragment
        userViewModel.currentUser.observe(this, Observer {user->
            if(user.status =="banned"){
                finish()
                AuthenticationRepository.signOut(this)
                ActivityHelper.changePage(this, LoginActivity::class.java)
                Toast.makeText(this,"Your Account Is Banned!", Toast.LENGTH_LONG).show()
            }
            if(user.role == "admin"){
                fragment = AdminHomeFragment()
            }else if(user.role == "member"){
                fragment = HomeFragment()
                (fragment as HomeFragment).setOnSearchQueryListener(this)
            }
            FragmentHelper.replaceFragment(fragment, supportFragmentManager)
        })

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "usernameSuccess"
            val descriptionText = "usernameSuccessDesc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("usernameSuccessID", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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

    private fun sendNotification(message: String) {
        val notificationBuilder = NotificationCompat.Builder(this, "usernameSuccessID")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, notificationBuilder.build())
    }


    override fun onAction() {
        binding.bottomNavigationView.setOnItemSelectedListener{item ->
            navigateToScreen(item.itemId)
            true
        }
        updateUsernameViewModel.updateResult.observe(this, Observer { result->
            if (result) {
                FragmentHelper.replaceFragment(ProfileFragment(),supportFragmentManager)
                sendNotification("Sucessfully Changed Username")
            }
        })
        deleteReviewViewModel.success.observe(this, Observer { res->
            FragmentHelper.replaceFragment(ListFragment(),supportFragmentManager)
        })
        updateReviewViewModel.updateSuccess.observe(this, Observer { res->
            if(res){
                FragmentHelper.replaceFragment(ListFragment(),supportFragmentManager)
            }
        })
    }
    fun showUpdateReview(itemId:String){
        val updateReviewDialog = UpdateReviewDialog.newInstance(itemId)
        updateReviewDialog.show(supportFragmentManager, "UpdateReviewDialog")
    }
    fun showDeleteReviewConfirmation(reviewId: String){
        val deleteReviewDialog = DeleteReviewDialog()
        deleteReviewDialog.show(supportFragmentManager, "DeleteDialogFragment")
        itemId = reviewId
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


    override fun onReviewDelete() {
        deleteReviewViewModel.deleteReview(this, itemId)
    }

    override fun onReviewUpdate(
        file: Uri?,
        title: String,
        description: String,
        context: Context,
        category: Category,
        currId : String,
        dialog: Dialog,
        selectedImage : ImageView,
        imageUrl : String
    ) {
        updateReviewViewModel.validateUpdate(
            file,
            title,
            description,
            context,
            category,
            currId,
            dialog,
            selectedImage,
            imageUrl)
    }
}