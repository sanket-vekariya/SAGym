package com.sa.gym.model

class User {

    var id: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var active: String? = null
    var email: String? = null


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

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("firstName", firstName!!)
        result.put("lastName", lastName!!)
        result.put("active", active!!)
        result.put("email", email!!)

        return result
    }
}