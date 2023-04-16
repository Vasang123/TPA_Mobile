package navigation_strategy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
import edu.bluejack22_2.BeeTech.SearchFragment
import util.FragmentHelper

class SearchStrategy : Strategy {
    override fun navigate(supportFragmentManager: FragmentManager) {
        FragmentHelper.replaceFragment(SearchFragment(),supportFragmentManager)
    }
    fun navigateWithQuery(supportFragmentManager: FragmentManager, query:String){
        val searchFragment = SearchFragment()
        val bundle = Bundle()
        bundle.putString("searchQuery", query)
        searchFragment.arguments = bundle
        FragmentHelper.replaceFragment(searchFragment, supportFragmentManager)
    }
}