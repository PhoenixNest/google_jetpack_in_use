package com.example.book_weather_report_example.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.book_weather_report_example.MainActivity
import com.example.book_weather_report_example.R
import com.example.book_weather_report_example.WeatherActivity
import com.example.book_weather_report_example.logic.place.PlaceViewModel
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 只有当PlaceFragment被嵌入MainActivity中，并且之前已经存在选中的城市，
        // 此时才会直接跳转到WeatherActivity，这样就可以解决无限循环跳转的问题了。
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }

            startActivity(intent)
            activity?.finish()
            return
        }

        // 若用户此前有在本App上搜索城市，则无需再次进行搜索，后续开启App后会自动跳转至历史搜索的城市（即：WeatherActivity）
        if (viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter

        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                // 查找当前输入城市
                viewModel.searchPlaces(content)
            } else {
                // 无输入值，重置默认Ui
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }

            viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
                val places = result.getOrNull()

                if (places != null) {
                    recyclerView.visibility = View.VISIBLE
                    bgImageView.visibility = View.GONE
                    viewModel.placeList.clear()
                    viewModel.placeList.addAll(places)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                    result.exceptionOrNull()?.printStackTrace()
                }
            })
        }
    }
}