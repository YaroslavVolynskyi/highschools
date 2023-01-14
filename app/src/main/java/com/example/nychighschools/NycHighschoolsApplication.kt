package com.example.nychighschools

import android.app.Application
import android.content.Context

class NycHighschoolsApplication: Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        RetrofitProvider.init(this)
    }
}
