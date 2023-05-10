package adapter

import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bumptech.glide.Glide
import edu.bluejack22_2.BeeTech.*
import util.ActivityHelper
import view_model.DeleteReviewViewModel
import view_model.FavouriteViewModel

class AdminReviewAdapter(
    userId : String,
    private val deleteReviewViewModel: DeleteReviewViewModel,
    context: Context,
    onFavoriteClickListener: OnFavoriteClickListener
) : BaseReviewAdapter(context, R.layout.user_review_admin_thumbnail, onFavoriteClickListener) {
    val userId = userId
    inner class ReviewViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val deleteButton: Button = itemView.findViewById(R.id.userDeleteReview)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val currentItem = reviewList[position]
        if (holder is ReviewViewHolder) {
            holder.title.text = currentItem.title
            holder.createdAt.text = currentItem.createdAt.toString()
            holder.author.text = currentItem.username
            holder.favCount.text = currentItem.totalFavorites.toString()
            holder.favCount.visibility = View.GONE
            holder.favorite.visibility = View.GONE
            Glide.with(context)
                .load(currentItem.imageURL)
                .into(holder.imageView)

            holder.imageView.setOnClickListener{
                (context as Activity).finish()
                ActivityHelper.changePage(context, ReviewDetailActivity::class.java, currentItem.id, currentItem.userId)
            }

            holder.deleteButton.setOnClickListener{
                deleteReviewViewModel.deleteReview(context,currentItem.id)
                (context as Activity).finish()
                ActivityHelper.changePage(context, UserSpesificReview::class.java,  userId)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ReviewViewHolder(itemView)
    }
}
