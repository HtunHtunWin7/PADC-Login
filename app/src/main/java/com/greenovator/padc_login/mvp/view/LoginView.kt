package com.greenovator.padc_login.mvp.view

interface LoginView : BaseView {
    fun facebookLoginAction()
    fun googleLoginAction()
    fun loginWithInputData(email:String,password:String)
    fun loginSuccess()
}