package edu.bluejack22_2.BeeTech

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import edu.bluejack22_2.BeeTech.databinding.FragmentAdminHomeBinding
import util.ActivityHelper
import view_model.UserViewModel

class AdminHomeFragment : Fragment() {

    lateinit var binding : FragmentAdminHomeBinding
    lateinit var userViewModel: UserViewModel

    lateinit var userBtn : Button
    lateinit var categoryBtn : Button
    lateinit var usernameTV : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.currentUser.observe(requireActivity()) { user ->
            init()
            usernameTV.text = "Welcome, " + user.username
            addOnClickListener()
        }
    }

    fun init(){
        usernameTV = binding.adminWelcome
        userBtn = binding.adminViewUserBtn
        categoryBtn = binding.adminViewCategoryBtn
    }

    fun addOnClickListener(){
        userBtn.setOnClickListener{
            ActivityHelper.changePage(requireContext(),UserListActivity::class.java)
        }
        categoryBtn.setOnClickListener{
            ActivityHelper.changePage(requireContext(),CategoryListActivity::class.java)
        }

    }




}