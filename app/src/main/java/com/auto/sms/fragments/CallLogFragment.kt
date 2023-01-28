package com.auto.sms.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.auto.sms.R
import com.auto.sms.adapters.CallLogAdapter
import com.auto.sms.databinding.FragmentCallLogBinding
import com.auto.sms.interfaces.OnContactSelectListener
import com.auto.sms.models.CallLogModel
import com.auto.sms.models.MissedCallModel
import com.auto.sms.utils.SharePrefs.Companion.getInstance
import java.lang.Long
import java.util.*
import kotlin.Any
import kotlin.Int
import kotlin.String
import kotlin.toString


class CallLogFragment : Fragment(), OnContactSelectListener {

    private val TAG = "CallLogFragment"
    private lateinit var mAdapter: CallLogAdapter
    var call_list = ArrayList<CallLogModel>()

    private lateinit var mBinding: FragmentCallLogBinding
    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mBinding = FragmentCallLogBinding.inflate(inflater, container, false)


        getCallDetails()
        mBinding.datePicker1.setIs24HourView(true)

        val saveContactModel = getInstance(requireContext())!!.getMissContactsObject(requireContext())
        if (saveContactModel != null) {
            Log.i(
                "TAG65",
                "hours :" + saveContactModel.hours + "minutes :" + saveContactModel.minute
            )
            mBinding.datePicker1.hour = saveContactModel.hours
            mBinding.datePicker1.minute = saveContactModel.minute
            mBinding.messageEdittext.setText(saveContactModel.sms)
            mBinding.inputDaysLayout.editText?.setText(saveContactModel.days.toString())
        }

        mBinding.backArrowImageview.setOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.btnMesagSend.setOnClickListener {

            if (mBinding.messageEdittext.text.toString().isEmpty()) {
                mBinding.messageEdittext.error = getString(R.string.required)
            }
            else {
                if (mBinding.inputDaysLayout.editText?.text.toString().isEmpty()) {
                    mBinding.inputDaysLayout.error = getString(R.string.required)
                } else {
                    var isContactsSelect = false

                    for (i in call_list.indices) {
                        if (call_list[i].isCheck) {
                            isContactsSelect = true
                            break
                        }
                    }

                    if (isContactsSelect) {
                        val list = ArrayList<String>()

                        for (i in call_list.indices) {
                            if (call_list[i].isCheck) {
                                list.add(call_list[i].user_nmbr)
                            }
                        }

                        val nmbrs_string: String = arrangeContactList(list)!!
                      //  val valueOf: String = mBinding.messageEdittext.text.toString().trim()

                        Toast.makeText(requireContext(), getString(R.string.save), Toast.LENGTH_LONG)
                            .show()

                        getInstance(requireContext())!!.saveMissContactsObject(
                            requireContext(),
                            MissedCallModel(
                                mBinding.messageEdittext.text.toString().trim(),
                                nmbrs_string,
                                mBinding.inputDaysLayout.editText?.text.toString().trim().toInt(),
                                0,
                                mBinding.datePicker1.hour,
                                mBinding.datePicker1.minute
                            )
                        )
                        findNavController().navigateUp()
//                        composeSMSMessage(valueOf, nmbrs_string)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.plse_select),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

        }


        return mBinding.root
    }

    private fun setUpRecyclerview() {


        Log.i(TAG, "setUpRecyclerview: listSize:" + call_list.size)
        val seen: HashSet<Any> = HashSet()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            call_list.removeIf { e -> !seen.add(e.user_nmbr) }
        }
        Log.i(TAG, "setUpRecyclerview: listSize:after:" + call_list.size)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        mBinding.callLogRecyclerview.layoutManager = layoutManager
        mAdapter = CallLogAdapter(call_list, requireContext(), this)
        mBinding.callLogRecyclerview.adapter = mAdapter
    }


    private fun getCallDetails(): String? {
        if(call_list.isEmpty()) {
            val sb = StringBuffer()
            val managedCursor: Cursor = requireActivity().managedQuery(
                CallLog.Calls.CONTENT_URI, null,
                null, null, null
            )
            val number: Int = managedCursor.getColumnIndex(CallLog.Calls.NUMBER)
            val type: Int = managedCursor.getColumnIndex(CallLog.Calls.TYPE)
            val date: Int = managedCursor.getColumnIndex(CallLog.Calls.DATE)
            val duration: Int = managedCursor.getColumnIndex(CallLog.Calls.DURATION)
            sb.append("Call Details :")
            while (managedCursor.moveToNext()) {
                val phNumber: String = managedCursor.getString(number)
                val callType: String = managedCursor.getString(type)
                val callDate: String = managedCursor.getString(date)
                val callDayTime = Date(Long.valueOf(callDate))
                val callDuration: String = managedCursor.getString(duration)
                var dir: String? = null
                val dircode = callType.toInt()
                when (dircode) {
                    CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
                }

                val callLogModel = CallLogModel(phNumber, 0, false)

                Log.i(
                    TAG,
                    "getCallDetails: isContain:" + call_list.contains(callLogModel) + "-->nmbr:" + callLogModel.user_nmbr
                )

                call_list.add(callLogModel)
                sb.append(
                    """
Phone Number:--- $phNumber 
Call Type:--- $dir 
Call Date:--- $callDayTime 
Call duration in sec :--- $callDuration"""
                )
                sb.append("\n----------------------------------")
            }


            managedCursor.close()
            setUpRecyclerview()
            Log.i(TAG, "getCallDetails: details:" + call_list.size)

            return sb.toString()
        }else{
            return null
        }
    }

    override fun onItemCallback(callLogModel: CallLogModel) {


        for (i in call_list.indices) {
            if (call_list[i].user_nmbr == callLogModel.user_nmbr) {
                call_list[i] = callLogModel
                Log.i(TAG, "onItemCallback: callLogModel:nmbr:"+callLogModel.user_nmbr+"-->days:"+callLogModel.days)
            }
        }
    }

    fun composeSMSMessage(message: String?, str2: String?) {
        val intent = Intent("android.intent.action.SENDTO")
        val sb = StringBuilder()
        sb.append("smsto:")
        sb.append(str2)
        intent.data = Uri.parse(sb.toString())
        intent.putExtra("sms_body", message)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    fun arrangeContactList(arrayList: ArrayList<String>): String? {
        var sb = java.lang.StringBuilder()
        for (i in arrayList.indices) {
            if (i == 0) {
                sb = java.lang.StringBuilder(arrayList[i])
            } else {
                sb.append(",")
                sb.append(arrayList[i])
            }
        }
        return sb.toString()
    }

}