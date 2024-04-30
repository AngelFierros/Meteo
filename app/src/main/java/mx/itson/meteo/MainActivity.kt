package mx.itson.meteo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import mx.itson.meteo.entities.Location
import mx.itson.meteo.entities.WeatherUnits
import mx.itson.meteo.utils.RetrofitUtils
import mx.itson.meteo.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    var context : Context = this
    var map : GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getLocation("27.9681409", "-110.9189332")
    }

    fun getLocation(lat: String, lon: String){

        val call : Call<Location> = RetrofitUtils.getApi()!!.getLocation(lat, lon, true)
        call.enqueue(object : Callback<Location>{
            override fun onResponse(call: Call<Location>, response: Response<Location>) {
                try{
                    val location : Location? = response.body()
                    Toast.makeText(context,"La temperatura es: " + location!!.weather!!.temperature + location.weatherUnits!!.temperatureUnit +
                            "\nVelocidad del viento: " + location!!.weather!!.windSpeed + location.weatherUnits!!.windSpeedUnit +
                            "\nDireccion del viento: " + location!!.weather!!.windDirection + location.weatherUnits!!.windDirectionUnit, Toast.LENGTH_LONG).show()
                }catch (e: Exception){
                    Log.e("error", e.message.toString())
                }

            }


            override fun onFailure(call: Call<Location>, t: Throwable) {
                Log.e("error", t.message.toString())

            }

        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            map = googleMap
            map?.mapType = GoogleMap.MAP_TYPE_HYBRID

            map?.clear()
            val latLong = LatLng(27.9681409, -110.9189332)
            map?.addMarker(MarkerOptions().position(latLong).draggable(true))
            map?.moveCamera(CameraUpdateFactory.newLatLng(latLong))
            map?.animateCamera(CameraUpdateFactory.zoomTo(12f))
            map?.setOnMarkerDragListener(object: OnMarkerDragListener{
                override fun onMarkerDrag(marker: Marker) {}
                override fun onMarkerDragEnd(marker: Marker) {
                    val latLng = marker.position
                    getLocation(latLng.latitude.toString(), latLng.latitude.toString())
                }
                override fun onMarkerDragStart(marker: Marker) {}
            })
        }catch (ex: Exception){
            Log.e("Error loading map", ex.message.toString())
        }
    }


}