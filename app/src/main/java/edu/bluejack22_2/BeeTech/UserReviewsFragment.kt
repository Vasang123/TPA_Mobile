package edu.bluejack22_2.BeeTech

import adapter.UserReviewAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import util.ActivityTemplate
import view_model.UserReviewViewModel

class UserReviewsFragment : Fragment(), ActivityTemplate {

    lateinit var userReviewViewModel: UserReviewViewModel
    lateinit var userReviewAdapter: UserReviewAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_review, container, false)
        recyclerView = view.findViewById(R.id.userReviewRecycleView)
        userReviewAdapter = UserReviewAdapter(requireContext())
        setupRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onAction()
    }

    override fun init() {
        userReviewViewModel = ViewModelProvider(this)[UserReviewViewModel::class.java]
        userReviewViewModel.loadReviews(requireContext())
        userReviewViewModel.reviewList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                userReviewAdapter.submitList(list)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addOnScrollListener(
            InfiniteScrollListener(
                layoutManager,
                { userReviewViewModel.loadMoreUserReviews(requireContext()) },
                { userReviewViewModel.isLoading.value == true }
            )
        )

    }

    override fun onAction() {

    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = userReviewAdapter

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





}