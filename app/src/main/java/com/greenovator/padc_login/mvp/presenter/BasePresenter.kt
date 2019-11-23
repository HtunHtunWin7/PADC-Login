package com.greenovator.padc_login.mvp.presenter

import androidx.lifecycle.ViewModel
import com.greenovator.padc_login.mvp.view.BaseView

abstract class BasePresenter<T: BaseView>:ViewModel() {
    protected lateinit var mView: T

    open fun initPresenter(view: T){
        this.mView  = view
    }
}