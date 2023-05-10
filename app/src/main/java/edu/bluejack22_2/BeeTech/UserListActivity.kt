package edu.bluejack22_2.BeeTech

import adapter.UserAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.databinding.ActivityUserListBinding
import view_model.UserViewModel

class UserListActivity : AppCompatActivity() {

    lateinit var binding : ActivityUserListBinding
    lateinit var recyclerView : RecyclerView
    lateinit var userAdapter : UserAdapter
    lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }


    fun init(){
        recyclerView = binding.userListRecycleView
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userAdapter = UserAdapter(this,userViewModel)
        setupRecyclerView()

        userViewModel.loadUser()

        userViewModel.userList.observe(this, Observer { users ->
            userAdapter.submitList(users)
        })


        val layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(
            InfiniteScrollListener(
                layoutManager,
                { userViewModel.loadMoreUser() },
                { userViewModel.isLoading.value == true }
            )
        )


    }
    class InfiniteScrollListener(
        private val layoutManager: LinearLayoutManager,
        private val loadMore: () -> Unit,
        private val isLoading: () -> Boolean
    ) : RecyclerView.OnScrollListener() {
        private val threshold = 5

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (isLoading()) return

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (firstVisibleItemPosition + visibleItemCount + threshold >= totalItemCount) {

                loadMore()
            }
        }
    }
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = userAdapter
    }


}