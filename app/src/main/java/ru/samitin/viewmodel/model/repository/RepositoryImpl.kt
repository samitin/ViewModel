package ru.samitin.viewmodel.model.repository

import ru.samitin.viewmodel.model.data.Weather

class RepositoryImpl : Repository {
    override fun getWeatherFromServer(): Weather {

        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {

        return Weather()
    }
}