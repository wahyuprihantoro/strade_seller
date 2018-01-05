package id.strade.android.seller

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import id.strade.android.seller.adapter.ProductAdapter
import id.strade.android.seller.chat.ChatActivity_
import id.strade.android.seller.model.Order
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_order_detail)
open class OrderDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private val MARKER_TITLE = "Lokasi pembeli"

    @ViewById
    lateinit var rvItem: RecyclerView
    @ViewById
    lateinit var locationTextView: TextView
    @ViewById
    lateinit var additionalInfoTextView: TextView

    @Extra
    lateinit var order: Order

    open lateinit var mMap: GoogleMap

    @AfterViews
    fun init() {
        if (supportActionBar != null) {
            supportActionBar!!.title = "Detail Pesanan - ${order.buyer.fullName}"
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        rvItem.layoutManager = LinearLayoutManager(this)
        rvItem.adapter = ProductAdapter(this, order.items.map { orderItem -> orderItem.product })
        locationTextView.text = order.address
        additionalInfoTextView.text = order.note

        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(maps: GoogleMap) {
        mMap = maps
        Log.d("wahyu", order.latitude.toString() + " " + order.longitude.toString())
        val latLng = LatLng(order.latitude, order.longitude)
        mMap.addMarker(MarkerOptions().position(latLng).title(MARKER_TITLE))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
    }


    @Click
    fun btnChat(){
        ChatActivity_.intent(this).order(order).start()
    }
}
