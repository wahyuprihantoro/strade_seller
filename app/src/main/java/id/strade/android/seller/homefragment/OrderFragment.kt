package id.strade.android.seller.homefragment

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import id.strade.android.seller.R
import id.strade.android.seller.adapter.OrderAdapter
import id.strade.android.seller.network.ApiClient
import id.strade.android.seller.network.response.BaseResponse
import id.strade.android.seller.network.response.OrderResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById
import retrofit2.HttpException

@EFragment(R.layout.fragment_order)
open class OrderFragment : Fragment() {

    @ViewById
    lateinit var rv: RecyclerView

    @Bean
    lateinit var apiClient: ApiClient

    @AfterViews
    fun init() {
        rv.layoutManager = LinearLayoutManager(context)
        apiClient.getOrderService().getOrders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ orderResponse: OrderResponse ->
                    showOrders(orderResponse)
                }, { e: Throwable ->
                    showLoadOrdersFailed(e)
                })

    }

    private fun showLoadOrdersFailed(e: Throwable) {
        if (e is HttpException) {
            if (e.code() == 401) {
                Toast.makeText(context, "Sesi anda telah berakhir, silakan login kembali.", Toast.LENGTH_SHORT).show()
                activity.finish()
            } else {
                val raw = e.response().errorBody()?.charStream()?.readText()
                val resp = Gson().fromJson(raw, BaseResponse::class.java)
                Toast.makeText(context, resp.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("wahyu error", e.message)
            Toast.makeText(context, "Terjadi kesalahan pada koneksi.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showOrders(orderResponse: OrderResponse) {
        val adapter = OrderAdapter(context, orderResponse.orders)
        rv.adapter = adapter
    }

}