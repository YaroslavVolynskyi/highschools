package com.example.nychighschools.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.nychighschools.NycHighschoolsApplication

open class PreferenceHelper {

    companion object {

        private const val UPDATE_PREFERENCES_NAME = "updatePreferences"
        private const val UPDATE_TIME_KEY = "updateTimeKey"

        private var INSTANCE: PreferenceHelper? = null

        fun getInstance(): PreferenceHelper {
            if (INSTANCE == null) {
                synchronized(PreferenceHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = PreferenceHelper()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    open fun saveUpdateTime(context: Context = NycHighschoolsApplication.appContext) {
        saveUpdateTime(context, System.currentTimeMillis().toString())
    }

    open fun getLastUpdateTime(context: Context = NycHighschoolsApplication.appContext): Long {
        return getPreferences(context).getString(UPDATE_TIME_KEY, "0")!!.toLong()
    }

    open fun resetLastUpdateTime(context: Context = NycHighschoolsApplication.appContext) {
        saveUpdateTime(context,"0")
    }

    private fun saveUpdateTime(context: Context = NycHighschoolsApplication.appContext, time: String) {
        getPreferences(context).edit().putString(UPDATE_TIME_KEY, time).apply()
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(UPDATE_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}
