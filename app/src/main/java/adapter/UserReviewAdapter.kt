package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.bluejack22_2.BeeTech.R
import model.Review

class UserReviewAdapter(context: Context) : BaseReviewAdapter(context, R.layout.user_review_thumbnail) {

    inner class ReviewViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val updateButton: Button = itemView.findViewById(R.id.userUpdateReview)
        val deleteButton: Button = itemView.findViewById(R.id.userDeleteReview)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val currentItem = reviewList[position]
        if (holder is ReviewViewHolder) {
            holder.title.text = currentItem.title
            holder.createdAt.text = currentItem.createdAt.toString()
            holder.author.text = currentItem.username
            holder.favCount.text = currentItem.totalFavorites.toString()
            Glide.with(context)
                .load(currentItem.imageURL)
                .into(holder.imageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ReviewViewHolder(itemView)
    }
}
