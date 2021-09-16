package ru.samitin.viewmodel.model.repository

import okhttp3.Callback

interface DetailsRepository {

    fun getWeatherDetailsFromServer(requestLink:String,callBack:Callback)

}