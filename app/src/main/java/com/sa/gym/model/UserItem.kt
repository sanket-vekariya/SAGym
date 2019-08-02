package com.sa.gym.model

import java.io.Serializable


class UserItem(
    var id: String,
    var firstName: String,
    var lastName: String,
    var active: Boolean,
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

    constructor() : this("", "firstName", "lastName", true, "first.last@gmail.com", 9435266443, "12:00","13:00","Ahmedabad","08/08/1998",5f,60f,"annual",8000,false,"admin")
}
