package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.R
import model.Comment
import model.Review
import java.text.SimpleDateFormat
import java.util.Locale

class CommentAdapter(val context: Context):
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    protected var commentList = emptyList<Comment>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = commentList[position]
        holder.author.text = currentComment.username
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        holder.createdAt.text = "Created At: " + sdf.format(currentComment.createdAt)
        holder.updatedAt.text = "Updated At: " +sdf.format(currentComment.updatedAt)
        holder.content.text = currentComment.content
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
    fun submitList(lists:List<Comment>){
        if(lists.isEmpty()) return
        val oldSize = commentList.size
        commentList = lists.toMutableList()
        notifyItemRangeInserted(oldSize,lists.size)
    }
    open inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val author: TextView = itemView.findViewById(R.id.authorComment)
        val createdAt: TextView = itemView.findViewById(R.id.commentCreatedAt)
        val updatedAt: TextView = itemView.findViewById(R.id.commentUpdatedAt)
        val content: TextView = itemView.findViewById(R.id.commentContent)
//        val updateButton: ImageView = itemView.findViewById(R.id.updateCommentButton)
//        val deleteButton: ImageView = itemView.findViewById(R.id.deleteComment)
        init {

        }
    }

}