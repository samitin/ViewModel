package ru.samitin.viewmodel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import ru.samitin.viewmodel.model.AppState
import ru.samitin.viewmodel.model.data.convertDTOToModel
import ru.samitin.viewmodel.model.dto.FactDTO
import ru.samitin.viewmodel.model.dto.WeatherDTO
import ru.samitin.viewmodel.model.repository.DetailsRepository
import ru.samitin.viewmodel.model.repository.DetailsRepositoryImpl
import ru.samitin.viewmodel.model.repository.RemoteDataSourse

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetaisViewModel( val detailsLifeData:MutableLiveData<AppState> = MutableLiveData(), private val detailsDataSourse: DetailsRepository=DetailsRepositoryImpl(RemoteDataSourse())
 ) : ViewModel(){


    fun getWeatherFromRemoteSourse(lat:Double,lon:Double){
        detailsLifeData.value=AppState.Loading
        detailsDataSourse.getWeatherDetailsFromServer(lat,lon,callBack)
    }
    private val callBack= object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse:WeatherDTO?=response.body()
            detailsLifeData.postValue(
                if (response.isSuccessful&&serverResponse!=null){
                    checkResponse(serverResponse)
                }else{
                        AppState.Error(Throwable(SERVER_ERROR))
                    })
        }
        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLifeData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    fun checkResponse(weatherDTO: WeatherDTO):AppState{
        val fact: FactDTO? =weatherDTO.fact
        return if (fact==null||fact.temp==null||fact.feels_like==null||fact.condition.isNullOrEmpty())
                   AppState.Error(Throwable(CORRUPTED_DATA))
               else
                  AppState.Success(convertDTOToModel(weatherDTO))
    }

}