package com.auto.sms.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.auto.sms.R
import com.auto.sms.models.MessageModel
import com.auto.sms.models.UserModel
import com.auto.sms.utils.SharePrefs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.gson.Gson

class MainActivity : BaseActivity()
{
    private  val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        FirebaseFirestore.getInstance().firestoreSettings = settings

    }
}