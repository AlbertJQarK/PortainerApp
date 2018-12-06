package com.albertjsoft.portainerapp.view.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.albertjsoft.portainerapp.R
import com.albertjsoft.portainerapp.data.api.Status.*
import com.albertjsoft.portainerapp.util.setVisible
import com.albertjsoft.portainerapp.viewodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by albertj on 18/10/2018.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var mServerAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val bundle=intent.extras
        if(bundle!=null) {
            mServerAddress = bundle.getString("serverAddress")
        }

        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    private fun initViews() {
        bt_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val username = et_username.text.toString()
        val password = et_password.text.toString()
        viewModel.login(username, password, mServerAddress).observe(this, Observer { loginResult ->
            when (loginResult?.status) {
                SUCCESS -> {
                    hideProgress()
                    startMainActivity()
                }
                ERROR -> {
                    hideProgress()
                }
                LOADING -> showProgress()
            }
        })
    }

    private fun showProgress() {
        progress_bar.setVisible(true)
        et_username.isEnabled = false
        et_password.isEnabled = false
    }

    private fun hideProgress() {
        progress_bar.setVisible(false)
        et_username.isEnabled = true
        et_password.isEnabled = true
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
