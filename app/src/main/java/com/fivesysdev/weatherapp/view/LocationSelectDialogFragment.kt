package com.fivesysdev.weatherapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.fivesysdev.weatherapp.adapters.LocationAdapter
import com.fivesysdev.weatherapp.R
import com.fivesysdev.weatherapp.databinding.LocationSelectDialogFragmentBinding
import com.fivesysdev.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSelectDialogFragment : BottomSheetDialogFragment() {

    private var _binding: LocationSelectDialogFragmentBinding? = null

    private val searchHandler = Handler(Looper.getMainLooper())

    private val viewModel: WeatherViewModel by viewModels()

    private var searchText: String = ""

    private val searchRunnable = Runnable {
        viewModel.findLocationByName(searchText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            DialogFragment.STYLE_NO_FRAME,
            R.style.CustomBottomSheetDialogTheme
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LocationSelectDialogFragmentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LocationAdapter()
        viewModel.locationInfoArrayLiveData.observe(viewLifecycleOwner) {
           adapter.updateData(it)
        }
        viewModel.saveInfoLiveData.observe(viewLifecycleOwner) {
            (requireActivity() as MainActivity).showBottomNavigation()
            dialog?.dismiss()
        }
        adapter.setOnItemClickListener { position ->
            val city = adapter.getItem(position)
            viewModel.saveInfo(city)
        }

        with(_binding) {
            this?.rvCountries?.adapter = adapter
            this?.etSearchCountry?.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    searchText = s.toString()
                    searchHandler.removeCallbacks(searchRunnable)
                    searchHandler.postDelayed(searchRunnable, 2000L)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            this?.ivClose?.setOnClickListener { dismiss() }
        }
    }

}

