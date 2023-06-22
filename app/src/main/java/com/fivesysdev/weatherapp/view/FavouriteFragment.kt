package com.fivesysdev.weatherapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.fivesysdev.weatherapp.adapters.FavouriteAdapter
import com.fivesysdev.weatherapp.databinding.FragmentFavouriteBinding
import com.fivesysdev.weatherapp.viewmodel.FavouriteFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class FavouriteFragment : Fragment() {

    private val viewModel: FavouriteFragmentViewModel by viewModels()

    private var _binding: FragmentFavouriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FavouriteAdapter()
        binding.rvFavouriteWeather.adapter = adapter
        viewModel.favoriteList()
        viewModel.weatherArrayLiveData.observe(this) { weatherInfoList ->
            adapter.updateData(weatherInfoList.filterNotNull())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
