package com.greenovator.padc_login.mvp.presenter

import com.greenovator.padc_login.mvp.view.MainView

class MainPresenter :BasePresenter<MainView>(){
    fun checkLogin(){
        mView.checkLoginUser()
    }
    fun logoutFromFirebase(){
        mView.onClickLogoutButton()
    }
    fun showUserProfile(){
        mView.showUserProfile()
    }
}