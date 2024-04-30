package mx.itson.meteo.entities

import com.google.gson.annotations.SerializedName


class WeatherUnits {
    @SerializedName("temperature")
    var temperatureUnit: String? = null
    @SerializedName("windspeed")
    var windSpeedUnit: String? = null
    @SerializedName("winddirection")
    var windDirectionUnit: String? = null
}