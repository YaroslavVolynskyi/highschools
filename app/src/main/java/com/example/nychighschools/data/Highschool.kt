package com.example.nychighschools.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Highschool(
    @Expose
    val dbn: String,
    @Expose
    @SerializedName("school_name")
    val schoolName: String,
    @Expose
    @SerializedName("school_email")
    val schoolEmail: String,
    @Expose
    val city: String,
    @Expose
    @SerializedName("overview_paragraph")
    val overviewParagraph: String
)
