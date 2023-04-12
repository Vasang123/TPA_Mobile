package navigation_strategy

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ListFragment
import util.FragmentHelper

class ListStrategy : Strategy {
    override fun navigate(supportFragmentManager: FragmentManager) {
        FragmentHelper.replaceFragment(ListFragment(),supportFragmentManager)
    }
}