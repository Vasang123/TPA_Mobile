package edu.bluejack22_2.BeeTech

import adapter.ListViewPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import util.ActivityTemplate


class ListFragment : Fragment(), ActivityTemplate {

    lateinit var listViewPager: ViewPager2
    lateinit var listViewPagerAdapter: ListViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun init() {
        listViewPager =   requireView().findViewById(R.id.list_pager)
        listViewPagerAdapter = ListViewPagerAdapter(parentFragmentManager, this.lifecycle)
        listViewPager.adapter = listViewPagerAdapter
        val tabLayout = requireView().findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, listViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Favorite List"
                1 -> tab.text = "My Review List"
                else -> throw IllegalArgumentException("Invalid page position: $position")
            }
        }.attach()
    }

    override fun onAction() {
        TODO("Not yet implemented")
    }


}