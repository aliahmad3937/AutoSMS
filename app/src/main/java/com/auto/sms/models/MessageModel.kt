package com.auto.sms.models

class MessageModel(
    var message_id:String="", /// auto generated id
    var user_id:String="",   //// user id who add message
    var message_type:String="",  //// missed or received
    var message_text:String=""  //// message added by user
    )