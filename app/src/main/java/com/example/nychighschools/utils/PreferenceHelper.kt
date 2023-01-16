package com.example.nychighschools.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.nychighschools.NycHighschoolsApplication


class PreferenceHelper {

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

    fun saveUpdateTime() {
        saveUpdateTime(System.currentTimeMillis().toString())
    }

    fun getLastUpdateTime(): Long {
        return getPreferences().getString(UPDATE_TIME_KEY, "0")!!.toLong()
    }

    fun resetLastUpdateTime() {
        saveUpdateTime("0")
    }

    private fun saveUpdateTime(time: String) {
        getPreferences().edit().putString(UPDATE_TIME_KEY, time).apply()
    }

    private fun getPreferences(): SharedPreferences {
        return NycHighschoolsApplication.appContext.getSharedPreferences(UPDATE_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }
}
