package dialog_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import edu.bluejack22_2.BeeTech.R

class UpdateReviewDialog : BaseDialogFragment() {
    interface UpdateReviewDialogListener {
        fun onReviewUpdate()
    }

    lateinit var updateReviewDialogListener: UpdateReviewDialogListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.popup_update_review, container, false)
        val cancelButton = view.findViewById<ImageView>(R.id.closeUpdateReview)
        val updateButton = view.findViewById<Button>(R.id.updateReviewButton)
        val selectedImage = view.findViewById<ImageView>(R.id.updateSelectedImage)
        selectedImage.setImageResource(R.drawable.blank_image)
        val chooseImage = view.findViewById<Button>(R.id.updateChooseImage)
        val descField = view.findViewById<EditText>(R.id.updateDescField)
        val titleField = view.findViewById<EditText>(R.id.updateTitleField)
        updateButton.setOnClickListener {
            updateReviewDialogListener.onReviewUpdate()
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
            updateReviewDialogListener = context as UpdateReviewDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement UpdateReviewDialog")
        }
    }
}