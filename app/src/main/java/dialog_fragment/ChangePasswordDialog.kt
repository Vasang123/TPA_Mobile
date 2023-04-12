package dialog_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import edu.bluejack22_2.BeeTech.R

class ChangePasswordDialog: BaseDialogFragment() {
    interface UpdatePasswordListener {
        fun onPasswordUpdate(oldPass:String, newPassword : String)
    }

    lateinit var email:String
    lateinit var updatePasswordListener: UpdatePasswordListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_change_password, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val button = view.findViewById<Button>(R.id.changePasswordButton)
        val oldPassword = view.findViewById<EditText>(R.id.oldPassword)
        val newPassword = view.findViewById<EditText>(R.id.newPassword)
        button.setOnClickListener {
            updatePasswordListener.onPasswordUpdate(oldPassword.text.toString(), newPassword.text.toString())

        }
        cancelButton.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            updatePasswordListener = context as UpdatePasswordListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ChangePasswordDialog")
        }
    }
}