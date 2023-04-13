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
import util.ActivityTemplate
import view_model.HomeViewModel


class HomeFragment : Fragment(),ActivityTemplate {

    lateinit var homeViewModel: HomeViewModel
    lateinit var homeAdapter: HomeAdapter
    lateinit var recyclerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        init()
//        onAction()
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

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.loadReviews(requireContext())
        homeViewModel.reviewList.observe(this, Observer { list ->
            if (!list.isEmpty()) {
                homeAdapter.submitList(list)
            }
        })
    }

    override fun init() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setupRecyclerView()
        homeViewModel.loadReviews(requireContext())
        homeViewModel.reviewList.observe(this, Observer { list ->
            if (!list.isEmpty()) {
                homeAdapter.submitList(list)
            }
        })
    }

    override fun onAction() {
        TODO("Not yet implemented")
    }


    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = homeAdapter
    }
}