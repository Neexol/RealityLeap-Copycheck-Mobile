package ru.rtuitlab.copycheck.server.handle

import ru.rtuitlab.copycheck.server.handle.Status.*

data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val message: String?
) {
    companion object {
        fun <T> success(data: T) = Resource(SUCCESS, data, null)
        fun error(msg: String) = Resource(ERROR, null, msg)
        fun loading() = Resource(LOADING, null, null)
    }
}