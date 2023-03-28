package Util

import android.content.Context
import android.content.Intent
import com.example.tpa_mobile.MainActivity
import com.example.tpa_mobile.RegisterActivity

class ActivtyHelper {
    companion object {
        @JvmStatic
        fun changeToRegis(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
        @JvmStatic
        fun changeToLogin(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

}