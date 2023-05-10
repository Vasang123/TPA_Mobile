package dialog_fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bluejack22_2.BeeTech.R
import view_model.CategoryViewModel

class CreateCategoryDialog: BaseDialogFragment() {
    interface CreateCategoryListener {
        fun onCategoryInsert(content: String)
    }
    lateinit var content:String
    lateinit var createCategoryListener: CreateCategoryListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val categoryId = arguments?.getString("categoryId").toString()
        val view = inflater.inflate(R.layout.popup_create_category, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelCategoryButton)
        val insertButton = view.findViewById<Button>(R.id.insertCategoryButton)
        val contentField = view.findViewById<EditText>(R.id.createCategoryField)
        insertButton.setOnClickListener {
            createCategoryListener.onCategoryInsert(contentField.text.toString())
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
            createCategoryListener = context as CreateCategoryDialog.CreateCategoryListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement CreateCategoryDialog")
        }
    }

}