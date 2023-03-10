package com.kevin.mvvm.ui.fragment

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.district.DistrictItem
import com.amap.api.services.district.DistrictResult
import com.amap.api.services.district.DistrictSearch
import com.amap.api.services.district.DistrictSearchQuery
import com.amap.api.services.geocoder.*
import com.amap.api.services.weather.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kevin.mvvm.R
import com.kevin.mvvm.databinding.DialogWeatherBinding
import com.kevin.mvvm.databinding.MapFragmentBinding
import com.kevin.mvvm.model.LiveWeather
import com.kevin.mvvm.ui.adapter.CityAdapter
import com.kevin.mvvm.ui.adapter.ForecastAdapter


class MapFragment : BaseFragment(), AMap.OnMyLocationChangeListener,
    GeocodeSearch.OnGeocodeSearchListener,
    WeatherSearch.OnWeatherSearchListener,
    DistrictSearch.OnDistrictSearchListener{

    private val TAG = MapFragment::class.java.simpleName

    private var _binding: MapFragmentBinding? = null
    private val binding get() = _binding!!

    //解析成功标识码
    private val PARSE_SUCCESS_CODE = 1000

    //地图对象
    private var aMap: AMap? = null

    private var geocoderSearch: GeocodeSearch? = null
    private var district: String? = null // 区/县

    private var liveResult: LocalWeatherLive? = null
    private var forecastResult: LocalWeatherForecast? = null

    //天气预报列表
    private var weatherForecast: MutableList<LocalDayWeatherForecast>? = null

    //地区搜索
    private var districtSearch: DistrictSearch? = null

    //地区搜索查询
    private var districtSearchQuery: DistrictSearchQuery? = null

    //数组下标
    private var index = 0

    //行政区数组
    private val districtArray = arrayOfNulls<String>(5)

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
        //显示加载弹窗
        showLoading()
        binding.fabCity.show()
        //点击按钮显示天气弹窗
        binding.fabWeather.setOnClickListener {showWeatherDialog()}
        //点击按钮显示城市弹窗，显示这个抽屉页面，这里设置是从屏幕右侧打开
        binding.fabCity.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.END) }
        //抽屉菜单监听
        binding.drawerLayout.addDrawerListener(object : DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerOpened(drawerView: View) {
                binding.fabCity.hide()
            }

            override fun onDrawerClosed(drawerView: View) {
                binding.fabCity.show()
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })

        initMap()
        initSearch()
        //搜索行政区
        districtArray[index] = "中国"
        districtSearch(districtArray[index])
    }

    /**
     * 行政区搜索
     */
    private fun districtSearch(name: String?) {
        binding.name = name
        //设置查询关键字
        districtSearchQuery!!.keywords = name
        districtSearch!!.query = districtSearchQuery
        // 异步查询行政区
        districtSearch!!.searchDistrictAsyn()
    }


    /**
     * 初始化地图
     */
    private fun initMap() {
        //初始化地图控制器对象
        aMap = binding.mapView.map
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap!!.isMyLocationEnabled = true
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        val style = MyLocationStyle()
        //定位一次，且将视角移动到地图中心点。
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        //设置定位蓝点的Style
        aMap!!.myLocationStyle = style
        //修改放大缩小按钮的位置
        aMap!!.uiSettings.zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER
        //设置默认定位按钮是否显示，非必需设置。
        aMap!!.uiSettings.isMyLocationButtonEnabled = true
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap!!.isMyLocationEnabled = true
        //设置SDK 自带定位消息监听
        aMap!!.setOnMyLocationChangeListener(this)
        //设置地图是否为夜间模式
        if(isNight()){
            aMap!!.mapType = AMap.MAP_TYPE_NIGHT
        } else {
            aMap!!.mapType = AMap.MAP_TYPE_NORMAL
        }

    }

    /**
     * 初始化搜索
     */
    private fun initSearch() {
        try {
            //初始化地理编码搜索
            geocoderSearch = GeocodeSearch(requireActivity())
            geocoderSearch!!.setOnGeocodeSearchListener(this)
            //初始化城市行政区搜索
            districtSearch = DistrictSearch(requireActivity())
            districtSearch!!.setOnDistrictSearchListener(this)
            districtSearchQuery = DistrictSearchQuery()
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
    override fun onGeocodeSearched(geocodeResult: GeocodeResult?, rCode: Int) {
        //拿到返回的坐标，然后去地图上定位，改变地图中心
        if (rCode == PARSE_SUCCESS_CODE) {
            Log.e(TAG, "onGeocodeSearched: 地址转坐标成功")
            val geocodeAddressList = geocodeResult!!.geocodeAddressList
            if (geocodeAddressList != null && geocodeAddressList.size > 0) {
                val latLonPoint = geocodeAddressList[0].latLonPoint
                Log.e(TAG,
                    "onGeocodeSearched: 坐标：" + latLonPoint.longitude + "，" + latLonPoint.latitude
                )
                //切换地图中心
                switchMapCenter(geocodeResult, latLonPoint)
            }
        } else {
            showMsg("获取坐标失败")
        }
    }

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
    override fun onWeatherForecastSearched(localWeatherForecastResult: LocalWeatherForecastResult?, p1: Int) {
        weatherForecast = localWeatherForecastResult!!.forecastResult.weatherForecast
        if (weatherForecast != null) {
            binding.fabWeather.show()
            //隐藏加载弹窗
            dismissLoading()
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
        weatherBinding.liveWeather = LiveWeather(district!!, liveResult!!)
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

    override fun onDistrictSearched(districtResult: DistrictResult?) {
        if (districtResult != null) {
            dismissLoading()
            if (districtResult.aMapException
                    .errorCode === AMapException.CODE_AMAP_SUCCESS
            ) {
                val nameList: MutableList<String> = ArrayList()
                val subDistrict1: List<DistrictItem> =
                    districtResult.district[0].subDistrict
                for (i in subDistrict1.indices) {
                    val name: String = subDistrict1[i].name
                    nameList.add(name)
                }
                Log.e(TAG, "onDistrictSearched: " + subDistrict1.size)
                for (districtItem in subDistrict1) {
                    Log.e(TAG, "onDistrictSearched: " + districtItem.name)
                }
                if (nameList.size != 0) {
                    //设置数据源
                    val cityAdapter = CityAdapter(nameList)
                    binding.rvCity.layoutManager = LinearLayoutManager(requireActivity())
                    //item点击事件
                    cityAdapter.setOnItemClickListener { adapter, view, position ->

                        showLoading()

                        index++
                        districtArray[index] = nameList[position]

                        binding.ivBack.visibility = View.VISIBLE
                        //返回键的监听
                        binding.ivBack.setOnClickListener { v ->
                            index--
                            //搜索上级行政区
                            districtSearch(districtArray[index])
                            if ("中国" == districtArray[index]) {
                                binding.ivBack.visibility = View.GONE
                            }
                        }
                        //搜索此区域的下级行政区
                        districtSearch(districtArray[index])
                    }
                    binding.rvCity.adapter = cityAdapter
                } else{
                    //size=0表示什么，表示它没有下级行政区了，也就是说已经到了镇这个单位了，当然有的地方也叫街道
                    //通过地址得到坐标
                    addressToLatlng()
                }
            } else {
                showMsg(districtResult.aMapException.errorCode.toString())
            }
        }
    }

    /**
     * 地址转经纬度坐标
     */
    private fun addressToLatlng() {
        showLoading()
        //关闭抽屉
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        // GeocodeQuery 有两个参数 一个是当前所选城市，第二个是当前地的上级城市，
        Log.e(
            TAG,
            "onDistrictSearched: " + districtArray[index].toString() + "  ,  " + districtArray[index - 2]
        )
        val query = GeocodeQuery(districtArray[index], districtArray[index - 2])
        geocoderSearch!!.getFromLocationNameAsyn(query)

        //重置行政区
        index = 0
        //搜索行政区
        districtArray[index] = "中国"
        districtSearch(districtArray[index])
    }

    /**
     * 切换地图中心
     */
    private fun switchMapCenter(geocodeResult: GeocodeResult, latLonPoint: LatLonPoint) {
        //显示解析后的坐标，
        val latitude = latLonPoint.latitude
        val longitude = latLonPoint.longitude
        //创建经纬度对象
        val latLng = LatLng(latitude, longitude)
        //改变地图中心点
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        val mCameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(latLng, 18F, 30F, 0F))
        //在地图上添加marker
        aMap!!.addMarker(
            MarkerOptions().position(latLng).title(geocodeResult.geocodeQuery.locationName)
                .snippet("DefaultMarker")
        )
        //动画移动
        aMap!!.animateCamera(mCameraUpdate)

        //移动地图后通过坐标转地址，触发onRegeocodeSearched回调，在这个回调里去查询天气
        val query = RegeocodeQuery(latLonPoint, 20F, GeocodeSearch.AMAP)
        geocoderSearch!!.getFromLocationAsyn(query)
    }


}


