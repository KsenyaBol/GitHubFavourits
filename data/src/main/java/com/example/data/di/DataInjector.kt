package com.example.data.di

import com.example.data.AppErrorHandler
import com.example.data.BuildConfig
import com.example.data.api.GitHubApi
import com.example.domain.di.Injector
import com.example.domain.entity.RepoRepository
import com.example.domain.objects.repositories.DataRepoRepository
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import com.omega_r.base.errors.ErrorHandler
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
abstract class DataInjector: Injector {

    companion object {

        var githubApi: GitHubApi? = null
        var retrofit: Retrofit? = null
        val builder = OkHttpClient.Builder()
    }

    override val repoRepository: RepoRepository = DataRepoRepository(1, "", true)

    @Provides
    @Singleton
    fun provideErrorHandler(moshi: Moshi): ErrorHandler = AppErrorHandler(moshi)

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit =  Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(builder.build())
            .build()

//        githubApi = retrofit!!.create(GitHubApi::class.java)

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = when {
            BuildConfig.DEBUG -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .build()
    }

//    if (BuildConfig.DEBUG) {
//        val logging = HttpLoggingInterceptor()
//        logging.level = HttpLoggingInterceptor.Level.BODY
//        builder.addInterceptor(logging)
//    }





}