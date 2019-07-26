package com.sa.gym.model

class Admin {

    var id: String? = null
    var userName: String? = null
    var password: String? = null

    constructor() {}

    constructor(id: String, title: String, content: String) {
        this.id = id
        this.userName = title
        this.password = content
    }

    constructor(title: String, content: String) {
        this.userName = title
        this.password = content
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result["userName"] = userName!!
        result["password"] = password!!

        return result
    }
}