package com.example.githubfavourits

import android.app.Application
import com.example.githubfavourits.ui.base.BasePresenter

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        BasePresenter.init()
    }

}