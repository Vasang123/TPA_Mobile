package dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import edu.bluejack22_2.BeeTech.R

class EmailResetDialogFragment : DialogFragment(){
    interface EmailResetDialogListener {
        fun onEmailReset(email: String)
    }

    lateinit var emailResetDialogListener: EmailResetDialogListener
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_email, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val sendButton = view.findViewById<Button>(R.id.resetButton)
        val emailResetField = view.findViewById<EditText>(R.id.emailSendField)
        sendButton.setOnClickListener {
            emailResetDialogListener.onEmailReset(emailResetField.text.toString())
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
            emailResetDialogListener = context as EmailResetDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement EmailResetDialogListener")
        }
    }
}