package com.kevin.mvvm.db.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.kevin.mvvm.BR

@Entity(tableName = "user")

class User() : BaseObservable(){

    @PrimaryKey
    var uid = 0
    private var account: String? = null
    private var pwd: String? = null

    @Ignore
    private var confirmPwd: String? = null
    private var nickname: String? = null
    private var introduction: String? = null

    @Bindable
    fun getAccount(): String? {
        return account
    }

    fun setAccount(account: String?) {
        this.account = account
        notifyPropertyChanged(BR.account)
    }

    @Bindable
    fun getPwd(): String? {
        return pwd
    }

    fun setPwd(pwd: String?) {
        this.pwd = pwd
        notifyPropertyChanged(BR.pwd)
    }

    @Bindable
    fun getConfirmPwd(): String? {
        return confirmPwd
    }

    fun setConfirmPwd(confirmPwd: String?) {
        this.confirmPwd = confirmPwd
        notifyPropertyChanged(BR.confirmPwd)
    }

    @Bindable
    fun getNickname(): String? {
        return nickname
    }

    fun setNickname(nickname: String?) {
        this.nickname = nickname
        notifyPropertyChanged(BR.nickname)
    }

    @Bindable
    fun getIntroduction(): String? {
        return introduction
    }

    fun setIntroduction(introduction: String?) {
        this.introduction = introduction
        notifyPropertyChanged(BR.introduction)
    }


    @Ignore
    constructor(
        uid: Int,
        account: String?,
        pwd: String?,
        confirmPwd: String?,
        nickname: String?,
        introduction: String?
    ): this()  {
        this.uid = uid
        this.account = account
        this.pwd = pwd
        this.confirmPwd = confirmPwd
        this.nickname = nickname
        this.introduction = introduction
    }
}

