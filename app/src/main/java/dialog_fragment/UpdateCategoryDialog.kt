package dialog_fragment

import android.app.Dialog
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
import view_model.CommentViewModel

class UpdateCategoryDialog: BaseDialogFragment() {
    interface UpdateCategoryListener {
        fun onCategoryUpdate(content: String)
    }
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var email:String
    lateinit var updateCategoryListener: UpdateCategoryListener
    lateinit var userId:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val categoryId = arguments?.getString("categoryId").toString()
        val view = inflater.inflate(R.layout.popup_update_category, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelCategoryButton)
        val updateButton = view.findViewById<Button>(R.id.changeCategoryButton)
        val contentField = view.findViewById<EditText>(R.id.updateCategoryField)
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        if(categoryId != null){
            categoryViewModel.getCategoryById(categoryId)
        }
        categoryViewModel.category.observe(this, Observer { category->
            val content = category.name.toString()
            val editableContent = Editable.Factory.getInstance().newEditable(content)
            contentField.text = editableContent
        })
        updateButton.setOnClickListener {
            updateCategoryListener.onCategoryUpdate(contentField.text.toString())
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
            updateCategoryListener = context as UpdateCategoryListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ChangeCategoryDialog")
        }
    }
    companion object {
        fun newInstance(commentId: String): UpdateCategoryDialog {
            val args = Bundle().apply {
                putString("categoryId", commentId)
            }
            return UpdateCategoryDialog().apply {
                arguments = args
            }
        }
    }
}