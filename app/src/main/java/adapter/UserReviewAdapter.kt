package adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dialog_fragment.UpdateReviewDialog
import edu.bluejack22_2.BeeTech.MainActivity
import edu.bluejack22_2.BeeTech.R
import edu.bluejack22_2.BeeTech.ReviewDetailActivity
import model.Review
import util.ActivityHelper
import view_model.FavouriteViewModel

class UserReviewAdapter(
    private val fragmentManager: FragmentManager,
    private val activity: Activity,
    context: Context,
    private val favouriteViewModel: FavouriteViewModel,
    private val userId : String) : BaseReviewAdapter(context, R.layout.user_review_thumbnail) {

    private val favoriteStatusMap = mutableMapOf<String, Boolean>()
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

            holder.imageView.setOnClickListener{
                (context as Activity).finish()
                ActivityHelper.changePage(context, ReviewDetailActivity::class.java, currentItem.id)
            }

            userId?.let { userId ->
                favouriteViewModel.isReviewFavorited(userId, currentItem.id) { isFavorited ->
                    favoriteStatusMap[currentItem.id] = isFavorited
                    favouriteViewModel.updateFavoriteIndicator(holder.favorite, isFavorited)
                }
            }
            holder.updateButton.setOnClickListener{
                (activity as MainActivity).showUpdateReview(currentItem.id)
            }
            holder.deleteButton.setOnClickListener{
                (activity as MainActivity).showDeleteReviewConfirmation(currentItem.id)
            }
            holder.favorite.setOnClickListener {
                holder.favorite.isEnabled = false

                val currentStatus = favoriteStatusMap[currentItem.id] ?: false

                val newStatus = !currentStatus

                if (newStatus) {
                    favouriteViewModel.addReviewToFavorites(userId, currentItem.id)
                } else {
                    favouriteViewModel.removeReviewFromFavorites(userId, currentItem.id)
                }
                favouriteViewModel.updateFavoriteCount(currentItem, newStatus) {
                        newCount ->  currentItem.totalFavorites = newCount
                    notifyItemChanged(position)
                    holder.favorite.isEnabled = true
                }
                favoriteStatusMap[currentItem.id] = newStatus
                favouriteViewModel.updateFavoriteIndicator(holder.favorite, newStatus)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ReviewViewHolder(itemView)
    }
}
