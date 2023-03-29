package Util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.tpa_mobile.RegisterActivity

object ActivtyHelper {
    fun changePage(context: Context, targetActivity: Class<out  Activity>) {
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
    }
}