package edu.bluejack22_2.BeeTech

import adapter.BaseReviewAdapter
import adapter.ReviewAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_2.BeeTech.databinding.FragmentFavouriteListBinding
import util.FragmentHelper
import view_model.FavouriteViewModel
import view_model.UserReviewViewModel
import view_model.UserViewModel

class FavouriteListFragment : Fragment(), BaseReviewAdapter.OnFavoriteClickListener {

    lateinit var userReviewViewModel: UserReviewViewModel
    lateinit var reviewAdapter : ReviewAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var binding : FragmentFavouriteListBinding

    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var userId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.currentUser.observe(requireActivity()) { user ->
            userId = user.id
            init()
        }
    }

    fun init() {
        recyclerView = binding.userFavReviewRecycleView
        favouriteViewModel = FavouriteViewModel()
        reviewAdapter = ReviewAdapter(requireContext(),favouriteViewModel,userId,this)
        setupRecyclerView()
        userReviewViewModel = ViewModelProvider(this)[UserReviewViewModel::class.java]
        userReviewViewModel.loadFavoritedReviews(requireContext())
        userReviewViewModel.reviewList.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                reviewAdapter.submitList(list)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addOnScrollListener(
            InfiniteScrollListener(
                layoutManager,
                { userReviewViewModel.loadMoreFavoritedReviews(requireContext()) },
                { userReviewViewModel.isLoading.value == true }
            )
        )

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

    override fun onFavoriteClick() {
        FragmentHelper.replaceFragment(ListFragment(),parentFragmentManager)

    }


}