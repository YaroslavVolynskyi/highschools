package com.example.nychighschools.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nychighschools.NycHighschoolsApplication
import com.example.nychighschools.data.Highschool
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

@Database(entities = [Highschool::class], version = 1, exportSchema = false)
abstract class HighschoolDatabase : RoomDatabase() {

    abstract fun highschoolDao(): HighschoolDao

    companion object {

        private const val databaseName = "HighschoolDatabase.db"
        private const val testDatabaseName = "TESTING_HighschoolDatabase.db"
        private var INSTANCE: HighschoolDatabase? = null

        val databaseScheduler = Schedulers.from(Executors.newFixedThreadPool(1))

        fun getInstance(): HighschoolDatabase {
            if (INSTANCE == null) {
                synchronized(HighschoolDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            NycHighschoolsApplication.appContext,
                            HighschoolDatabase::class.java,
                            databaseName
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}