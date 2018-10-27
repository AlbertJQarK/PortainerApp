package com.albertjsoft.portainerapp.session

import android.content.Context

import com.albertjsoft.portainerapp.service.model.User

import android.accounts.AccountManager
import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import com.albertjsoft.portainerapp.view.ui.WelcomeActivity
import android.accounts.AccountManagerFuture

/**
 * Created by albertj on 18/10/2018.
 */

class SessionAuthenticator(token: String, private var user: User, private var password: String) {

    var token: String? = null
        private set

    init {
        this.token = token
    }

    companion object {

        private const val PARAM_PASS = "USER_PASS"

        private var mAccountManager: AccountManager? = null
        var savedToken: String? = null

        fun loadInstance(context: Context): SessionAuthenticator? {
            mAccountManager =  AccountManager.get(context)

            var session: SessionAuthenticator? = null
            var lastSavedToken: String? = null
            var lastSavedUsername: String? = null
            var lastSavedServerAddress: String? = null

            val account = findAccount(WelcomeActivity.ACCOUNT_NAME, WelcomeActivity.ACCOUNT_TYPE)
            if(account != null) {
                val threat = Thread(Runnable {
                    val resultFuture: AccountManagerFuture<Bundle>  = mAccountManager!!.getAuthToken(account, WelcomeActivity.AUTH_TYPE, null, context as WelcomeActivity, null, null)
                    val bundle: Bundle = resultFuture.result
                    savedToken =  bundle.getString("authtoken")
                })

                // spawn thread
                threat.start()

                // wait for thread to finish
                threat.join()

                lastSavedToken = savedToken
                lastSavedUsername = mAccountManager!!.getUserData(account, "user")
                lastSavedServerAddress = mAccountManager!!.getUserData(account, "serverAddress")
            }

            if (lastSavedToken != null && lastSavedUsername != null && lastSavedServerAddress != null) {
                val user = User()
                user.userName = lastSavedUsername
                user.ServerAddress = lastSavedServerAddress

                session = SessionAuthenticator(lastSavedToken, user, mAccountManager!!.getPassword(account))
            }

            return session
        }

        fun saveInstance(session: SessionAuthenticator?, context: Context) {
            mAccountManager = AccountManager.get(context)

            val username: String? = session!!.user.userName
            val token: String? = session.token
            val password: String? = session.password

            if (!username.isNullOrEmpty()) {
                val data = Bundle()
                data.putString(AccountManager.KEY_ACCOUNT_NAME, WelcomeActivity.ACCOUNT_NAME)
                data.putString(AccountManager.KEY_ACCOUNT_TYPE, WelcomeActivity.ACCOUNT_TYPE)
                data.putString(AccountManager.KEY_AUTHTOKEN, token)
                data.putString(PARAM_PASS, password)

                // Extra data about the user and passed back an intent to the Android Authenticator
                val userData = Bundle()
                userData.putString("serverAddress", session.user.ServerAddress)
                userData.putString("user", username)
                data.putBundle(AccountManager.KEY_USERDATA, userData)
                val res = Intent()
                res.putExtras(data)

                //Create the new account
                val account = Account(WelcomeActivity.ACCOUNT_NAME, WelcomeActivity.ACCOUNT_TYPE)

                //Add the account to the Android System
                if (mAccountManager!!.addAccountExplicitly(account, password, userData)) {
                    mAccountManager!!.setAuthToken(account, WelcomeActivity.AUTH_TYPE, token)
                    WelcomeActivity.instance.setAccountAuthenticator(data,res)
                }
            }
        }

        private fun findAccount(accountName: String, accountType: String): Account? {
            var appAccount: Account? = null
            for (account in mAccountManager!!.accounts) {
                if (account.name == accountName)
                    if(account.type == accountType) {
                        appAccount = account
                    }
            }
            return appAccount
        }

    }
}
