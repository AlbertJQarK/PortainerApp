package com.albertjsoft.portainerapp.service.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by albertj on 18/10/2018.
 */
data class User(
        @SerializedName("username") var userName: String? = null,
        @SerializedName("serverAddress") var ServerAddress: String? = null
) : Serializable