package com.albertjsoft.portainerapp.session

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Created by albertj on 18/10/2018.
 */
class SessionService : Service(){

    var authenticator: SessionAuthenticator? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val authenticator = SessionAuthenticator.loadInstance()

        if (authenticator != null) {
            saveUserSession(authenticator)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        if (intent.action == android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT)
            if (authenticator == null) {
                authenticator = SessionAuthenticator.loadInstance()
            }
        return iBinder
    }

    private val iBinder = LocalIBinder()

    inner class LocalIBinder : Binder() {
        fun getService(): SessionService = this@SessionService
    }

    private fun saveUserSession(sessionInstance: SessionAuthenticator) {
        authenticator = sessionInstance
        saveCurrentSession()
    }

    private fun saveCurrentSession() {
        SessionAuthenticator.saveInstance(authenticator)
    }
}
