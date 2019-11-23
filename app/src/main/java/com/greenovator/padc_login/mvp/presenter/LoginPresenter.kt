package com.greenovator.padc_login.mvp.presenter

import com.greenovator.padc_login.mvp.view.LoginView

class LoginPresenter : BasePresenter<LoginView>() {
    fun loginWithFacebook() {
        mView.facebookLoginAction()
    }

    fun loginWithGoogle() {
        mView.googleLoginAction()
    }

    fun loginWtihInputData(email:String, password: String) {
        mView.loginWithInputData(email,password)
    }
    fun loginSuccess(){
        mView.loginSuccess()
    }
}