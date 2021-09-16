package ru.samitin.viewmodel.view



import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.samitin.viewmodel.R
import ru.samitin.viewmodel.databinding.FragmentDetailsBinding
import ru.samitin.viewmodel.model.AppState
import ru.samitin.viewmodel.model.data.Weather
import ru.samitin.viewmodel.viewmodel.DetaisViewModel




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
        viewModel.detailsLifeData.observe(viewLifecycleOwner,{renderData(it)})
        viewModel.getWeatherFromRemoteSourse(weatherBundle.city.lat,weatherBundle.city.lon)
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
                    viewModel.getWeatherFromRemoteSourse(weatherBundle.city.lat,weatherBundle.city.lon)
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