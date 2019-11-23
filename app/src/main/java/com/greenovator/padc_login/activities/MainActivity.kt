package com.greenovator.padc_login.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.greenovator.padc_login.R
import com.greenovator.padc_login.mvp.presenter.MainPresenter
import com.greenovator.padc_login.mvp.view.MainView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), MainView {
    private lateinit var mPresenter: MainPresenter
    override fun onClickLogoutButton() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    override fun showUserProfile() {
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            profile_username.text = name
            profile_email.text = email
            Glide.with(this)
                .load(photoUrl)
                .into(profile_imageView)

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
    }

    override fun checkLoginUser() {
        if (user != null) {
            mPresenter.showUserProfile()
        } else {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter = MainPresenter()
        mPresenter.initPresenter(this)

        mPresenter.checkLogin()
        btn_logout.setOnClickListener {
            mPresenter.logoutFromFirebase()
        }
        //setSupportActionBar(toolbar)

    }
}
