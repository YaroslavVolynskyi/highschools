package com.example.nychighschools

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.room.EmptyResultSetException
import com.example.nychighschools.data.Highschool
import com.example.nychighschools.database.HighschoolDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class HighschoolsViewModel(stateHandle: SavedStateHandle) : ViewModel() {

    private val highschoolsLiveData: MutableLiveData<List<Highschool>> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()
    private val highschoolDao = HighschoolDatabase.getInstance().highschoolDao()

    fun fetchHighschoolsData() {
        val disposable = highschoolDao.getHighschoolsList()
            .subscribeOn(HighschoolDatabase.databaseScheduler)
            .flatMap { list ->
                if (list.isEmpty()) {
                    return@flatMap RetrofitProvider.highschoolsApi
                        .getHighSchools()
                        .doOnSuccess {
                            highschoolDao.insertHighschoolsList(it)
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

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}
