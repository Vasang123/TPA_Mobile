package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.R
import model.Review

abstract class BaseReviewAdapter(val context: Context, val layoutId: Int) :
    RecyclerView.Adapter<BaseReviewAdapter.BaseViewHolder>() {

    protected var reviewList = emptyList<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return BaseViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    fun submitList(lists:List<Review>){
        if(lists.isEmpty()) return
        val oldSize = reviewList.size
        reviewList = lists.toMutableList()
        notifyItemRangeInserted(oldSize,lists.size)
    }

    open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageThumbnail)
        val title: TextView = itemView.findViewById(R.id.titleThumbnail)
        var createdAt: TextView = itemView.findViewById(R.id.dateThumbnail)
        var author: TextView = itemView.findViewById(R.id.authorThumbnail)
        var favCount: TextView = itemView.findViewById(R.id.favCount)
        val favorite: ImageView = itemView.findViewById(R.id.favorite)


        init {

        }
    }

}