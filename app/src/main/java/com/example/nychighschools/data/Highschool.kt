package com.example.nychighschools.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "highschools")
data class Highschool(

    @PrimaryKey
    @Expose
    val dbn: String,
    @Expose
    @SerializedName("school_name")
    val schoolName: String?,
    @Expose
    @SerializedName("school_email")
    val schoolEmail: String?,
    @Expose
    val city: String?,
    @Expose
    @SerializedName("overview_paragraph")
    val overviewParagraph: String?,

    @Expose
    @SerializedName("sat_critical_reading_avg_score")
    var satScoreReading: String?,
    @Expose
    @SerializedName("sat_math_avg_score")
    var satScoreMath: String?,
    @Expose
    @SerializedName("sat_writing_avg_score")
    var satScoreWriting: String?,

    @Expose
    var website: String?,
    @Expose
    var boro: String?,
    @Expose
    @SerializedName("fax_number")
    var faxNumber: String?,
    @Expose
    var bus: String?,
    @Expose
    @SerializedName("primary_address_line_1")
    var address: String?,
    @Expose
    var zip: String?,
    @Expose
    @SerializedName("total_students")
    var totalStudents: String?,
    @Expose
    @SerializedName("program_highlights")
    var programHighlights: String?,
    @Expose
    @SerializedName("language_classes")
    val languageClasses: String?,
    @Expose
    @SerializedName("extracurricular_activities")
    var extracurricularActivities: String?

) {
    @Expose
    var isExpanded: Boolean = false
}
