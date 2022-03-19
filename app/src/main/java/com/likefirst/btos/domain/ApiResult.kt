package com.likefirst.btos.domain

import java.net.URLDecoder

data class ApiResult<out T>(val status: Status, val code: String?, val message: String?, val data: T?, val exception: Exception?) {

    enum class Status {
        SUCCESS,
        API_ERROR,
        NETWORK_ERROR,
        LOADING
    }

    companion object {
        fun <T> success(code: Int, data: T?): ApiResult<T> {
            return ApiResult(Status.SUCCESS, code.toString(), "", data, null)
        }

        fun <T> error(code: Int, message: String): ApiResult<T> {
            return ApiResult(Status.API_ERROR, URLDecoder.decode(code.toString(), "UTF-8"), URLDecoder.decode(message, "UTF-8"), null, null)
        }

        fun <T> error(exception: Exception?): ApiResult<T> {
            return ApiResult(Status.NETWORK_ERROR, null, null, null, exception)
        }

        fun <T> loading(): ApiResult<T> {
            return ApiResult(Status.LOADING, null, null, null, null)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, code=$code, message=$message, data=$data, error=$exception)"
    }
}