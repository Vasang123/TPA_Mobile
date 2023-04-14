package edu.bluejack22_2.BeeTech

import adapeter.HomeAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import model.Review
import util.ActivityTemplate
import view_model.HomeViewModel


class HomeFragment : Fragment(),ActivityTemplate {

    lateinit var homeViewModel: HomeViewModel
    lateinit var homeAdapter: HomeAdapter
    lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.homeRecyclerView)
        homeAdapter = HomeAdapter(requireContext())
        setupRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun init() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.loadReviews(requireContext())
        homeViewModel.reviewList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                homeAdapter.submitList(list)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addOnScrollListener(
            InfiniteScrollListener(
                layoutManager,
                { homeViewModel.loadMoreReviews(requireContext()) },
                { homeViewModel.isLoading.value == true }
            )
        )

    }

    override fun onAction() {
        TODO("Not yet implemented")
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = homeAdapter

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