package com.albertjsoft.portainerapp.data.repository

import android.arch.lifecycle.LiveData
import com.albertjsoft.portainerapp.data.api.*
import com.albertjsoft.portainerapp.data.api.request.LoginRequest

/**
 * Created by albertj on 18/10/2018.
 */
class AuthenticationRepository {

    fun login(username: String, password: String, serverAddress: String): LiveData<Resource<String?>> {
        val request = LoginRequest(username, password)
        changeApiBaseUrl(serverAddress)
        return NetworkBoundResourceBuilder.asLiveData { provideAuthenticationService().authUser(request) }
    }
}