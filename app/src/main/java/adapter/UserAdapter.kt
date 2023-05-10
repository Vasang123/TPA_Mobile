package adapter

import android.content.Context
import android.graphics.Color
import edu.bluejack22_2.BeeTech.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import model.Review
import model.User

class UserAdapter(val context:Context) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
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

//        holder.banBtn.setOnClickListener(){
//
//        }

        //Redirect to user's review list
        holder.usernameTV.setOnClickListener(){}

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameTV : TextView = view.findViewById(R.id.usernameList)
        val emailTV : TextView = view.findViewById(R.id.emailList)
        val banBtn : Button = view.findViewById(R.id.banButton)
        val unbanBtn : Button = view.findViewById(R.id.unbanButton)

    }

}