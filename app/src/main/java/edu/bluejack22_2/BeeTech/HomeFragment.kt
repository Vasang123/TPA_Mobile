package edu.bluejack22_2.BeeTech

import adapter.BaseReviewAdapter
import adapter.ReviewAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.databinding.FragmentHomeBinding
import util.ActivityTemplate
import view_model.FavouriteViewModel
import view_model.HomeViewModel
import view_model.UserViewModel


class HomeFragment : Fragment(),ActivityTemplate,BaseReviewAdapter.OnFavoriteClickListener {

    lateinit var homeViewModel: HomeViewModel
    lateinit var reviewAdapter: ReviewAdapter
    lateinit var recyclerView:RecyclerView
    lateinit var searchView: SearchView
    lateinit var binding: FragmentHomeBinding
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var userId : String

    var onSearchQueryListener: OnSearchQueryListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.currentUser.observe(requireActivity()) { user ->
            userId = user.id
            init()
            onAction()
        }
    }

    override fun init() {
        recyclerView = binding.homeRecyclerView
        favouriteViewModel = FavouriteViewModel()
        reviewAdapter = ReviewAdapter(requireContext(),favouriteViewModel,userId,this)
        setupRecyclerView()
        searchView = binding.homeSearch
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.loadReviews(requireContext())
        homeViewModel.reviewList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                reviewAdapter.submitList(list)
            }
        }

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

    override fun onFavoriteClick() {

    }


}