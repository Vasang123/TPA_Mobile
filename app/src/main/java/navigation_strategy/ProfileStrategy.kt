package navigation_strategy

import androidx.fragment.app.FragmentManager
import edu.bluejack22_2.BeeTech.ProfileFragment
import util.FragmentHelper

class ProfileStrategy : Strategy {
    override fun navigate(supportFragmentManager: FragmentManager) {
        FragmentHelper.replaceFragment(ProfileFragment(),supportFragmentManager)
    }
}