package ru.samitin.viewmodel.model.data

import ru.samitin.viewmodel.model.dto.WeatherDTO


fun convertDTOToModel(weatherDTO: WeatherDTO):List<Weather>{
    val fact=weatherDTO.fact
    return listOf(Weather(getDefaultCity(), fact!!.temp!!,fact!!.feels_like!!,fact!!.condition!!))
}