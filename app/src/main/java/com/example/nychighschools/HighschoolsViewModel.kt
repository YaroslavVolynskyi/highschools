package com.example.nychighschools

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.nychighschools.data.Highschool
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class HighschoolsViewModel(stateHandle: SavedStateHandle): ViewModel() {

    private val highschoolsLiveData: MutableLiveData<List<Highschool>> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    fun fetchHighschoolsData() {
        val disposable = RetrofitProvider.highschoolsApi.getHighSchools()
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
