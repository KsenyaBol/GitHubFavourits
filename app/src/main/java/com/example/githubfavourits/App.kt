package com.example.githubfavourits

import android.app.Application
import com.example.data.api.GitHubApi
import com.example.data.di.DataInjector
import com.example.githubfavourits.ui.base.BasePresenter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        BasePresenter.init()
    }

}