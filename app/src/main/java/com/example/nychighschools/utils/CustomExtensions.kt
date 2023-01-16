package com.example.nychighschools.utils

import android.text.TextUtils
import android.view.View
import android.widget.TextView

fun TextView.init(inputText: String?) {
    if (!TextUtils.isEmpty(inputText)) {
        visibility = View.VISIBLE
        text = inputText
    } else {
        visibility = View.GONE
    }
}
