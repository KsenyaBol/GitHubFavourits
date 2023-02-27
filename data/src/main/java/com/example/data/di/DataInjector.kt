package com.example.data.di

import android.annotation.SuppressLint
import com.example.data.repository.RepoRepositoryImpl
import com.example.data.source.RemoteRepoSource
import com.example.domain.di.Injector
import com.example.domain.repository.RepoRepository
import com.omega_r.base.errors.ErrorHandler
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


abstract class DataInjector: Injector {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    val customDateAdapter: Any = object : Any() {
        @SuppressLint("SimpleDateFormat")
        var dateFormat =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

        @ToJson
        @Synchronized
        fun dateToJson(d: Date?): String? {
            return dateFormat.format(d)
        }

        @FromJson
        @Synchronized
        @Throws(ParseException::class)
        fun dateToJson(s: String?): Date? {
            return dateFormat.parse(s)
        }

    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(customDateAdapter)
        .build()

    private val retrofit =  Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    override val repoRepository: RepoRepository =
        RepoRepositoryImpl(errorHandler = ErrorHandler(), RemoteRepoSource(retrofit.create()))


}