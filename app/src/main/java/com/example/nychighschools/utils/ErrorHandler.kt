package com.example.nychighschools.utils

import com.example.nychighschools.NycHighschoolsApplication
import com.example.nychighschools.R
import java.net.UnknownHostException

class ErrorHandler {

    companion object {

        // todo add more if clauses in future according to some other exceptions
        fun getErrorMessage(throwable: Throwable): String {
            return if (throwable is UnknownHostException) {
                NycHighschoolsApplication.appContext.getString(R.string.error_no_internet)
            } else {
                throwable.message.toString()
            }
        }
    }
}
