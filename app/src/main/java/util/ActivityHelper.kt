package util

import android.app.Activity
import android.content.Context
import android.content.Intent
import model.Review

object ActivityHelper {
    fun changePage(context: Context, targetActivity: Class<out  Activity>) {
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
    }
    fun changePage(context: Context, targetActivity: Class<out  Activity>, reviewId : String, reviewOwner : String) {
        val intent = Intent(context, targetActivity)
        intent.putExtra("review", reviewId)
        intent.putExtra("reviewOwner", reviewOwner)
        context.startActivity(intent)
    }
}