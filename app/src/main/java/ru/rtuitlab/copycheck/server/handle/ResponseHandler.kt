package ru.rtuitlab.copycheck.server.handle

import android.util.Log
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResponseHandler {
    fun <T : Any> handleSuccess(data: T) = Resource.success(data)

    fun <T : Any> handleException(e: Exception): Resource<T> {
        Log.e(javaClass.name, e.toString())
        return Resource.error(
            getErrorMessage(
                when (e) {
                    is HttpException -> e.code()
                    is SocketTimeoutException -> ErrorCodes.SocketTimeOut.code
                    is UnknownHostException -> ErrorCodes.UnknownHost.code
                    else -> Int.MAX_VALUE
                }
            )
        )
    }

    private fun getErrorMessage(code: Int) = when (code) {
        ErrorCodes.UnknownHost.code -> "Check your internet connection"
        ErrorCodes.SocketTimeOut.code -> "Timeout"
        401 -> "Unauthorized"
        404 -> "Not found"
        in 400..499 -> "Check entered data"
        in 500..599 -> "Error with connecting to server. Code: $code"
        else -> "Something went wrong"
    }

    private enum class ErrorCodes(val code: Int) {
        SocketTimeOut(-1),
        UnknownHost(-2)
    }
}