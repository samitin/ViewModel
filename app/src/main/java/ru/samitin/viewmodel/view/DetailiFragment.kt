package ru.samitin.viewmodel.view



import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import okhttp3.*
import ru.samitin.viewmodel.BuildConfig
import ru.samitin.viewmodel.R
import ru.samitin.viewmodel.databinding.FragmentDetailsBinding
import ru.samitin.viewmodel.model.data.Weather
import ru.samitin.viewmodel.model.dto.WeatherDTO
import java.io.IOException

private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"
private const val REQUEST_API_KEY = "X-Yandex-API-Key"


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        getWeather()
    }

    private fun getWeather() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE

        val client=OkHttpClient()
        val builder=Request.Builder()
        builder.header(REQUEST_API_KEY,BuildConfig.WEATHER_API_KEY)
        builder.url("https://api.weather.yandex.ru/v2/informers?lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
        val request=builder.build()
        val call:Call=client.newCall(request)
        call.enqueue( object : Callback{
            val handler=Handler(Looper.myLooper()!!)
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val serverResponse=response.body()?.string()
                if (response.isSuccessful&&serverResponse!=null){
                    handler.post {
                        renderData(Gson().fromJson(serverResponse, WeatherDTO::class.java))
                    }
                }else{
                    TODO(PROCESS_ERROR)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                TODO(PROCESS_ERROR)
            }
        })

    }

    private fun renderData(weatherDTO: WeatherDTO) {
        binding.mainView.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.GONE

        val fact = weatherDTO.fact
        val temp = fact!!.temp
        val feelsLike = fact.feels_like
        val condition = fact.condition
        if (temp == TEMP_INVALID || feelsLike == FEELS_LIKE_INVALID || condition == null) {
            TODO(PROCESS_ERROR)
        } else {
            val city = weatherBundle.city
            binding.cityName.text = city.city
            binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                city.lat.toString(),
                city.lon.toString()
            )
            binding.temperatureValue.text = temp.toString()
            binding.feelsLikeValue.text = feelsLike.toString()
            binding.weatherCondition.text = condition
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}