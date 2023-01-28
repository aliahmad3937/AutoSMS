package com.auto.sms.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import com.auto.sms.R
import java.util.*


class LoadingUtils {
    private val context: Context? = null

    companion object {
        private const val TAG = "LoadAds"
        var lotiedilouge: Dialog? = null
        fun showLoading(activity: Activity?) {
            lotiedilouge = Dialog(activity!!)
            lotiedilouge!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Optional.ofNullable(lotiedilouge!!.window).ifPresent { window: Window ->
                    window.setBackgroundDrawable(
                        ColorDrawable(
                            Color.TRANSPARENT
                        )
                    )
                }
            }
            lotiedilouge!!.setCancelable(false)
            lotiedilouge!!.setContentView(R.layout.loading_dialog_lottie)
            lotiedilouge!!.show()
        }

        fun setMessage(message: String) {
            val textView = lotiedilouge!!.findViewById<TextView>(R.id.message_tv)
            if (!message.isEmpty()) {
                textView.text = message
            }
        }

        fun pauseLoading() {
            if (lotiedilouge != null && lotiedilouge!!.isShowing) {
                Log.d(TAG, "pauseLotieProgressDialog: ")
                lotiedilouge!!.cancel()
                lotiedilouge!!.dismiss()
            }
        }
         fun isNetworkAvailable(ctx:Context): Boolean {
            val connectivityManager =
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }


}