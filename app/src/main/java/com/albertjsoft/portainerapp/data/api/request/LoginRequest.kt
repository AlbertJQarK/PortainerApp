package com.albertjsoft.portainerapp.data.api.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by albertj on 18/10/2018.
 */

class LoginRequest(
        @SerializedName("username") val username: String? = null,
        @SerializedName("password") val password: String? = null
) : Serializable