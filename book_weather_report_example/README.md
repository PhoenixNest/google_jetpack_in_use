# Weather Report (Kotlin)

## App 架构

本项目使用`MVVM组织`架构进行编写。

### Ui 层

+ MainActivity - [PlaceFragment]

+ WeatherActivity

---

### 数据层

+ PlaceViewModel -> 对应 PlaceFragment

+ WeatherViewModel -> 对应 WeatherActivity

+ Repository -> 管理 Place、Weather 两个 ViewModel

+ PlaceDao -> 本地持久化SharePreferences

+ WeatherNetwork [Retrofit] -> 天气API
