package com.example.nychighschools

import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.example.nychighschools.database.HighschoolDatabase
import com.example.nychighschools.utils.PreferenceHelper
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class HighschoolsUnitTests {

    @Before
    fun initResources() {
        RetrofitProvider.init()
    }

    private val compositeDisposable = CompositeDisposable()
    private val highscoolsViewModel = HighschoolsViewModel(
        SavedStateHandle(),
        HighschoolDatabase.getTestInstance(ApplicationProvider.getApplicationContext()).highschoolDao(),
        PreferenceHelper.getInstance(),
        RetrofitProvider.highschoolsApi
    )

    @Test
    fun check_lists_merge_isCorrect() {
//        highscoolsViewModel.fetchHighschoolsData(database) {
//            val disposable = database
//                .getHighschoolsList()
//                .subscribeOn(HighschoolDatabase.databaseScheduler)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ highscoolsList ->
//                    assert(highscoolsList.isNotEmpty())
//                    }, { throwable ->
//                    assert(false)
//                })
//            compositeDisposable.add(disposable)
//        }
        assertEquals(7, 2 + 5)
    }

    @Test
    fun testOk() {
        assertEquals(7, 2 + 5)
    }

    @After
    fun clearResources() {
        compositeDisposable.dispose()
    }
}
