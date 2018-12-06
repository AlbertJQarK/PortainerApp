package com.albertjsoft.portainerapp.view.ui

import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by albertj on 23/10/2018.
 */
open class AccountAuthenticatorAppCompatActivity : AppCompatActivity() {

    private var mAccountAuthenticatorResponse: AccountAuthenticatorResponse? = null
    private var mResultBundle: Bundle? = null

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        mAccountAuthenticatorResponse = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse!!.onRequestContinued()
        }
    }

    fun setAccountAuthenticatorResult(result: Bundle) {
        mResultBundle = result
    }

}