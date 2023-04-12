package navigation_strategy

import androidx.fragment.app.FragmentManager
import edu.bluejack22_2.BeeTech.CreateFragment
import util.FragmentHelper

class CreateStrategy : Strategy {
    override fun navigate(supportFragmentManager: FragmentManager) {
        FragmentHelper.replaceFragment(CreateFragment(),supportFragmentManager)
    }
}