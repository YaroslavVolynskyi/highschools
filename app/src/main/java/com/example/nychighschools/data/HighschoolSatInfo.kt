package com.example.nychighschools.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HighschoolSatInfo(

    @Expose
    val dbn: String?,
    @Expose
    @SerializedName("school_name")
    val schoolName: String?,
    @Expose
    @SerializedName("sat_critical_reading_avg_score")
    val satScoreReading: String?,
    @Expose
    @SerializedName("sat_math_avg_score")
    val satScoreMath: String?,
    @Expose
    @SerializedName("sat_writing_avg_score")
    val satScoreWriting: String?
) {
    companion object {
        fun getSatInfoTestInstance(
            dbn: String,
            satReading: String,
            satMath: String,
            satWriting: String
        ): HighschoolSatInfo {
            return HighschoolSatInfo(
                dbn = dbn,
                satScoreMath = satMath,
                satScoreReading = satReading,
                satScoreWriting = satWriting,
                schoolName = null
            )
        }
    }
}
