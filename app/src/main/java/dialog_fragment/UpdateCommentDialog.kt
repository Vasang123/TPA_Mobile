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
import view_model.CommentViewModel
import view_model.DeleteCommentViewModel
import view_model.UserViewModel

class UpdateCommentDialog: BaseDialogFragment() {
    interface UpdateCommentListener {
        fun onCommentUpdate(dialog: Dialog, content: String)
    }
    lateinit var commentViewModel: CommentViewModel
    lateinit var email:String
    lateinit var updateUserListener: UpdateCommentListener
    lateinit var userId:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val commentId = arguments?.getString("commentId").toString()
        val view = inflater.inflate(R.layout.popup_update_comment, container, false)
        val cancelButton = view.findViewById<Button>(R.id.cancelUpdateCommentButton)
        val updateButton = view.findViewById<Button>(R.id.updateCommentButton)
        val contentField = view.findViewById<EditText>(R.id.updateCommentField)
        commentViewModel = ViewModelProvider(this)[CommentViewModel::class.java]
        if(commentId != null){
            commentViewModel.getCommentById(commentId)
        }
        commentViewModel.comment.observe(this, Observer { comment->
            val content = comment.content.toString()
            val editableContent = Editable.Factory.getInstance().newEditable(content)
            contentField.text = editableContent
        })
        updateButton.setOnClickListener {
            dialog?.let { it1 -> updateUserListener.onCommentUpdate(it1, contentField.text.toString()) }
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            updateUserListener = context as UpdateCommentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ChangeUsernameDialog")
        }
    }
    companion object {
        fun newInstance(commentId: String): UpdateCommentDialog {
            val args = Bundle().apply {
                putString("commentId", commentId)
            }
            return UpdateCommentDialog().apply {
                arguments = args
            }
        }
    }
}