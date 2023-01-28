package com.auto.sms.activities

import android.content.Context

import androidx.appcompat.app.AppCompatActivity

import android.os.Build
import com.auto.sms.utils.SharePrefs.Companion.getInstance
import java.util.*

open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val localeString = getInstance(newBase)!!.lANGUAGE
        val myLocale = Locale(localeString)
        Locale.setDefault(myLocale)
        val config = newBase.resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(myLocale)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                val newContext = newBase.createConfigurationContext(config)
                super.attachBaseContext(newContext)
                return
            }
        } else {
            config.locale = myLocale
        }
        super.attachBaseContext(newBase)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}