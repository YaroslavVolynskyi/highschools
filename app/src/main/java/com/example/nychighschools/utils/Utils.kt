package com.example.nychighschools.utils

import com.example.nychighschools.data.Highschool
import com.example.nychighschools.data.HighschoolSatInfo

class Utils {

    companion object {

        /**
         * add missing info about SAT scores from [satInfoList] into [highschoolsList]
         * @return list of highschools with complete info
         */
        fun mergeHighschoolsInfo(highschoolsList: List<Highschool>, satInfoList: List<HighschoolSatInfo>): List<Highschool> {
            highschoolsList.forEach{ highschool ->
                satInfoList.find { it.dbn == highschool.dbn }?.let { satInfo ->
                    highschool.satScoreMath = satInfo.satScoreMath
                    highschool.satScoreReading = satInfo.satScoreReading
                    highschool.satScoreWriting = satInfo.satScoreWriting
                }
            }
            return highschoolsList
        }
    }
}
