package com.albertjsoft.portainerapp

import android.app.Application

/**
 * Created by albertj on 18/10/2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
