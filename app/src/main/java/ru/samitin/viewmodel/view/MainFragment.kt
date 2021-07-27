package ru.samitin.viewmodel.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import ru.samitin.viewmodel.model.AppState
import ru.samitin.viewmodel.R
import ru.samitin.viewmodel.databinding.MainFragmentBinding
import ru.samitin.viewmodel.model.data.Weather
import ru.samitin.viewmodel.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _bainding:MainFragmentBinding?=null
    private val binding
    get()=_bainding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _bainding= MainFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val observer= Observer<AppState> {renderData(it)  }
        viewModel.getLiveData().observe(viewLifecycleOwner,observer)
        viewModel.getWeatherFromLocalSource()
    }

    private fun renderData(appState: AppState) {
        when(appState){
            is AppState.Success ->{
                val weatherData=appState.weatherData
                binding.loadingLayout.visibility=View.GONE
                setData(weatherData)
            }
            is AppState.Loading ->{
                binding.loadingLayout.visibility=View.VISIBLE
            }
            is AppState.Error  ->{
                binding.loadingLayout.visibility=View.GONE
                Snackbar
                        .make(binding.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Reload") { viewModel.getWeatherFromLocalSource() }
                        .show()

            }
        }
    }
    private fun setData(weatherData:Weather){
        binding.cityName.text=weatherData.city.city
        binding.cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weatherData.city.lat.toString(),
                weatherData.city.lon.toString()
        )

        binding.temperatureValue.text = weatherData.temperature.toString()
        binding.feelsLikeValue.text = weatherData.feelsLike.toString()
    }


    override fun onDestroy() {
        super.onDestroy()
        _bainding=null
    }
}