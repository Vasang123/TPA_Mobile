package adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import edu.bluejack22_2.BeeTech.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.UserListActivity
import edu.bluejack22_2.BeeTech.UserSpesificReview
import model.User
import util.ActivityHelper
import view_model.UserViewModel

class UserAdapter(val context:Context, private val userViewModel : UserViewModel?) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    protected  var userList = emptyList<User>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate( R.layout.user_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun submitList(lists:List<User>){
        if(lists.isEmpty()) return
        val oldSize = userList.size
        userList = lists.toMutableList()
        notifyItemRangeInserted(oldSize,lists.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = userList[position]

        if(holder is ViewHolder){
            holder.usernameTV.text = currentUser.username
            holder.emailTV.text = currentUser.email
        }

        if(currentUser.status == "banned"){
            Log.e("User Data", currentUser.username)
            val color = ContextCompat.getColor(context, R.color.banned)
            holder.itemView.setBackgroundColor(color)
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }


        holder.banBtn.setOnClickListener(){
            userViewModel?.banUser(userID = currentUser.id)

            (context as UserListActivity).finish()
            ActivityHelper.changePage(context,UserListActivity::class.java)
        }

        holder.unbanBtn.setOnClickListener(){
            userViewModel?.unbanUser(userID = currentUser.id)
            (context as UserListActivity).finish()
            ActivityHelper.changePage(context,UserListActivity::class.java)
        }

        holder.usernameTV.setOnClickListener(){
            ActivityHelper.changePage(context,UserSpesificReview::class.java,currentUser.id)
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameTV : TextView = view.findViewById(R.id.usernameList)
        val emailTV : TextView = view.findViewById(R.id.emailList)
        val banBtn : Button = view.findViewById(R.id.banButton)
        val unbanBtn : Button = view.findViewById(R.id.unbanButton)

    }

}