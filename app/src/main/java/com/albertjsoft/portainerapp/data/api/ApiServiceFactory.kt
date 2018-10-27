package com.albertjsoft.portainerapp.data.api

import com.albertjsoft.portainerapp.data.api.service.AuthService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by albertj on 18/10/2018.
 */
internal fun provideAuthenticationService() = createService(AuthService::class.java)

private const val DEFAULT_TIMEOUT = 30
private var apiBaseUrl = "http://localhost:9000"


private var builder = Retrofit.Builder().baseUrl(apiBaseUrl)

fun <T> createService(service: Class<T>): T {
    return provideRetrofit().create(service)
}

private fun provideRetrofit(): Retrofit {
    return builder.build()
}

private fun provideOkHttpClient(): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    httpClient.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
    httpClient.readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
    httpClient.writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)

    return httpClient.build()
}

private fun provideGson(): Gson {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

    return gsonBuilder.create()
}

fun changeApiBaseUrl(newApiBaseUrl: String) {
    apiBaseUrl = newApiBaseUrl

    builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://" + apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(provideOkHttpClient())
}