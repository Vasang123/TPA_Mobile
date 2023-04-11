package edu.bluejack22_2.BeeTech

import util.ActivityTemplate
import util.ActivtiyHelper
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dialogfragment.ChangeUsernameDialogFragment
import dialogfragment.EmailResetDialogFragment
import repository.AuthenticationRepository
import repository.UserRepository
import viewmodel.UserViewModel


class ProfileFragment : Fragment(),ActivityTemplate {
    lateinit var logout: Button
    lateinit var currUsername:EditText
    lateinit var currEmail:EditText
    lateinit var changePassword:TextView
    lateinit var changeUsername:TextView
    lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onAction()
    }

    override fun init() {
        logout = requireView().findViewById(R.id.logout_button)
        changeUsername = requireView().findViewById(R.id.changeUsername)
        changePassword = requireView().findViewById(R.id.changePassword)
        currUsername = requireView().findViewById(R.id.currUsername)
        currEmail = requireView().findViewById(R.id.currEmail)
        currUsername.isEnabled = false
        currEmail.isEnabled = false
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

    }

    override fun onAction() {
        logout.setOnClickListener{
            AuthenticationRepository.signOut(requireActivity())
            requireActivity().finish()
            ActivtiyHelper.changePage(requireContext(),LoginActivity::class.java)
        }
        userViewModel.currentUser.observe(requireActivity(), Observer{user->
            val email = user.email.toString()
            val username = user.username.toString()
            val editableEmail = Editable.Factory.getInstance().newEditable(email)
            val editableUsername = Editable.Factory.getInstance().newEditable(username)
            currEmail.text = editableEmail
            currUsername.text = editableUsername
        })
        if(AuthenticationRepository.checkSignInMethod()){
            changePassword.visibility = View.GONE
        }
        changeUsername.setOnClickListener{
            (activity as MainActivity).showChangeUsernamePopup()
        }
        changePassword.setOnClickListener{
            (activity as MainActivity).showChangePasswordPopup()
        }
    }
}