package id.strade.android.seller.homefragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import id.strade.android.seller.R
import id.strade.android.seller.network.ApiClient
import id.strade.android.seller.network.response.LocationResponse
import id.strade.android.seller.service.LocationService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment

@EFragment(R.layout.fragment_map)
open class MapFragment : Fragment(), OnMapReadyCallback {
    private val TAG = "wahyu " + MapFragment::class.java.simpleName
    private lateinit var mMap: GoogleMap

    @Bean
    lateinit var locationService: LocationService

    @Bean
    lateinit var apiClient: ApiClient

    @AfterViews
    fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    212)
        } else {
            locationService.setListener(object : LocationService.LocationChangeListener {
                override fun onLocationChange(location: Location) {
                    val sydney = LatLng(location.latitude, location.longitude)
                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(sydney).title("Lokasi anda"))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14f))
                    Log.d(TAG, "on location received")
                    apiClient.getUserService().createProducts(location.latitude, location.longitude)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ resp: LocationResponse ->
                                Log.d(TAG, "success update location: " + Gson().toJson(resp))
                            }, { e: Throwable ->
                                Log.d(TAG, "error update location: " + e.message)
                            })
                }
            })
            locationService.init(activity)
            Log.d(TAG, "permission granted")
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d(TAG, "Maps is ready")
        mMap.setOnCameraIdleListener {
            Log.d(TAG, "maps idle")
            Log.d(TAG, mMap.projection.visibleRegion.farRight.toString())
            Log.d(TAG, mMap.projection.visibleRegion.nearLeft.toString())
            val farRight = mMap.projection.visibleRegion.farRight
            val nearLeft = mMap.projection.visibleRegion.nearLeft
        }
    }

}