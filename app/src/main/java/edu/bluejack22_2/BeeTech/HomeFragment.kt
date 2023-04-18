package edu.bluejack22_2.BeeTech

import adapter.ReviewAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import navigation_strategy.SearchStrategy
import util.ActivityTemplate
import view_model.HomeViewModel


class HomeFragment : Fragment(),ActivityTemplate {

    lateinit var homeViewModel: HomeViewModel
    lateinit var reviewAdapter: ReviewAdapter
    lateinit var recyclerView:RecyclerView
    lateinit var searchView: SearchView
    var onSearchQueryListener: OnSearchQueryListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchView = view?.findViewById(R.id.homeSearch) ?: SearchView(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.homeRecyclerView)
        reviewAdapter = ReviewAdapter(requireContext())
        setupRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        onAction()
    }

    override fun init() {
        searchView = view?.findViewById(R.id.homeSearch) ?: SearchView(requireContext())
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.loadReviews(requireContext())
        homeViewModel.reviewList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                reviewAdapter.submitList(list)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addOnScrollListener(
            InfiniteScrollListener(
                layoutManager,
                { homeViewModel.loadMoreHomeReviews(requireContext()) },
                { homeViewModel.isLoading.value == true }
            )
        )

    }

    override fun onAction() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    onSearchQueryListener?.onSearchQuery(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

        })
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = reviewAdapter

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
    interface OnSearchQueryListener {
        fun onSearchQuery(query: String)
    }
    @JvmName("setOnSearchQueryListener1")
    fun setOnSearchQueryListener(listener: OnSearchQueryListener) {
        onSearchQueryListener = listener
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchQueryListener) {
            onSearchQueryListener = context
        } else {
            throw RuntimeException("$context must implement OnSearchQueryListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onSearchQueryListener = null
    }



}