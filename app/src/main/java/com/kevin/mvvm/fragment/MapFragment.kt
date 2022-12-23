package com.kevin.mvvm.fragment

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amap.api.maps.AMap
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.MyLocationStyle
import com.kevin.mvvm.databinding.MapFragmentBinding


class MapFragment : BaseFragment(), AMap.OnMyLocationChangeListener {

    private val TAG = MapFragment::class.java.simpleName

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

        initMap()
    }

    /**
     * 初始化地图
     */
    private fun initMap() {
        //初始化地图控制器对象
        val aMap = binding.mapView.map
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.isMyLocationEnabled = true
        val style =
            MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) //定位一次，且将视角移动到地图中心点。
        aMap.myLocationStyle = style //设置定位蓝点的Style
        aMap.uiSettings.isMyLocationButtonEnabled = true //设置默认定位按钮是否显示，非必需设置。
        aMap.isMyLocationEnabled = true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this)
    }

    override fun onMyLocationChange(location: Location?) {
        // 定位回调监听
        if (location != null) {
            Log.e(TAG,
                "onMyLocationChange 定位成功， lat: " + location.getLatitude()
                    .toString() + " lon: " + location.getLongitude())
        } else {
            Log.e(TAG, "定位失败")
        }
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


