package com.example.githubfavourits.ui.base

import com.example.domain.di.Injector
import com.example.githubfavourits.R
import com.example.githubfavourits.di.AppInjector
import com.omega_r.base.mvp.presenters.OmegaPresenter
import com.omega_r.libs.omegatypes.Text

open class BasePresenter<View : BaseView> : OmegaPresenter<View>(), Injector by injector {

    companion object {
        private lateinit var injector: Injector
        fun init() {
            injector = AppInjector()
        }
    }

    protected fun showComingSoon() {
        viewState.showToast(Text.from(R.string.message_coming_soon))
    }

}