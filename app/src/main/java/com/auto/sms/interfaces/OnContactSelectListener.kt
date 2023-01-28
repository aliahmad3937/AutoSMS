package com.auto.sms.interfaces

import com.auto.sms.models.CallLogModel
import com.auto.sms.models.UserModel

interface OnContactSelectListener {
    fun onItemCallback( callLogModel: CallLogModel)

}