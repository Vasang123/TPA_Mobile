package adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.CategoryListActivity
import edu.bluejack22_2.BeeTech.R
import model.Category
import view_model.DeleteCategoryViewModel

class CategoryAdapter(val context: Context) :
RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    protected var categoryList = emptyList<Category>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTxt : TextView = view.findViewById(R.id.categoryName)
        val updateCategoryBtn : ImageView = view.findViewById(R.id.updateCategory)
        val deleteCategoryBtn : ImageView = view.findViewById(R.id.deleteCategory)
    }
    fun submitList(lists:List<Category>){
        if(lists.isEmpty()) return
        val oldSize = categoryList.size
        categoryList = lists.toMutableList()
        notifyItemRangeInserted(oldSize,lists.size)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        if(holder is CategoryAdapter.ViewHolder){
            holder.categoryTxt.text = category.name
            holder.deleteCategoryBtn.setOnClickListener{
                (context as CategoryListActivity).showDeleteCategoryConfirmation(category.id)
            }
            holder.updateCategoryBtn.setOnClickListener{
                (context as CategoryListActivity).showUpdateCategory(category.id)
            }
        }
    }

}
