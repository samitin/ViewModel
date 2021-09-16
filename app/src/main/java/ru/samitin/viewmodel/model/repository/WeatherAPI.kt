package ru.samitin.viewmodel.model.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.samitin.viewmodel.model.dto.WeatherDTO

const val REQUEST_API_KEY = "X-Yandex-API-Key"
interface WeatherAPI {
    @GET("v2/informers")
    fun getWeather(
        @Header(REQUEST_API_KEY)  token : String,
        @Query("lat") lat : Double,
        @Query("lon") lon : Double
    ):Call<WeatherDTO>
}