package com.albertjsoft.portainerapp.data.api.service

import com.albertjsoft.portainerapp.data.api.request.LoginRequest
import com.albertjsoft.portainerapp.data.api.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by albertj on 14/10/2018.
 */
interface AuthService {

    @POST("api/auth")
    fun authUser(@Body loginRequest: LoginRequest): Observable<LoginResponse>

}