package ru.samitin.viewmodel.model.repository

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.samitin.viewmodel.BuildConfig


private const val REQUEST_API_KEY = "X-Yandex-API-Key"
class RemoteDataSourse {
    fun getWeatherDetails(requestLink: String,callBack:Callback){
        val builder= Request.Builder().apply {
            header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY)
            url(requestLink)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callBack)
    }


}