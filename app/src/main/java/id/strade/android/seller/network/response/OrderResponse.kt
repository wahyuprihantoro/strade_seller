package id.strade.android.seller.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.strade.android.seller.model.Order

/**
 * Created by wahyu on 25 November 2017.
 */
class OrderResponse : BaseResponse() {
    @SerializedName("orders")
    @Expose
    lateinit var orders: List<Order>
}