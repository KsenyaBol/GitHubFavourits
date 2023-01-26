package com.example.data.di

import com.example.data.AppErrorHandler
import com.example.data.repository.DataRepoOmegaRepository
import com.example.data.source.RemoteRepoSource
import com.example.domain.di.Injector
import com.example.domain.repository.RepoRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

abstract class DataInjector: Injector {

    private val loggingInterceptor = HttpLoggingInterceptor()

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val errorHandler = AppErrorHandler(moshi)

    private val retrofit =  Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    override val repoRepository: RepoRepository =
        DataRepoOmegaRepository(errorHandler = errorHandler, RemoteRepoSource(retrofit.create()))


}