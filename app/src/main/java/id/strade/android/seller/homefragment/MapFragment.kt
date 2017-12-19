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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import id.strade.android.seller.R
import id.strade.android.seller.model.User
import id.strade.android.seller.network.ApiClient
import id.strade.android.seller.network.response.ListUserResponse
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

    var users: List<User> = ArrayList()

    @AfterViews
    fun init() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        apiClient.getUserService().getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp: ListUserResponse ->
                    users = resp.users
                    Log.d(TAG, "success get users: " + Gson().toJson(resp))
                    users.forEach {
                        try {
                            val lat = it.location.latitude
                            val lng = it.location.longitude
                            val latLng = LatLng(lat, lng)
                            val marker = MarkerOptions().position(latLng).title(it.username)
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_buyer_orange))
                            mMap.addMarker(marker)
                        } catch (e: Exception) {
                            Log.d(TAG, "error marker: " + e.message)
                        }
                    }
                }, { e: Throwable ->
                    Log.d(TAG, "error get users: " + e.message)
                })

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
                    users.forEach {
                        try {
                            val lat = it.location.latitude
                            val lng = it.location.longitude
                            val latLng = LatLng(lat, lng)
                            val marker = MarkerOptions().position(latLng).title(it.username)
                            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_buyer_orange))
                            mMap.addMarker(marker)
                        } catch (e: Exception) {
                            Log.d(TAG, "error marker: " + e.message)
                        }
                    }
                    val marker = MarkerOptions().position(sydney).title("Lokasi anda")
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_seller_blue))
                    mMap.addMarker(marker)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
                    Log.d(TAG, "on location received")
                    apiClient.getUserService().updateLocation(location.latitude, location.longitude)
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