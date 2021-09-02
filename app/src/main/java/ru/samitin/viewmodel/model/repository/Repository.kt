package ru.samitin.viewmodel.model.repository

import ru.samitin.viewmodel.model.data.Weather

interface Repository {
    fun getWeatherFromServer(): List<Weather>
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}