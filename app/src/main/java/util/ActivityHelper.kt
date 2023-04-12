package util

import android.app.Activity
import android.content.Context
import android.content.Intent

object ActivityHelper {
    fun changePage(context: Context, targetActivity: Class<out  Activity>) {
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
    }
}