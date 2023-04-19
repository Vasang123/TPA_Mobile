package edu.bluejack22_2.BeeTech

import adapter.ReviewAdapter
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
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack22_2.BeeTech.databinding.FragmentProfileBinding
import edu.bluejack22_2.BeeTech.databinding.FragmentSearchBinding

import model.Review
import util.ActivityTemplate
import view_model.HomeViewModel
import view_model.SearchViewModel

class SearchFragment : Fragment(),ActivityTemplate {
    lateinit var searchViewModel: SearchViewModel
    lateinit var reviewAdapter: ReviewAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView
    lateinit var searchQuery: String
    lateinit var binding: FragmentSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchQuery = arguments?.getString("searchQuery").toString()
        init()
        onAction()
    }

    override fun init() {
        recyclerView = binding.searchRecycleReview
        reviewAdapter = ReviewAdapter(requireContext())
        setupRecyclerView()
        searchView = binding.searchBar
        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        Log.e("123",searchQuery)
        if(searchQuery.isNullOrEmpty() || searchQuery == "null"){
            searchViewModel.loadReviews(requireContext(),"")
        }else{
            searchViewModel.loadReviews(requireContext(),searchQuery)
        }
        searchViewModel.reviewList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                reviewAdapter.submitList(list)
            }
        })
    }
    private fun resetAdapter(newList: List<Review>) {
        val newAdapter = ReviewAdapter(requireContext())
        newAdapter.submitList(newList)
        recyclerView.adapter = newAdapter
    }

    override fun onAction() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewModel.loadReviews(requireContext(),query.toString())
                return false
            }


            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel.loadReviews(requireContext(),newText.toString())
                return false
            }

        })
        searchViewModel.reviewList.observe(viewLifecycleOwner, Observer { list ->
            if (list.isNotEmpty()) {
                resetAdapter(list)
            }
        })
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager =  layoutManager
        recyclerView.adapter = reviewAdapter
    }
}