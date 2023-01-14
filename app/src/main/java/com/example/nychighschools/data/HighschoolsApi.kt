package com.example.nychighschools.data

import io.reactivex.Single
import retrofit2.http.GET

interface HighschoolsApi {

    /**
     * If I had more time and API would allow me to pass page number as query parameter
     * I would implement some kind of pagination, or maybe continuous loading, for example
     * load first 50 elements, then if user has scrolled to let's say 45th element - load next 50 elements
     * and so on.
     */
    @GET("https://data.cityofnewyork.us/resource/7crd-d9xh.json")
    fun getHighSchools(): Single<List<Highschool>>

    @GET("https://data.cityofnewyork.us/resource/f9bf-2cp4.json")
    fun getSatHighschoolsInfo(): Single<List<HighschoolSatInfo>>
}