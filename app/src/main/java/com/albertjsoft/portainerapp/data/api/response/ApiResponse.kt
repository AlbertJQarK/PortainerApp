package com.albertjsoft.portainerapp.data.api.response

import com.google.gson.annotations.SerializedName

/**
 * Created by albertj on 18/10/2018.
 */
abstract class ApiResponse<out T>(
        @SerializedName("status") val status: Status? = null,
        @SerializedName("message") val message: String? = null,
        @SerializedName("err") val errorMessage: String? = null) {

    enum class Status {
        @SerializedName("success") SUCCESS,
        @SerializedName("error") ERROR
    }

    abstract fun getData(): T?
}
