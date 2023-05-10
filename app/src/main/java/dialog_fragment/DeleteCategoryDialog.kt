package dialog_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import edu.bluejack22_2.BeeTech.R

class DeleteCategoryDialog : BaseDialogFragment() {
    interface DeleteCategoryDialogListener {
        fun onCategoryDelete()
    }

    lateinit var deleteDialogListener: DeleteCategoryDialogListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_delete_confirmation, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelConfirmationButton)
        val accButton = view.findViewById<Button>(R.id.deleteConfirmationButton)
        accButton.setOnClickListener {
            deleteDialogListener.onCategoryDelete()
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
            deleteDialogListener = context as DeleteCategoryDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Error Confirmation: " + e.toString())
        }
    }
}