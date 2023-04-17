package navigation_strategy

import androidx.fragment.app.FragmentManager
import edu.bluejack22_2.BeeTech.ListFragment
import util.FragmentHelper

class ListStrategy : Strategy {
    override fun navigate(supportFragmentManager: FragmentManager) {
        FragmentHelper.replaceFragment(ListFragment(),supportFragmentManager)
    }
}