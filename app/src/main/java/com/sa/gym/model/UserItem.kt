package com.sa.gym.model

import java.io.Serializable


class UserItem(
    var id: String,
    var firstName: String,
    var lastName: String,
    var active: String,
    var email: String,

    var mobile: Long,
    var inTime: String,
    var outTime: String,

    var addresss: String,
    var birthDate: String,
    var height: Float,
    var weight: Float,

    var membershipType: String,
    var amount: Int,
    var paymentStatus: Boolean,
    var addedBy: String
) : Serializable {

    constructor() : this("", "", "", "", "", 0, "","","","",0.0f,0.0f,"",0,false,"")
}
