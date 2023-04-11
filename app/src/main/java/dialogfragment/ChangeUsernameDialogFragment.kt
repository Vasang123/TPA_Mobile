package dialogfragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bluejack22_2.BeeTech.R
import viewmodel.UserViewModel

class ChangeUsernameDialogFragment: DialogFragment() {
    interface UpdateUserListener {
        fun onUserUpdate(username: String, email:String)
    }
    lateinit var userViewModel: UserViewModel
    lateinit var email:String
    lateinit var updateUserListener: UpdateUserListener
    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        dialog?.setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_change_username, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val updateButton = view.findViewById<Button>(R.id.changeUsernameButton)
        val usernameField = view.findViewById<EditText>(R.id.updateUsernameField)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.currentUser.observe(this, Observer{user->
            val username = user.username.toString()
            email = user.email
            val editableUsername = Editable.Factory.getInstance().newEditable(username)
            usernameField.text = editableUsername
        })
        updateButton.setOnClickListener {
            updateUserListener.onUserUpdate(usernameField.text.toString(),email )
            dismiss()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {

            updateUserListener = context as UpdateUserListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ChangeUsernameDialogFragment")
        }
    }
}