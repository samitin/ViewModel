package ru.samitin.viewmodel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.samitin.viewmodel.model.AppState
import ru.samitin.viewmodel.model.repository.Repository
import ru.samitin.viewmodel.model.repository.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(private val liveDataToObserve:MutableLiveData<AppState> = MutableLiveData(),
                    private val repositoryImpl: Repository = RepositoryImpl()) : ViewModel() {


    fun getLiveData()=liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getWeatherFromLocalSource(isRus = true)

    fun getWeatherFromLocalSourceWorld() =getWeatherFromLocalSource(isRus = false)

    private fun getWeatherFromLocalSource(isRus:Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(3000)
            liveDataToObserve.postValue(AppState.Success(
                    if (isRus)repositoryImpl.getWeatherFromLocalStorageRus()
                    else repositoryImpl.getWeatherFromLocalStorageWorld()))
        }.start()
    }

    fun getWeatherFromRemoteSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(AppState.Success(repositoryImpl.getWeatherFromServer()))
        }.start()
    }
}

