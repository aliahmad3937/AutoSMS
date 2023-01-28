package com.auto.sms.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.auto.sms.R
import com.auto.sms.models.MissedCallModel
import com.auto.sms.utils.SharePrefs.Companion.getInstance
import com.google.gson.Gson
import java.util.*

class CallReceiver : PhonecallReceiver() {
    private val TAG = "CallReceiver"
    override fun onIncomingCallReceived(ctx: Context?, number: String?, start: Date?) {
        Log.i(TAG, "onIncomingCallReceived: nmbr:$number")
        //
    }

    override fun onIncomingCallAnswered(ctx: Context?, number: String?, start: Date?) {
        //
        Log.i(TAG, "onIncomingCallAnswered: nmbr:$number")
        if (ctx != null) {
            // SharePrefs.getInstance(ctx)?.saveReceivedCallNumber(ctx,number!!)
            sendSmsMsgFnc(number, ctx, false)
        }
    }

    override fun onIncomingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        //
        Log.i(TAG, "onIncomingCallEnded: nmbr:$number")

    }

    override fun onOutgoingCallStarted(ctx: Context?, number: String?, start: Date?) {
        //
        Log.i(TAG, "onOutgoingCallStarted: nmbr:$number")

    }

    override fun onOutgoingCallEnded(ctx: Context?, number: String?, start: Date?, end: Date?) {
        //
        Log.i(TAG, "onOutgoingCallEnded: nmbr:$number")

    }

    override fun onMissedCall(ctx: Context?, number: String?, start: Date?) {
        //
        Log.i(TAG, "onMissedCall: nmbr:$number")
        if (ctx != null) {
            //  SharePrefs.getInstance(ctx)?.saveMissedCallNumber(ctx,number!!)

            sendSmsMsgFnc(number, ctx, true)

        }
    }


    fun sendSmsMsgFnc(mblNumVar: String?, context: Context, missCall: Boolean) {
        Log.i(TAG, "sendSmsMsgFnc: nmbr:")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val excludeInContactList = SharePrefs.getInstance(context)!!.getExcludeInContactList(context)
                val excludeOutContactList = SharePrefs.getInstance(context)!!.getExcludeOutContactList(context)
                val inContactList = SharePrefs.getInstance(context)!!.getInContactList(context)
              //  val outContactList = SharePrefs.getInstance(context)!!.getOutContactList(context)
                val afterCount = SharePrefs.getInstance(context)!!.getAfterCount(context)
                if (missCall) {
                    Log.i(TAG, "Miss call: nmbr::$mblNumVar")
                    val missCallMap: MutableMap<String, Int> =
                        SharePrefs.getInstance(context)!!.getMissCallMap(context).toMutableMap()
                    if (afterCount != 0) {
                        Log.i(TAG, "Miss call: if afterCount::$afterCount")
                        if (missCallMap.isNotEmpty() && missCallMap.containsKey(mblNumVar)) {

                            val myCount = missCallMap[mblNumVar]!!
                            Log.i(TAG, "Miss call: mapCallCount::$myCount")
                            if (myCount < afterCount) {
                                Log.i(TAG, "Miss call: 90")
                                missCallMap[mblNumVar!!] = myCount + 1
                                SharePrefs.getInstance(context)!!
                                    .saveMissCallMap(context, missCallMap)
                            } else {
                                Log.i(TAG, "Miss call: 95")
                                if ((excludeInContactList.isNotEmpty() && excludeInContactList.contains(mblNumVar)) || (excludeOutContactList.isNotEmpty() && excludeOutContactList.contains(
                                        mblNumVar
                                    ))
                                ) {
                                    Log.i(TAG, "Miss call: 100")
                                    // do nothing
                                } else {
                                    Log.i(TAG, "Miss call: 103")
                                    var msg = ""
                                    if(inContactList.isNotEmpty() && inContactList.contains(mblNumVar)){
                                        msg = SharePrefs.getInstance(context)!!.getInMissedCallMsg(context)
                                    }else{
                                        msg = SharePrefs.getInstance(context)!!.getOutMissedCallMsg(context)
                                    }
                                    val smsMgrVar: SmsManager = SmsManager.getDefault()
                                    smsMgrVar.sendTextMessage(mblNumVar, null, msg, null, null)
//                                    Toast.makeText(
//                                        context, context.getString(R.string.sent_sms),
//                                        Toast.LENGTH_LONG
//                                    ).show()

                                    missCallMap.remove(mblNumVar!!)
                                    SharePrefs.getInstance(context)!!
                                        .saveMissCallMap(context, missCallMap)

                                }
                            }
                        }
                        else {
                            Log.i(TAG, "Miss call: mapEmpty::")
                            missCallMap.put(mblNumVar!!, 0)
                            var msg = ""
                            if(inContactList.isNotEmpty() && inContactList.contains(mblNumVar)){
                                 msg = SharePrefs.getInstance(context)!!.getInMissedCallMsg(context)
                            }else{
                                msg = SharePrefs.getInstance(context)!!.getOutMissedCallMsg(context)
                            }

                            val smsMgrVar: SmsManager = SmsManager.getDefault()
                            smsMgrVar.sendTextMessage(mblNumVar, null, msg, null, null)
                            SharePrefs.getInstance(context)!!.saveMissCallMap(context, missCallMap)
                        }
                    }else{
                        Log.i(TAG, "Miss call: else afterCount::$afterCount")
                    }
                } else {
                    Log.i(TAG, "Receive call: 128")
                    val receiveCallMap =
                        SharePrefs.getInstance(context)!!.getReceiveCallMap(context).toMutableMap()

                    if (afterCount != 0) {
                        Log.i(TAG, "Receive call: 133")
                        if (receiveCallMap.isNotEmpty() && receiveCallMap.containsKey(mblNumVar)) {
                            Log.i(TAG, "Receive call: 135")
                            val myCount = receiveCallMap[mblNumVar]!!
                            if (myCount < afterCount) {
                                Log.i(TAG, "Receive call: 138")
                                receiveCallMap[mblNumVar!!] = myCount + 1
                                SharePrefs.getInstance(context)!!
                                    .saveReceiveCallMap(context, receiveCallMap)
                            } else {
                                Log.i(TAG, "Receive call: 143")
                                if ((excludeInContactList.isNotEmpty() && excludeInContactList.contains(mblNumVar)) || (excludeOutContactList.isNotEmpty() && excludeOutContactList.contains(
                                        mblNumVar
                                    ))
                                ) {
                                    Log.i(TAG, "Receive call: 148")
                                    // do nothing
                                } else {
                                    Log.i(TAG, "Receive call: 151")
                                    var msg = ""
                                    if(inContactList.isNotEmpty() && inContactList.contains(mblNumVar)){
                                        msg = SharePrefs.getInstance(context)!!.getInReceiveCallMsg(context)
                                    }else{
                                        msg = SharePrefs.getInstance(context)!!.getOutReceiveCallMsg(context)
                                    }

                                    val smsMgrVar: SmsManager = SmsManager.getDefault()
                                    smsMgrVar.sendTextMessage(mblNumVar, null, msg, null, null)
                                    Toast.makeText(
                                        context, context.getString(R.string.sent_sms),
                                        Toast.LENGTH_LONG
                                    ).show()

                                    receiveCallMap.remove(mblNumVar!!)
                                    SharePrefs.getInstance(context)!!
                                        .saveReceiveCallMap(context, receiveCallMap)

                                }
                            }
                        } else {
                            receiveCallMap.put(mblNumVar!!, 0)
                            var msg = ""
                            if(inContactList.isNotEmpty() && inContactList.contains(mblNumVar)){
                                msg = SharePrefs.getInstance(context)!!.getInReceiveCallMsg(context)
                            }else{
                                msg = SharePrefs.getInstance(context)!!.getOutReceiveCallMsg(context)
                            }

                            val smsMgrVar: SmsManager = SmsManager.getDefault()
                            smsMgrVar.sendTextMessage(mblNumVar, null, msg, null, null)
                            SharePrefs.getInstance(context)!!
                                .saveReceiveCallMap(context, receiveCallMap)
                        }
                    }

                }
            } catch (ErrVar: Exception) {
                Log.i(TAG, "sendSmsMsgFnc: nmbr: error:" + ErrVar.message)

                Toast.makeText(
                    context, ErrVar.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
                ErrVar.printStackTrace()
            }
        } else {

        }
    }

    class SmsSentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, arg1: Intent) {
            when (resultCode) {
                Activity.RESULT_OK -> Toast.makeText(
                    context,
                    context.resources.getString(R.string.sent_sms),
                    Toast.LENGTH_SHORT
                ).show()
                SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                    context,
                    "SMS generic failure",
                    Toast.LENGTH_SHORT
                ).show()
                SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                    context,
                    "SMS no service",
                    Toast.LENGTH_SHORT
                )
                    .show()
                SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(
                    context,
                    "SMS null PDU",
                    Toast.LENGTH_SHORT
                ).show()
                SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(
                    context,
                    "SMS radio off",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}