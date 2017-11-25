package id.strade.android.seller.network.service

import id.strade.android.seller.network.response.OrderResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by wahyu on 25 November 2017.
 */
interface OrderService {
    @GET("orders")
    fun getOrders(): Observable<OrderResponse>
}