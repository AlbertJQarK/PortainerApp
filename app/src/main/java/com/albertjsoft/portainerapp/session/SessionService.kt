package com.albertjsoft.portainerapp.session

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

import com.albertjsoft.portainerapp.app.PortainerApp

/**
 * Created by albertj on 18/10/2018.
 */
class SessionService : Service(){

    init {
        val session = SessionAuthenticator.loadInstance(appContext!!)

        if (session != null) {
            startUserSession(session)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        when (intent.action == android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT) {
            //TODO: bind authenticator
        }
        return null
    }

    fun startUserSession(sessionInstance: SessionAuthenticator) {
        session = sessionInstance
        saveCurrentSession()
    }

    private fun saveCurrentSession() {
        SessionAuthenticator.saveInstance(session, PortainerApp.instance)
    }

    companion object {
        var instance: SessionService? = null
            private set

        var session: SessionAuthenticator? = null
            private set

        var appContext: Context? = null
            private set

        fun init(applicationContext: Context) {
            this.appContext = applicationContext
            instance = SessionService()
        }
    }
}
