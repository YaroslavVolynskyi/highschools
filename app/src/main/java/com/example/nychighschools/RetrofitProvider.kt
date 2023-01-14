package com.example.nychighschools

import android.content.Context
import androidx.room.Room
import com.example.nychighschools.data.HighschoolsApi
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object RetrofitProvider {

    const val BASE_URL = "https://data.cityofnewyork.us/"
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    lateinit var highschoolsApi: HighschoolsApi

    fun init(context: Context) {
        okHttpClient = OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, "http_cache"), 50L * 1024L * 1024L))
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                )
            )
            .client(okHttpClient)
            .build()
        highschoolsApi = retrofit.create(HighschoolsApi::class.java)
    }
}
