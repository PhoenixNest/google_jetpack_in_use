package com.example.book_weather_report_example.logic

import androidx.lifecycle.liveData
import com.example.book_weather_report_example.logic.dao.PlaceDao
import com.example.book_weather_report_example.logic.model.Place
import com.example.book_weather_report_example.logic.model.Weather
import com.example.book_weather_report_example.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

// 仓库层有点像是一个数据获取与缓存的中间层，在本地没有缓存数据的情况下就去网络层获取，
// 如果本地已经有缓存了，就直接将缓存数据返回。
object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = WeatherNetwork.searchPlaces(query)

            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }

        // 类似于 LiveData 的 SetValue()方法 通知数据变化
        emit(result)
    }

    // 保存城市信息到SP
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavePlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    fun refreshWeather(lng: String, lat: String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                val deferredRealtime = async {
                    WeatherNetwork.getRealtimeWeather(lng, lat)
                }

                val deferredDaily = async {
                    WeatherNetwork.getDailyWeather(lng, lat)
                }

                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()

                if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                    val weather = Weather(
                        realtime = realtimeResponse.result.realtime,
                        daily = dailyResponse.result.daily
                    )
                    Result.success(weather)
                } else {
                    Result.failure(
                        RuntimeException(
                            "realTime response status is ${realtimeResponse.status}, " +
                                    "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Result.failure<Weather>(e)
        }

        // 类似于 LiveData 的 SetValue()方法 通知数据变化
        emit(result)
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>> {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }

            emit(result)
        }
}