package edu.bluejack22_2.BeeTech

import util.ActivityTemplate
import util.ActivtiyHelper
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import repository.AuthenticationRepository


class ProfileFragment : Fragment(),ActivityTemplate {
    lateinit var logout: Button

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
    }

    override fun onAction() {
        logout.setOnClickListener{
            AuthenticationRepository.signOut()
            requireActivity().finish()
            ActivtiyHelper.changePage(requireContext(),LoginActivity::class.java)
        }
    }

}