package com.albertjsoft.portainerapp.view.ui

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.AppCompatEditText
import android.view.View
import com.albertjsoft.portainerapp.R

import com.albertjsoft.portainerapp.session.SessionAuthenticator
import com.albertjsoft.portainerapp.session.SessionService

/**
 * Created by albertj on 15/10/2018.
 */
class WelcomeActivity:  AccountAuthenticatorAppCompatActivity(), View.OnClickListener {

    private var mServerAddress: String? = null
    private var mAccountManager: AccountManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        findViewById<View>(R.id.bt_check).setOnClickListener(this)

        mAccountManager = AccountManager.get(this)
        SessionService.init(this)
        instance=this

        val actionBar = supportActionBar
        actionBar!!.hide()

        startMainActivityIfActiveSession()
    }

    private fun startMainActivityIfActiveSession() {
        val session: SessionAuthenticator? = SessionService.session
        if (session != null) {
            startMainActivity()
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.bt_check){
            mServerAddress = findViewById<AppCompatEditText>(R.id.et_server_address).
                    text.toString()
            startLoginActivity()

        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private  fun startLoginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("serverAddress", mServerAddress)
        startActivity(intent)
    }

    fun setAccountAuthenticator(data: Bundle, res: Intent){
        this.setAccountAuthenticatorResult(data)
        this.setResult(Activity.RESULT_OK, res)
    }

    companion object {

        lateinit var instance: WelcomeActivity
            private set

        const val ACCOUNT_TYPE = "com.albertjsoft.portainerapp.auth_portainerapp"
        const val AUTH_TYPE = "PortainerApp:AuthType"
        const val ACCOUNT_NAME = "PortainerApp"
    }
}