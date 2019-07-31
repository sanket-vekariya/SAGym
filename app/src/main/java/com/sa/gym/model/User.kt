package com.sa.gym.model

import java.util.Date

class User {

    var id: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var active: String? = null
    var email: String? = null

    var mobile: Long? = null
    var inTime: String? = null
    var outTime: String? = null

    var address: String? = null
    var birthDate: String? = null
    var height: Float? = null
    var weight: Float? = null

    var membershipType: String? = null
    var amount: Int? = null
    var paymentStatus: Boolean? = null
    var addedBy: String? = null

    constructor() {}

    constructor(id: String, firstName: String, lastName: String, active: String, email: String) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.active = active
        this.email = email
    }

    constructor(firstName: String, lastName: String, active: String, email: String) {
        this.firstName = firstName
        this.lastName = lastName
        this.active = active
        this.email = email
    }

    constructor(
        id: String,
        firstName: String,
        lastName: String,
        active: String,
        email: String,

        mobile: Long,
        inTime: String,
        outTime: String,

        address: String,
        birthDate: String,
        height: Float,
        weight: Float,

        membershipType: String,
        amount: Int,
        paymentStatus: Boolean,
        addedBy: String
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.active = active
        this.email = email

        this.mobile = mobile
        this.inTime = inTime
        this.outTime = outTime

        this.address = address
        this.birthDate = birthDate
        this.height = height
        this.weight = weight

        this.membershipType = membershipType
        this.amount = amount
        this.paymentStatus = paymentStatus
        this.addedBy = addedBy

    }

    constructor(
        firstName: String,
        lastName: String,
        active: String,
        email: String,

        mobile: Long,
        inTime: String,
        outTime: String,

        address: String,
        birthDate: String,
        height: Float,
        weight: Float,

        membershipType: String,
        amount: Int,
        paymentStatus: Boolean,
        addedBy: String
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.active = active
        this.email = email

        this.mobile = mobile
        this.inTime = inTime
        this.outTime = outTime

        this.address = address
        this.birthDate = birthDate
        this.height = height
        this.weight = weight

        this.membershipType = membershipType
        this.amount = amount
        this.paymentStatus = paymentStatus
        this.addedBy = addedBy

    }

    //fields of fire store with values
    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("firstName", firstName!!)
        result.put("lastName", lastName!!)
        result.put("active", active!!)
        result.put("email", email!!)

        result.put("mobile", mobile!!)
        result.put("inTime", inTime!!)
        result.put("outTime", outTime!!)

        result.put("address", address!!)
        result.put("birthDate", birthDate!!)
        result.put("height", height!!)
        result.put("weight", weight!!)

        result.put("membershipType", membershipType!!)
        result.put("amount", amount!!)
        result.put("paymentStatus", paymentStatus!!)
        result.put("addedBy", addedBy!!)

        return result
    }
}