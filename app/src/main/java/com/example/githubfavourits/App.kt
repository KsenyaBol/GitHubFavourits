package com.example.githubfavourits

import android.app.Application
import com.example.data.api.GitHubApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class App: Application() {

    companion object {

        var githubApi: GitHubApi? = null
        var retrofit: Retrofit? = null
        val builder = OkHttpClient.Builder()

        fun getApi(): GitHubApi? { return githubApi}

    }

    override fun onCreate() {
        super.onCreate()


        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(builder.build())
            .build()

        githubApi = retrofit!!.create(GitHubApi::class.java)

    }


}