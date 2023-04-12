package navigation_strategy

import androidx.fragment.app.FragmentManager
import edu.bluejack22_2.BeeTech.HomeFragment
import util.FragmentHelper

class HomeStrategy: Strategy {
    override fun navigate(supportFragmentManager: FragmentManager) {
        FragmentHelper.replaceFragment(HomeFragment(),supportFragmentManager)
    }
}