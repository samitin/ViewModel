package ru.samitin.viewmodel.model.repository

import ru.samitin.viewmodel.model.data.Weather
import ru.samitin.viewmodel.model.data.getRussianCities
import ru.samitin.viewmodel.model.data.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }
}