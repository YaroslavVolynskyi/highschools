package com.example.nychighschools

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.nychighschools.data.Highschool
import com.example.nychighschools.data.HighschoolSatInfo
import com.example.nychighschools.data.HighschoolsApi
import com.example.nychighschools.database.HighschoolDao
import com.example.nychighschools.database.HighschoolDatabase
import com.example.nychighschools.utils.ErrorHandler
import com.example.nychighschools.utils.PreferenceHelper
import com.example.nychighschools.utils.SingleLiveData
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

const val MIN_UPDATE_TIME: Long = 40000L // 40 seconds update time

class HighschoolsViewModel(
    private val stateHandle: SavedStateHandle,
    private val highschoolDao: HighschoolDao,
    private val preferehcesHelper: PreferenceHelper,
    private val highschoolsApi: HighschoolsApi,
) : ViewModel() {

    companion object {

        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val savedStateHandle = extras.createSavedStateHandle()
                return HighschoolsViewModel(
                    savedStateHandle,
                    HighschoolDatabase.getInstance().highschoolDao(),
                    PreferenceHelper.getInstance(),
                    RetrofitProvider.highschoolsApi
                ) as T
            }
        }
    }

    private val highschoolsLiveData: MutableLiveData<List<Highschool>> = MutableLiveData()
    private val errorLiveData: SingleLiveData<String> = SingleLiveData()
    private val loadingLiveData: SingleLiveData<Boolean> = SingleLiveData()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Tries to fetch high schools' data from database. If database is still empty, or much time have passed from last update
     * (more then [MIN_UPDATE_TIME]), then we launch request to server (get high schools from [RetrofitProvider.highschoolsApi].
     *
     * In onSuccess callback from server - update database, and last update time.
     */
    // todo   change this fetch data to run 2 requests (get schools info, get sat info),
    // todo   put data into two separate database tables, then get merged info by fetching data from database using JOIN
    fun fetchHighschoolsData(onSuccessFinishAction: (() -> Unit)? = null) {
        val disposable = highschoolDao.getHighschoolsList()
            .subscribeOn(HighschoolDatabase.databaseScheduler)
            .flatMap { list ->
                if (list.isEmpty() || isTimeForNewUpdate()) {
                    return@flatMap highschoolsApi
                        .getHighSchools()
                        .zipWith(highschoolsApi.getSatHighschoolsInfo()) { highschoolsList, satList ->
                            mergeHighschoolsInfo(highschoolsList, satList)
                            return@zipWith highschoolsList
                        }
                        .doOnSuccess { highschoolsList ->
                            highschoolDao.insertHighschoolsList(highschoolsList)
                                .doOnComplete { preferehcesHelper.saveUpdateTime() }
                                .subscribeOn(HighschoolDatabase.databaseScheduler).subscribe()
                        }
                } else {
                    return@flatMap Single.just(list)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { d -> loadingLiveData.postValue(true) }
            .doFinally{ loadingLiveData.postValue(false) }
            .subscribe({ list ->
                highschoolsLiveData.postValue(list)
                onSuccessFinishAction?.invoke()
            }, this::handleThrowable)
        compositeDisposable.add(disposable)
    }

    fun forceRefresh() {
        preferehcesHelper.resetLastUpdateTime()
        fetchHighschoolsData()
    }

    fun getHighschoolsLiveData(): LiveData<List<Highschool>> {
        return highschoolsLiveData
    }

    fun getErrorLiveData(): LiveData<String> {
        return errorLiveData
    }

    /**
     * if true is observed - need to show some indicator that loading is in progress (for now will just show
     * simple text view). Progress bar can be implemented later.
     * if false is observed - removed loading indicator
     */
    fun getLoadingLiveData(): LiveData<Boolean> {
        return loadingLiveData
    }

    /**
     * add missing info about SAT scores from [satInfoList] into [highschoolsList]
     * @return list of highschools with complete info
     */
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

    private fun handleThrowable(throwable: Throwable) {
        Log.e("fail", throwable.message.toString())
        errorLiveData.postValue(ErrorHandler.getErrorMessage(throwable))
    }

    private fun isTimeForNewUpdate(): Boolean {
        return System.currentTimeMillis() - preferehcesHelper.getLastUpdateTime() > MIN_UPDATE_TIME
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}
