package ru.samitin.viewmodel.model.repository

import okhttp3.Callback

class DetailsRepositoryImpl(private val remoteDataSource :RemoteDataSourse) : DetailsRepository {
    override fun getWeatherDetailsFromServer(requestLink: String, callBack: Callback) {
        remoteDataSource.getWeatherDetails(requestLink,callBack)
    }
}