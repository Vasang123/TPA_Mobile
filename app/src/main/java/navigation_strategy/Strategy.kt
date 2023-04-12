package navigation_strategy

import androidx.fragment.app.FragmentManager

interface Strategy {
    fun navigate(supportFragmentManager: FragmentManager)
}