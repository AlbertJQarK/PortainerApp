package com.albertjsoft.portainerapp.session

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerFuture
import android.content.Intent
import android.os.Bundle
import com.albertjsoft.portainerapp.PortainerApp
import com.albertjsoft.portainerapp.service.model.User
import com.albertjsoft.portainerapp.view.ui.WelcomeActivity


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
        private const val ACCOUNT_TYPE = "com.albertjsoft.portainerapp.auth_portainerapp"
        private const val AUTH_TYPE = "PortainerApp:AuthType"
        private const val ACCOUNT_NAME = "PortainerApp"

        private var mAccountManager: AccountManager? = null
        private var savedToken: String? = null

        fun loadInstance(): SessionAuthenticator? {
            mAccountManager =  AccountManager.get(PortainerApp.instance)

            var sessionAuthenticator: SessionAuthenticator? = null
            var lastSavedToken: String? = null
            var lastSavedUsername: String? = null
            var lastSavedServerAddress: String? = null

            val account = findAccount(ACCOUNT_NAME, ACCOUNT_TYPE)
            if(account != null) {
                // Obtain account token
                val threat = Thread(Runnable {
                    val response: AccountManagerFuture<Bundle>  = mAccountManager!!.getAuthToken(account, AUTH_TYPE, false,  null, null)
                    val bundle: Bundle = response.result
                    savedToken =  bundle.getString("authtoken")
                })

                // spawn thread
                threat.start()

                // wait for thread to finish
                threat.join()

                // Restore session parameters
                lastSavedToken = savedToken
                lastSavedUsername = mAccountManager!!.getUserData(account, "user")
                lastSavedServerAddress = mAccountManager!!.getUserData(account, "serverAddress")
            }

            if (lastSavedToken != null && lastSavedUsername != null && lastSavedServerAddress != null) {
                val user = User()
                user.userName = lastSavedUsername
                user.ServerAddress = lastSavedServerAddress

                // Set Authenticator
                sessionAuthenticator = sessionAuthenticatorProvider(lastSavedToken, user, account!!)
            }

            return sessionAuthenticator
        }

        private fun sessionAuthenticatorProvider(token: String, user: User, account: Account) : SessionAuthenticator =
                SessionAuthenticator(token, user, mAccountManager!!.getPassword(account))

        fun saveInstance(session: SessionAuthenticator?) {
            mAccountManager = AccountManager.get(PortainerApp.instance)

            val username: String? = session!!.user.userName
            val token: String? = session.token
            val password: String? = session.password

            if (!username.isNullOrEmpty()) {
                // Setup account configuration bundle
                val data = Bundle()
                data.putString(AccountManager.KEY_ACCOUNT_NAME, ACCOUNT_NAME)
                data.putString(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE)
                data.putString(AccountManager.KEY_AUTHTOKEN, token)
                data.putString(PARAM_PASS, password)

                // Extra data about the user
                val userData = Bundle()
                userData.putString("serverAddress", session.user.ServerAddress)
                userData.putString("user", username)
                data.putBundle(AccountManager.KEY_USERDATA, userData)

                // Put data on intent
                val intent = Intent()
                intent.putExtras(data)

                // Create the new account
                val account = Account(ACCOUNT_NAME, ACCOUNT_TYPE)

                addAccount(account, password, userData, data, intent, token)
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

        private fun addAccount(account: Account, password: String?, userData: Bundle, data: Bundle, res: Intent, token: String?){
            if (mAccountManager!!.addAccountExplicitly(account, password, userData)) {
                mAccountManager!!.setAuthToken(account, AUTH_TYPE, token)
                WelcomeActivity.instance.setAccountAuthenticator(data,res)
            }
        }
    }

}

