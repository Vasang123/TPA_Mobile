package edu.bluejack22_2.BeeTech

import adapter.BaseReviewAdapter
import adapter.ReviewAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.databinding.ActivityCategoryListBinding
import edu.bluejack22_2.BeeTech.databinding.ActivityUserSpesificReviewBinding
import util.ActivityTemplate
import view_model.*

class UserSpesificReview : AppCompatActivity(), ActivityTemplate, BaseReviewAdapter.OnFavoriteClickListener {
    lateinit var binding : ActivityUserSpesificReviewBinding
    lateinit var recyclerView: RecyclerView
    lateinit var reviewAdapter: ReviewAdapter
    lateinit var userSpesificViewModel: UserSpesificViewModel
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var userId : String
    lateinit var targetId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSpesificReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.currentUser.observe(this) { user ->
            userId = user.id
            init()
            onAction()
        }
    }

    override fun init() {
        val intent = intent
        targetId = intent.getStringExtra("userid").toString()
        recyclerView = binding.userSpesificRecycleView
        favouriteViewModel = FavouriteViewModel()
        userSpesificViewModel = ViewModelProvider(this)[UserSpesificViewModel::class.java]
        reviewAdapter = ReviewAdapter(this,favouriteViewModel,userId,this)
        setupRecyclerView()
        userSpesificViewModel.loadReviews(this,targetId)
        userSpesificViewModel.reviewList.observe(this) { list ->
            if (list.isNotEmpty()) {
                reviewAdapter.submitList(list)
            }
        }

    }
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = reviewAdapter
    }

    override fun onAction() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(
            InfiniteScrollListener(
                layoutManager,
                { userSpesificViewModel.loadMoreUserSpesificReviews(this,targetId) },
                { userSpesificViewModel.isLoading.value == true }
            )
        )
    }
    class InfiniteScrollListener(
        private val layoutManager: LinearLayoutManager,
        private val loadMore: () -> Unit,
        private val isLoading: () -> Boolean
    ) : RecyclerView.OnScrollListener() {
        private val threshold = 2

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
    override fun onFavoriteClick() {

    }
}