package com.auto.sms.utils

import android.content.Context
import android.content.SharedPreferences
import com.auto.sms.models.MissedCallModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type


class SharePrefs(private val context: Context) {

    private val PREFERENCE = "AutoSms"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCE, 0)
    private var postSmsPref = "POST_SMS"
    private var inMissedCallSms = "IN_MISSED_SMS"
    private var outMissedCallSms = "OUT_MISSED_SMS"
    private var saveContactsSms = "SAVE_CONTACTS_SMS"
    private var missContactsSms = "MISS_CONTACTS_SMS"
    private var inReceivedCallsms = "IN_RECEIVED_SMS"
    private var outReceivedCallsms = "OUT_RECEIVED_SMS"
    private var receivedCallNumber= "RECEIVED_CALL_NUMBER"
    private var missedCallNumber= "Missed_CALL_NUMBER"
    private var excludeInContactList= "EXCLUDE_IN_CONTACT_LIST"
    private var inContactList= "IN_CONTACT_LIST"
    private var excludeOutContactList= "EXCLUDE_OUT_CONTACT_LIST"
    private var outContactList= "OUT_CONTACT_LIST"
    private var afterCallCount= "AFTER_CALL_COUNT"
    private var missCallMap= "MISS_CALL_MAP"
    private var receiveCallMap= "RECEIVE_CALL_MAP"
    private var messagesList= "MESSAGES_LIST"


    companion object {
        private var instance: SharePrefs? = null
        @JvmStatic
        fun getInstance(context: Context): SharePrefs? {
            if (instance == null) {
                instance = SharePrefs(context)
            }
            return instance
        }
    }

    var loginModel: String? = "{}"
        get() = sharedPreferences.getString(field, "{}")
        set(gson_object) {
            sharedPreferences.edit().putString(loginModel, gson_object).apply()
        }

//    var missedCallNumber: String? = "missedCallNumber"
//        get() = sharedPreferences.getString(field, "")
//        set(number) {
//           // sharedPreferences.edit().putString(missedCallNumber, number).apply()
//            val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//            val editor = sharedPreference.edit()
//            editor.putString(field, number)
//            editor.apply()
//        }
//
//
//    var receivedCallNumber: String? = "receivedCallNumber"
//        get() = sharedPreferences.getString(field, "")
//        set(number) {
//           // sharedPreferences.edit().putString(receivedCallNumber, number).apply()
//            val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//            val editor = sharedPreference.edit()
//            editor.putString(field, number)
//            editor.apply()
//        }

    var lANGUAGE = "language"
        get() = sharedPreferences.getString(field, "iw")!!
        set(language) {
            val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString(field, language)
                editor.apply()
        }
    fun saveOutMissedCallMsg(context: Context, sms: String) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.remove(outMissedCallSms)
        editor.putString(outMissedCallSms, sms)
        editor.apply()
    }
    fun getOutMissedCallMsg(context: Context) : String{
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val msg = sharedPreference.getString(outMissedCallSms,"")
        return msg ?: ""
    }

    fun saveOutReceiveCallMsg(context: Context, sms: String) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.remove(outReceivedCallsms)
        editor.putString(outReceivedCallsms, sms)
        editor.apply()
    }
    fun getOutReceiveCallMsg(context: Context) : String{
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val msg = sharedPreference.getString(outReceivedCallsms,"")
        return msg ?: ""
    }


    fun saveInMissedCallMsg(context: Context, sms: String) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.remove(inMissedCallSms)
        editor.putString(inMissedCallSms, sms)
        editor.apply()
    }
    fun getInMissedCallMsg(context: Context) : String{
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val msg = sharedPreference.getString(inMissedCallSms,"")
        return msg ?: ""
    }

    fun saveInReceiveCallMsg(context: Context, sms: String) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.remove(inReceivedCallsms)
        editor.putString(inReceivedCallsms, sms)
        editor.apply()
    }
    fun getInReceiveCallMsg(context: Context) : String{
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val msg = sharedPreference.getString(inReceivedCallsms,"")
        return msg ?: ""
    }




//    fun saveMissedCallObject(context: Context, missedCallModel: MissedCallModel) {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        val editor = sharedPreference.edit()
//        val gson = Gson()
//        val json: String = gson.toJson(missedCallModel)
//        editor.putString(missedCallSms, json)
//        editor.apply()
//    }

//    fun getMissedCallObject(context: Context):MissedCallModel? {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        val gson = Gson()
//        val json: String? = sharedPreference.getString(missedCallSms, "")
//        if(json == null ){
//            return null
//        }
//        if(json.isEmpty())
//            return null
//
//        val obj = gson.fromJson(json, MissedCallModel::class.java)
//        return obj
//    }

    fun saveContactsObject(context: Context, missedCallModel: MissedCallModel) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val gson = Gson()
        val json: String = gson.toJson(missedCallModel)
        editor.putString(saveContactsSms, json)
        editor.apply()
    }

    fun getContactsObject(context: Context):MissedCallModel? {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = sharedPreference.getString(saveContactsSms, "")
        if(json == null ){
            return null
        }
        if(json.isEmpty())
            return null

        val obj = gson.fromJson(json, MissedCallModel::class.java)
        return obj
    }
    fun saveMissContactsObject(context: Context, missedCallModel: MissedCallModel) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val gson = Gson()
        val json: String = gson.toJson(missedCallModel)
        editor.putString(missContactsSms, json)
        editor.apply()
    }

    fun getMissContactsObject(context: Context):MissedCallModel? {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = sharedPreference.getString(missContactsSms, "")
        if(json == null ){
            return null
        }
        if(json.isEmpty())
            return null

        val obj = gson.fromJson(json, MissedCallModel::class.java)
        return obj
    }

//    fun saveReceivedCallObject(context: Context, missedCallModel: MissedCallModel) {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        val editor = sharedPreference.edit()
//        val gson = Gson()
//        val json: String = gson.toJson(missedCallModel)
//        editor.putString(receivedCallsms, json)
//        editor.apply()
//    }
//
//    fun getReceivedCallObject(context: Context):MissedCallModel? {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        val gson = Gson()
//        val json: String? = sharedPreference.getString(receivedCallsms, "")
//        if(json == null ){
//            return null
//        }
//        if(json.isEmpty())
//            return null
//
//        val obj = gson.fromJson(json, MissedCallModel::class.java)
//        return obj
//    }
//
//    fun getMissedCallSms(context: Context): String {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        return sharedPreference.getString(missedCallSms, "") ?: ""
//    }


//    fun saveReceivedCallNumber(context: Context, sms: String) {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        val editor = sharedPreference.edit()
//        editor.putString(receivedCallNumber, sms)
//        editor.apply()
//    }
//    fun getReceivedCallNumber(context: Context): String {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        return sharedPreference.getString(receivedCallNumber, "") ?: ""
//    }
//
//    fun saveMissedCallNumber(context: Context, sms: String) {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        val editor = sharedPreference.edit()
//        editor.putString(missedCallNumber, sms)
//        editor.apply()
//    }
//    fun getMissedCallNumber(context: Context): String {
//        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
//        return sharedPreference.getString(missedCallNumber, "") ?: ""
//    }

    fun resetDB(){
        sharedPreferences.edit().clear().apply()
    }

    fun saveExcludeInContactList(context: Context,list: ArrayList<String>) {
    saveArrayList(context,list,excludeInContactList)
    }

    fun saveInContactList(context: Context,list: ArrayList<String>) {
        saveArrayList(context,list,inContactList)
    }


    fun getExcludeInContactList(context: Context): ArrayList<String> = getArrayList(context,excludeInContactList)
    fun getInContactList(context: Context): ArrayList<String> = getArrayList(context,inContactList)


    fun getExcludeOutContactList(context: Context): ArrayList<String> = getArrayList(context,excludeOutContactList)
    fun getOutContactList(context: Context): ArrayList<String> = getArrayList(context,outContactList)

    fun saveExcludeOutContactList(context: Context,list: ArrayList<String>) {
        saveArrayList(context,list,excludeOutContactList)
    }
    fun saveOutContactList(context: Context,list: ArrayList<String>) {
        saveArrayList(context,list,outContactList)
    }

    fun saveMessagesList(messages: ArrayList<String>) {
        saveArrayList(context,messages,messagesList)
    }
    fun getMessagesList(context: Context): ArrayList<String> = getArrayList(context,messagesList)


    fun saveArrayList(context: Context, list: ArrayList<String>, key: String?) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    fun getArrayList(context: Context,key: String?): ArrayList<String> {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = sharedPreference.getString(key, null)
        if(json == null){
            return arrayListOf()
        }
        val type: Type = object : TypeToken<java.util.ArrayList<String?>?>() {}.getType()
        return gson.fromJson(json, type)
    }

    fun saveAfterCount(context: Context, parseInt: Int) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putInt(afterCallCount,parseInt)
        editor.apply()
    }

    fun getAfterCount(context: Context) = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getInt(afterCallCount,0)


     fun saveMissCallMap(context: Context, inputMap: Map<String, Int>) {
         val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
         val editor = sharedPreference.edit()
            val jsonObject = JSONObject(inputMap)
            val jsonString = jsonObject.toString()
         editor.remove(missCallMap)
         editor.putString(missCallMap, jsonString)
         editor.apply()

    }
    fun saveReceiveCallMap(context: Context,inputMap: Map<String, Int>) {
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val jsonObject = JSONObject(inputMap)
        val jsonString = jsonObject.toString()
        editor.remove(receiveCallMap)
        editor.putString(receiveCallMap, jsonString)
        editor.apply()

    }


     fun getReceiveCallMap(context: Context): Map<String, Int> {
        val outputMap: MutableMap<String, Int> = HashMap()
         val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        try {
            if (sharedPreference != null) {
                val jsonString = sharedPreference.getString(receiveCallMap, JSONObject().toString())
                if (jsonString != null) {
                    val jsonObject = JSONObject(jsonString)
                    val keysItr = jsonObject.keys()
                    while (keysItr.hasNext()) {
                        val key = keysItr.next()
                        val value = jsonObject.getInt(key)
                        outputMap[key] = value
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return outputMap
    }

    fun getMissCallMap(context: Context): Map<String, Int> {
        val outputMap: MutableMap<String, Int> = HashMap()
        val sharedPreference = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        try {
            if (sharedPreference != null) {
                val jsonString = sharedPreference.getString(missCallMap, JSONObject().toString())
                if (jsonString != null) {
                    val jsonObject = JSONObject(jsonString)
                    val keysItr = jsonObject.keys()
                    while (keysItr.hasNext()) {
                        val key = keysItr.next()
                        val value = jsonObject.getInt(key)
                        outputMap[key] = value
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return outputMap
    }


}