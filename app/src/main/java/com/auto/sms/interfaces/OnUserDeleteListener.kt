package com.auto.sms.interfaces

import com.auto.sms.models.UserModel

interface OnUserDeleteListener {
    fun onItemCallback( userModel: UserModel, type:Int)
}