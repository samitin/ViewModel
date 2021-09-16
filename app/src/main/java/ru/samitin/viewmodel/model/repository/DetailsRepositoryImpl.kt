package ru.samitin.viewmodel.model.repository


import ru.samitin.viewmodel.model.dto.WeatherDTO

class DetailsRepositoryImpl(private val remoteDataSource :RemoteDataSourse) : DetailsRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }
}