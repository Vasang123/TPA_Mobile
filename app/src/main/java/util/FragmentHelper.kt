package util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.dynamic.SupportFragmentWrapper
import edu.bluejack22_2.BeeTech.R

object FragmentHelper {
    fun replaceFragment(fragment: Fragment,supportFragmentManager:FragmentManager){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}