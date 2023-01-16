package com.example.nychighschools

import com.example.nychighschools.data.Highschool
import com.example.nychighschools.data.HighschoolSatInfo
import com.example.nychighschools.utils.Utils
import org.junit.Assert.assertEquals

import org.junit.Test

class HighschoolsUnitTests {

    val schoolsList: ArrayList<Highschool> = ArrayList()
    val satInfoList: ArrayList<HighschoolSatInfo> = ArrayList()

    private fun initResources() {
        val elementsCount = 5
        schoolsList.clear()
        satInfoList.clear()
        schoolsList.apply {
            for (i in 0..elementsCount) {
                add(Highschool.getHighschoolTestInstance("dbn $i", "name - $i"))
            }
        }
        satInfoList.apply {
            for (i in 0..elementsCount) {
                add(
                    HighschoolSatInfo.getSatInfoTestInstance(
                        "dbn $i",
                        "${i + 1}",
                        "${i + 1}",
                        "${i + 1}"
                    )
                )
            }
        }
    }

    /**
     * This test check if merge of two lists using [Utils.mergeHighschoolsInfo] was successful
     * and that [schoolsList] have sat info from [satInfoList]
     */
    @Test
    fun check_that_merged_lists_contain_sat_info() {
        initResources()
        Utils.mergeHighschoolsInfo(schoolsList, satInfoList)
        for (i in 0 until schoolsList.size) {
            assertEquals("${i + 1}", schoolsList[i].satScoreReading)
            assertEquals("${i + 1}", schoolsList[i].satScoreMath)
            assertEquals("${i + 1}", schoolsList[i].satScoreWriting)
        }
    }

    /**
     * This test check that without merge using [Utils.mergeHighschoolsInfo], [schoolsList] will not
     * contain sat info from [satInfoList]
     */
    @Test
    fun check_that_NOT_merged_lists_DONT_contain_sat_info() {
        initResources()
        for (i in 0 until schoolsList.size) {
            assertEquals(null, schoolsList[i].satScoreReading)
            assertEquals(null, schoolsList[i].satScoreMath)
            assertEquals(null, schoolsList[i].satScoreWriting)
        }
    }
}
