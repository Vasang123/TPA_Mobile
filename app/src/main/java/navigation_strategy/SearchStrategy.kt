package navigation_strategy

import androidx.fragment.app.FragmentManager
import edu.bluejack22_2.BeeTech.SearchFragment
import util.FragmentHelper

class SearchStrategy : Strategy {
    override fun navigate(supportFragmentManager: FragmentManager) {
        FragmentHelper.replaceFragment(SearchFragment(),supportFragmentManager)
    }
}