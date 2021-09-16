package ru.samitin.viewmodel.view



import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import okhttp3.*
import ru.samitin.viewmodel.BuildConfig
import ru.samitin.viewmodel.R
import ru.samitin.viewmodel.databinding.FragmentDetailsBinding
import ru.samitin.viewmodel.model.AppState
import ru.samitin.viewmodel.model.data.Weather
import ru.samitin.viewmodel.model.dto.WeatherDTO
import ru.samitin.viewmodel.viewmodel.DetaisViewModel
import java.io.IOException

private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"



class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    private val viewModel:DetaisViewModel by lazy {
        ViewModelProvider(this).get(DetaisViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        viewModel.getLifeData().observe(viewLifecycleOwner,{renderData(it)})
        viewModel.getWeatherFromRemoteSourse("https://api.weather.yandex.ru/v2/informers?lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
    }

    private fun renderData(appState:AppState) {
        when (appState) {
            is AppState.Loading -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
            }
            is AppState.Success -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                setWeather(appState.weatherData[0])
            }
            is AppState.Error -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload)
                ) {
                    viewModel.getWeatherFromRemoteSourse("https://api.weather.yandex.ru/v2/informers?lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
                }
            }
        }
    }
     private fun setWeather(weather:Weather)  {
         with(binding){
             weatherBundle.city.let {
                 cityName.text=it.city
                 cityCoordinates.text = String.format(
                     getString(R.string.city_coordinates),
                     it.lat.toString(),
                     it.lon.toString()
                 )
             }
             temperatureValue.text = weather.temperature.toString()
             feelsLikeValue.text = weather.feelsLike.toString()
             weatherCondition.text = weather.condition.toString()
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