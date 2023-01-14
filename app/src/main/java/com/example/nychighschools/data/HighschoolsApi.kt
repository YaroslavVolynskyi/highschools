package com.example.nychighschools.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface HighschoolsApi {

    @GET("https://data.cityofnewyork.us/resource/7crd-d9xh.json")
    fun getHighSchools(): Single<List<Highschool>>

    @GET
    fun getSatHighschoolsInfo(@Url url: String): Single<List<HighschoolSatInfo>>
}