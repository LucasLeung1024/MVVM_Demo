package com.kevin.mvvm.fragment

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapException
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.weather.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.kevin.mvvm.R
import com.kevin.mvvm.adapter.ForecastAdapter
import com.kevin.mvvm.databinding.DialogWeatherBinding
import com.kevin.mvvm.databinding.MapFragmentBinding


class MapFragment : BaseFragment(), AMap.OnMyLocationChangeListener,
    GeocodeSearch.OnGeocodeSearchListener,
    WeatherSearch.OnWeatherSearchListener {

    private val TAG = MapFragment::class.java.simpleName

    private var _binding: MapFragmentBinding? = null
    private val binding get() = _binding!!

    //解析成功标识码
    private val PARSE_SUCCESS_CODE = 1000
    private var geocoderSearch: GeocodeSearch? = null
    private var district: String? = null // 区/县

    private var liveResult: LocalWeatherLive? = null
    private var forecastResult: LocalWeatherForecast? = null

    //天气预报列表
    private val weatherForecast: MutableList<LocalDayWeatherForecast>? = null


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
        //点击按钮显示天气弹窗
        binding.fabWeather.setOnClickListener {showWeatherDialog()}
        
        initMap()
        initSearch()
    }

    /**
     * 初始化地图
     */
    private fun initMap() {
        //初始化地图控制器对象
        val aMap = binding.mapView.map
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.isMyLocationEnabled = true
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        val style = MyLocationStyle()
        //定位一次，且将视角移动到地图中心点。
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        //设置定位蓝点的Style
        aMap.myLocationStyle = style
        //修改放大缩小按钮的位置
        aMap.uiSettings.zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER
        //设置默认定位按钮是否显示，非必需设置。
        aMap.uiSettings.isMyLocationButtonEnabled = true
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.isMyLocationEnabled = true
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this)
    }

    /**
     * 初始化搜索
     */
    private fun initSearch() {
        try {
            geocoderSearch = GeocodeSearch(requireActivity())
            geocoderSearch!!.setOnGeocodeSearchListener(this)
        } catch (e: AMapException) {
            e.printStackTrace()
        }
    }

    /**
     * 搜索天气
     *
     * @param type WEATHER_TYPE_LIVE 实时天气   WEATHER_TYPE_FORECAST  预报天气
     */
    private fun searchWeather(type: Int) {
        val weatherSearchQuery = WeatherSearchQuery(district, type)
        try {
            val weatherSearch = WeatherSearch(requireActivity())
            weatherSearch.setOnWeatherSearchListener(this)
            weatherSearch.query = weatherSearchQuery
            weatherSearch.searchWeatherAsyn() //异步搜索
        } catch (e: com.amap.api.services.core.AMapException) {
            e.printStackTrace()
        }
    }

    override fun onMyLocationChange(location: Location?) {
        // 定位回调监听
        if (location != null) {
            Log.e(TAG,
                "onMyLocationChange 定位成功， lat: " + location.latitude
                    .toString() + " lon: " + location.longitude)

            //创建一个经纬度点，参数一是纬度，参数二是经度
            val latLonPoint = LatLonPoint(location.latitude, location.longitude)
            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            val query = RegeocodeQuery(latLonPoint, 20F, GeocodeSearch.AMAP)
            //通过经纬度获取地址信息
            geocoderSearch!!.getFromLocationAsyn(query)

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

    /**
     * 坐标转地址
     */
    override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult?, rCode: Int) {
        //解析result获取地址描述信息
        if (rCode === PARSE_SUCCESS_CODE) {
            val regeocodeAddress = regeocodeResult!!.regeocodeAddress
            //显示解析后的地址
            Log.e(TAG, "地址: " + regeocodeAddress.formatAddress)
            district = regeocodeAddress.district
            Log.e(TAG, "区: $district")

            //搜索天气  实时天气和预报天气
            searchWeather(WeatherSearchQuery.WEATHER_TYPE_LIVE)
            searchWeather(WeatherSearchQuery.WEATHER_TYPE_FORECAST)
        } else {
            showMsg("获取地址失败")
        }
    }

    /**
     * 地址转坐标
     */
    override fun onGeocodeSearched(geocodeResult: GeocodeResult?, rCode: Int) {}

    /**
     * 实时天气返回
     */
    override fun onWeatherLiveSearched(p0: LocalWeatherLiveResult?, p1: Int) {
        liveResult = p0!!.liveResult
        if (liveResult == null) {
            showMsg("实时天气数据为空")
        }
    }

    /**
     * 天气预报返回
     */
    override fun onWeatherForecastSearched(p0: LocalWeatherForecastResult?, p1: Int) {
        forecastResult = p0!!.forecastResult
        if (forecastResult != null) {
            Log.e(TAG, "onWeatherForecastSearched: " + Gson().toJson(forecastResult))
            binding.fabWeather.show()
        } else {
            showMsg("天气预报数据为空")
        }
    }

    /**
     * 显示天气弹窗
     */
    private fun showWeatherDialog() {
        //隐藏浮动按钮
        binding.fabWeather.hide()
        val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogStyle_Light)
        val weatherBinding: DialogWeatherBinding =
            DataBindingUtil.inflate(LayoutInflater.from(requireActivity()),
                R.layout.dialog_weather,
                null,
                false)
        //设置数据源
        //weatherBinding.setLiveWeather(LiveWeather(district!!, liveResult!!))
        //配置天气预报列表
        val forecastAdapter = ForecastAdapter(weatherForecast!!)
        weatherBinding.rvForecast.layoutManager = LinearLayoutManager(requireActivity())
        weatherBinding.rvForecast.adapter = forecastAdapter
        dialog.setContentView(weatherBinding.root)
        dialog.window!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT)
        //弹窗关闭时显示浮动按钮
        dialog.setOnDismissListener { binding.fabWeather.show() }
        dialog.show()
    }


}


