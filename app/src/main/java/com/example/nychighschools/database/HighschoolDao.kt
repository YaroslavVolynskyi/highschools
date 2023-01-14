package com.example.nychighschools.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nychighschools.data.Highschool
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface HighschoolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHighschoolsList(highschoolsList: List<Highschool>): Completable

    @Query("SELECT * FROM highschools")
    fun getHighschoolsList(): Single<List<Highschool>>
    
}
