package com.albertjsoft.portainerapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.view.View

/**
 * Created by albertj on 15/10/2018.
 */
class WelcomeActivity  : AppCompatActivity(), View.OnClickListener {

    private var mServerAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        findViewById<View>(R.id.bt_check).setOnClickListener(this)

        val actionBar = supportActionBar
        actionBar!!.hide()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.bt_check){
            mServerAddress = findViewById<AppCompatEditText>(R.id.et_server_address).
                    text.toString()
        }
    }
}