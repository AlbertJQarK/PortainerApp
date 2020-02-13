package com.albertjsoft.portainerapp.session

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.albertjsoft.portainerapp.data.api.Resource
import com.albertjsoft.portainerapp.data.api.Status
import com.albertjsoft.portainerapp.data.repository.AuthenticationRepository
import com.albertjsoft.portainerapp.data.repository.provideAuthenticationRepository
import com.albertjsoft.portainerapp.data.database.identity.User

/**
 * Created by albertj on 18/10/2018.
 */
class UserSessionInteractor(
        private val authenticationRepository: AuthenticationRepository = provideAuthenticationRepository()
){

    fun login(username: String, password: String, serverAddress: String): LiveData<Resource<out Any?>> {
        val result = MediatorLiveData<Resource<out Any?>>()
        result.addSource(authenticationRepository.login(username, password, serverAddress)) { loginResult ->
            if (loginResult?.status == Status.SUCCESS) {
                val token = loginResult.data
                val user = User(username, serverAddress)
                saveUserSession(token, user, password)
            }
            result.value = loginResult
        }
        return result
    }

    fun saveUserSession(token: String?, user: User?, password: String?) {
        if (token != null && user != null && password != null) {
            val sessionAuthenticator = SessionAuthenticator(token, user, password)
            SessionAuthenticator.saveInstance(sessionAuthenticator)
        }
    }
}
