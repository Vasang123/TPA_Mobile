package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import edu.bluejack22_2.BeeTech.R
import view_model.FavouriteViewModel
import view_model.UserViewModel

class ReviewAdapter(context: Context) : BaseReviewAdapter(context, R.layout.review_thumbnail) {

    lateinit var favouriteViewModel : FavouriteViewModel

    inner class ReviewViewHolder(itemView: View) : BaseViewHolder(itemView) {

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


//        favouriteViewModel.isReviewFavorited()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)

        return ReviewViewHolder(itemView)
    }
}
