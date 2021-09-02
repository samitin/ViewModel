package ru.samitin.viewmodel.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.samitin.viewmodel.model.data.City

@Parcelize
data class Weather(val city: City =getDefaultCity(), val temperature:Int=0, val feelsLike:Int=0):
        Parcelable
fun getDefaultCity()= City("Москва", 55.755826, 37.617299900000035)
