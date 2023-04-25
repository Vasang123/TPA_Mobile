package navigation_strategy

import androidx.fragment.app.FragmentManager
import edu.bluejack22_2.BeeTech.AdminHomeFragment
import util.FragmentHelper

class AdminHomeStrategy : Strategy {
    override fun navigate(supportFragmentManager: FragmentManager) {
        FragmentHelper.replaceFragment(AdminHomeFragment(),supportFragmentManager)
    }
}