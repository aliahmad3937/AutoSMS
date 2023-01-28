package com.auto.sms.models

import java.io.Serializable

class MissedCallModel (
    var sms: String ="",
    var numbers: String ="",
    var days: Int = -1,
    var count: Int = 0,
    var afterCount: Int = 0,
    var hours: Int = -1,
    var minute: Int = -1
        ) : Serializable {

}
