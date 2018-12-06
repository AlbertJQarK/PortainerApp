package com.albertjsoft.portainerapp.viewodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.albertjsoft.portainerapp.data.api.Resource
import com.albertjsoft.portainerapp.data.api.Status
import com.albertjsoft.portainerapp.session.UserSessionInteractor

/**
 * Created by albertj on 18/10/2018.
 */
class LoginViewModel(
        private val userSessionInteractor: UserSessionInteractor = UserSessionInteractor()
) : ViewModel() {

    fun login(username: String?, password: String?, severAddress: String?): LiveData<Resource<out Any?>> {
        val validateResult = Resource.success(null)
        return when (validateResult.status) {
            Status.ERROR -> MutableLiveData<Resource<out Any?>>().apply { value = validateResult }
            else -> userSessionInteractor.login(username!!, password!!, severAddress!!)
        }
    }
}