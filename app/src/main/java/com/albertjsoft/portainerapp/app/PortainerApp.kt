package com.albertjsoft.portainerapp.app

import android.app.Application

/**
 * Created by albertj on 18/10/2018.
 */
class PortainerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

    companion object {
        lateinit var instance: PortainerApp
            private set
    }
}
