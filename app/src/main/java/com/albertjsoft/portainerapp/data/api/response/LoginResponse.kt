package com.albertjsoft.portainerapp.data.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by albertj on 18/10/2018.
 */
class LoginResponse : ApiResponse<String>() {
    @Expose
    @SerializedName("jwt")
    private val token: String? = null

    override fun getData(): String? = token
}
