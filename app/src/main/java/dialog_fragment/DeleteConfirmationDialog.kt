package dialog_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import edu.bluejack22_2.BeeTech.R

class DeleteConfirmationDialog : BaseDialogFragment() {
    interface DeleteDialogListener {
        fun onDelete()
    }

    lateinit var deleteDialogListener: DeleteDialogListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_delete_confirmation, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelConfirmationButton)
        val accButton = view.findViewById<Button>(R.id.deleteConfirmationButton)
        accButton.setOnClickListener {
            deleteDialogListener.onDelete()
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
            deleteDialogListener = context as DeleteDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DeleteDialog")
        }
    }
}