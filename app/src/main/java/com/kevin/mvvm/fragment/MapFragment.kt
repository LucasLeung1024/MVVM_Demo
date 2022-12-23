package com.kevin.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.maps.MapsInitializer
import com.kevin.mvvm.databinding.MapFragmentBinding


class MapFragment : BaseFragment() {

    private var _binding: MapFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MapFragmentBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //基于个人隐私保护的关系，这里要设置为true，否则会出现地图白屏的情况
        MapsInitializer.updatePrivacyShow(requireActivity(), true, true)
        MapsInitializer.updatePrivacyAgree(requireActivity(), true)
        binding.mapView.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }


}
