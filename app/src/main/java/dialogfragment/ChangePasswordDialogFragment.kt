package dialogfragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bluejack22_2.BeeTech.R
import viewmodel.UserViewModel

class ChangePasswordDialogFragment: DialogFragment() {
    interface UpdatePasswordListener {
        fun onPasswordUpdate(oldPass:String, newPassword : String)
    }

    lateinit var email:String
    lateinit var updatePasswordListener: UpdatePasswordListener
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
            throw ClassCastException("$context must implement ChangePasswordDialogFragment")
        }
    }
}