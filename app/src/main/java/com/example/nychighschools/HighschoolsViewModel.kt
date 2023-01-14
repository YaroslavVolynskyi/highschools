package com.example.nychighschools

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.example.nychighschools.data.Highschool
import com.example.nychighschools.data.HighschoolSatInfo
import com.example.nychighschools.database.HighschoolDatabase
import com.example.nychighschools.utils.PreferenceHelper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction

const val MIN_UPDATE_TIME: Long = 10L

class HighschoolsViewModel(stateHandle: SavedStateHandle) : ViewModel() {

    private val highschoolsLiveData: MutableLiveData<List<Highschool>> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private val highschoolDao = HighschoolDatabase.getInstance().highschoolDao()

    /**
     * Tries to fetch high schools' data from database. If database is still empty, or much time have passed from last update
     * (more then [MIN_UPDATE_TIME]), then we launch request to server (get high schools from [RetrofitProvider.highschoolsApi].
     *
     * In onSuccess from server - update database, and last update time.
     */
    // todo change this fetch data to run 2 requests, put data into database, then get merged info by JOIN
    fun fetchHighschoolsData() {
        val disposable = highschoolDao.getHighschoolsList()
            .subscribeOn(HighschoolDatabase.databaseScheduler)
            .flatMap { list ->
                if (list.isEmpty() || isTimeForNewUpdate()) {
                    return@flatMap RetrofitProvider.highschoolsApi
                        .getHighSchools()
                        .zipWith(RetrofitProvider.highschoolsApi.getSatHighschoolsInfo()) { highschoolsList, satList ->
                            mergeHighschoolsInfo(highschoolsList, satList)
                            return@zipWith highschoolsList
                        }
                        .doOnSuccess { highschoolsList ->
                            PreferenceHelper.getInstance().saveUpdateTime()
                            highschoolDao.insertHighschoolsList(highschoolsList)
                                .subscribeOn(HighschoolDatabase.databaseScheduler).subscribe()
                        }
                } else {
                    return@flatMap Single.just(list)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                highschoolsLiveData.postValue(list)
            }, { throwable ->
                Log.e("fail", throwable.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    fun getHighschoolsLiveData(): LiveData<List<Highschool>> {
        return highschoolsLiveData
    }

    private fun mergeHighschoolsInfo(highschoolsList: List<Highschool>, satInfoList: List<HighschoolSatInfo>): List<Highschool> {
        highschoolsList.forEach{ highschool ->
            satInfoList.find { it.dbn == highschool.dbn }?.let { satInfo ->
                highschool.satScoreMath = satInfo.satScoreMath
                highschool.satScoreReading = satInfo.satScoreReading
                highschool.satScoreWriting = satInfo.satScoreWriting
            }
        }
        return highschoolsList
    }

    private fun isTimeForNewUpdate(): Boolean {
        return System.currentTimeMillis() - PreferenceHelper.getInstance()
            .getLastUpdateTime() > MIN_UPDATE_TIME
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}
