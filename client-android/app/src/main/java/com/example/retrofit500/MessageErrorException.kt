package com.example.retrofit500

import java.io.IOException

class MessageErrorException : IOException {
    private var errorCode: String? = null

    constructor(message: String?) : super(message)
    constructor(message: String?, errorCode: String?) : super(message) {
        this.errorCode = errorCode
    }

    fun getErrorCode(): String? {
        return errorCode
    }

    constructor()
}
