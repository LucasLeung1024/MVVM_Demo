package com.kevin.mvvm.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.kevin.mvvm.BR

//允许数据可观察
//https://developer.android.com/topic/libraries/data-binding/observability?hl=zh-cn

class User() : BaseObservable() {

    constructor(account: String, pwd: String) : this() {
        this.account = account
        this.pwd = pwd
    }

    @get:Bindable
    var account: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.account)//只通知改变的参数
        }

    @get:Bindable
    var pwd: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.pwd)//只通知改变的参数
        }

}
