package ru.samitin.viewmodel.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.samitin.viewmodel.model.AppState
import ru.samitin.viewmodel.model.data.Weather
import ru.samitin.viewmodel.model.data.convertDTOToModel
import ru.samitin.viewmodel.model.data.getDefaultCity
import ru.samitin.viewmodel.model.dto.FactDTO
import ru.samitin.viewmodel.model.dto.WeatherDTO
import ru.samitin.viewmodel.model.repository.DetailsRepository
import ru.samitin.viewmodel.model.repository.DetailsRepositoryImpl
import ru.samitin.viewmodel.model.repository.RemoteDataSourse
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetaisViewModel(
    private val detailsLifeData:MutableLiveData<AppState> = MutableLiveData(),
    private val detailsDataSourse: DetailsRepository=DetailsRepositoryImpl(RemoteDataSourse())
 ) : ViewModel(){

    fun getLifeData()=detailsLifeData
    fun getWeatherFromRemoteSourse(requestLink : String){
        detailsLifeData.value=AppState.Loading
        detailsDataSourse.getWeatherDetailsFromServer(requestLink,callBack)
    }
    private val callBack= object : Callback{

        override fun onResponse(call: Call, response: Response) {
            val serverResponse=response.body()?.string()
            detailsLifeData.postValue(
                if (response.isSuccessful&&serverResponse!=null){
                    checkResponse(serverResponse)
                }else{
                        AppState.Error(Throwable(SERVER_ERROR))
                    })

        }
        override fun onFailure(call: Call, e: IOException) {
            detailsLifeData.postValue(AppState.Error(Throwable(e.message ?: REQUEST_ERROR)))
        }
    }

    fun checkResponse(serverResponse:String):AppState{
        val weatherDto:WeatherDTO = Gson().fromJson(serverResponse,WeatherDTO::class.java)
        val fact:FactDTO?=weatherDto.fact
        return if (fact==null||fact.temp==null||fact.feels_like==null||fact.condition.isNullOrEmpty())
                   AppState.Error(Throwable(CORRUPTED_DATA))
               else
                  AppState.Success(convertDTOToModel(weatherDto))
    }

}