package com.example.nychighschools

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nychighschools.data.Highschool
import com.example.nychighschools.data.HighschoolSatInfo
import com.example.nychighschools.data.HighschoolsApi
import com.example.nychighschools.database.HighschoolDao
import com.example.nychighschools.utils.PreferenceHelper
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 *
 * Here in this test I tried to create viewmodel with test instances of Database, HighschoolApi,
 * and PreferencesHelper, and TestSchedulers.
 * The goal was to check rx chain for retrieving information.
 * I wanted to make sure that [HighschoolsViewModel.fetchHighschoolsData] populates database with
 * response from network API and also returns it.
 */
@RunWith(AndroidJUnit4::class)
class HighschoolInstrumentedTest {

    /**
     * test class of database
     */
    class TestDatabase: HighschoolDao {

        val highschooslList: ArrayList<Highschool> = ArrayList()

        override fun insertHighschoolsList(schools: List<Highschool>): Completable {
            return Completable.fromAction { highschooslList.addAll(schools) }
        }

        override fun getHighschoolsList(): Single<List<Highschool>> {
            return Single.just(highschooslList)
        }
    }

    /**
     * test class for Network api
     */
    class TestApi: HighschoolsApi {
        override fun getHighSchools(): Single<List<Highschool>> {
            val highschools = ArrayList<Highschool>().apply {
                add(Highschool.getHighschoolTestInstance("dbn1", "name1"))
                add(Highschool.getHighschoolTestInstance("dbn2", "name2"))
            }
            return Single.just(highschools)
        }

        override fun getSatHighschoolsInfo(): Single<List<HighschoolSatInfo>> {
            val satInfo = ArrayList<HighschoolSatInfo>().apply {
                add(HighschoolSatInfo.getSatInfoTestInstance("dbn1", "1", "1", "1"))
                add(HighschoolSatInfo.getSatInfoTestInstance("dbn2", "2", "2", "2"))
            }
            return Single.just(satInfo)
        }

    }

    /**
     * test class for preferences, which uses test instance of context.
     */
    class TestPreferencesHelper: PreferenceHelper() {
        override fun saveUpdateTime(context: Context) {
            super.saveUpdateTime(InstrumentationRegistry.getInstrumentation().targetContext)
        }

        override fun getLastUpdateTime(context: Context): Long {
            return super.getLastUpdateTime(InstrumentationRegistry.getInstrumentation().targetContext)
        }

        override fun resetLastUpdateTime(context: Context) {
            super.resetLastUpdateTime(InstrumentationRegistry.getInstrumentation().targetContext)
        }
    }

    private val compositeDisposable = CompositeDisposable()
    private val database = TestDatabase()
    private val scheduler = TestScheduler()
    private val highschoolsViewModel = HighschoolsViewModel(
        SavedStateHandle(),
        database,
        TestPreferencesHelper(),
        TestApi(),
        scheduler,
        scheduler,
        scheduler
    )


    /**
     * For some reason rx chain from [HighschoolsViewModel.fetchHighschoolsData] can't be tested.
     * Given more time I would fix this somehow. Maybe I would try to use Mockito framework
     * for mocking all network and database call, and not use explicit test instances of database and API,
     * but to use mock objects.
     */
    @Test
    fun check_that_database_is_populated_after_api_call_for_retrieving_info() {
        highschoolsViewModel.fetchHighschoolsData(
            onSuccessFinishAction = {
                val disposable = database
                    .getHighschoolsList()
                    .subscribeOn(scheduler)
                    .observeOn(scheduler)
                    .subscribe({ highscoolsList ->
                        assertTrue(highscoolsList.isNotEmpty())
                    }, { throwable ->
                        Log.e("sdfdsf", "DDDDDDD_444444")
                        assertTrue(false)
                    })
                compositeDisposable.add(disposable)
            }
        )
    }

    @After
    fun clearResources() {
        compositeDisposable.clear()
    }
}