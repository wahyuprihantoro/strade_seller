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
import id.strade.android.seller.service.LocationService
import id.strade.android.seller.R
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment

@EFragment(R.layout.fragment_map)
open class MapFragment : Fragment(), OnMapReadyCallback {
    private val TAG = MapFragment::class.java.simpleName
    private lateinit var mMap: GoogleMap

    @Bean
    lateinit var locationService: LocationService

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
                }
            })
            locationService.init(activity)
            Log.d(TAG, "permission granted")
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d(TAG, "Maps is ready")
    }

}