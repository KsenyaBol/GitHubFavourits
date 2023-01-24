package com.example.data

import com.omega_r.base.errors.ErrorHandler
import com.squareup.moshi.Moshi

class AppErrorHandler(private val moshi: Moshi) : ErrorHandler() {
}

inline operator fun <R> ErrorHandler.invoke(block: () -> R): R {
    try {
        return block()
    } catch (e: Throwable) {
        throw handleThrowable(e)
    }
}