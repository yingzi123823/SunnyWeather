package com.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocationClient
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.sunnyweather.android.MainActivity
import com.sunnyweather.android.databinding.FragmentPlaceBinding
import com.sunnyweather.android.logic.model.Location
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlacedFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter
    private lateinit var binding: FragmentPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        adapter = PlaceAdapter(this, viewModel.placeList)
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        binding.recyclerview.adapter = adapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(activity is MainActivity && viewModel.isPlaceSaved()){
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity( intent)
            activity?.finish()
            return
        }
        // 搜索框文本变化监听
        binding.searchPlaceEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val content = s.toString().trim()
                if (content.isNotEmpty()) {
                    searchPlaces(content)
                } else {
                    binding.recyclerview.visibility = View.GONE
                    binding.bgImageView.visibility = View.VISIBLE
                    viewModel.placeList.clear()
                    adapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                binding.recyclerview.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
    private fun searchPlaces(query: String) {
        val queryInfo = PoiSearch.Query(query, "", "")
        queryInfo.pageSize = 20
        queryInfo.pageNum = 1
        val poiSearch = PoiSearch(requireContext(), queryInfo)
        poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiSearched(result: PoiResult?, rCode: Int) {
                if (rCode == 1000) {
                    result?.pois?.let { poiItems ->
                        // 将PoiItem转换为自定义Place列表（与你的搜索结果格式一致）
                        val places = poiItems.map { item ->
                            Place(
                                name = item.title,
                                location = Location(
                                    lng = item.latLonPoint.longitude.toString(),
                                    lat = item.latLonPoint.latitude.toString()
                                ),
                                address = item.snippet
                            )
                        }
                        // 更新ViewModel中的数据源
                        viewModel.placeList.clear()
                        viewModel.placeList.addAll(places)
                        // 在主线程通知适配器刷新
                        requireActivity().runOnUiThread {
                            adapter.notifyDataSetChanged()
                            // 显示列表，隐藏背景图
                            binding.recyclerview.visibility = View.VISIBLE
                            binding.bgImageView.visibility = View.GONE
                        }
                    }
                } else {
                    Log.e("PlacedFragment", "搜索失败，错误码：$rCode")
                    requireActivity().runOnUiThread {
                        Toast.makeText(activity, "搜索失败，错误码：$rCode", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {}
        })
        poiSearch.searchPOIAsyn()
    }
}