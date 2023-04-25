package adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import edu.bluejack22_2.BeeTech.FavouriteListFragment
import edu.bluejack22_2.BeeTech.UserReviewsFragment

class ListViewPagerAdapter(private val fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouriteListFragment()
            1 -> UserReviewsFragment()
            else -> throw IllegalArgumentException("Invalid page position: $position")
        }
    }

}