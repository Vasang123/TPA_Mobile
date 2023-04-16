package adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.R
import model.Review

class HomeAdapter(private val context: Context) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var reviewList = emptyList<Review>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_thumbnail, parent, false)
        return HomeViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = reviewList[position]
        holder.title.text = currentItem.title
        holder.createdAt.text = currentItem.createdAt.toString()
        holder.author.text = currentItem.username
        // Load image using Glide library
//        Glide.with(context)
//            .load(currentItem.imageURL)
//            .placeholder(R.drawable.blank_image)
//            .error(R.drawable.blank_image)
//            .into(holder.imageView)
//        if (currentItem.isFavorite) {
//            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_filled)
//        } else {
//            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite)
//        }
//        holder.itemView.setOnClickListener {
//            listener.onItemClick(currentItem)
//        }
    }
    override fun getItemCount(): Int {
        return reviewList.size
    }

    fun submitList(lists:List<Review>){
        Log.e("submit list", reviewList.toString())
        if(lists.isEmpty()) return

        val oldSize = reviewList.size
        reviewList = lists.toMutableList()
        notifyItemRangeInserted(oldSize,lists.size)
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageThumbnail)
        val title: TextView = itemView.findViewById(R.id.titleThumbnail)
        var createdAt: TextView = itemView.findViewById(R.id.dateThumbnail)
        var author: TextView = itemView.findViewById(R.id.authorThumbnail)
        val favorite: ImageView = itemView.findViewById(R.id.favorite)

//        init {
//            favorite.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    val currentItem = reviewList[position]
//                    listener.onFavoriteClick(currentItem)
//                }
//            }
//        }
    }




}