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


private const val DEFAULT_TIMEOUT = 30
private var apiBaseUrl = "http://localhost:9000"
private var builder =  Retrofit.Builder().baseUrl(apiBaseUrl)

internal fun provideAuthenticationService() = createService(AuthService::class.java)

fun <T> createService(service: Class<T>): T = provideRetrofit().create(service)

private fun provideRetrofit(): Retrofit = builder.build()

private fun provideOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder().apply {
        connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
    }.build()

private fun provideGson(): Gson =
            GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

fun changeApiBaseUrl(newApiBaseUrl: String) {
    apiBaseUrl = newApiBaseUrl

    builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http:// $apiBaseUrl")
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(provideOkHttpClient())
}