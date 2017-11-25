package id.strade.android.seller.model

import android.content.ClipData.Item
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by wahyu on 25 November 2017.
 */
class Order {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("seller")
    @Expose
    lateinit var seller: User
    @SerializedName("status")
    @Expose
    var status: Int = 0
    @SerializedName("total_price")
    @Expose
    var totalPrice: Int = 0
    @SerializedName("latitude")
    @Expose
    var latitude: Double = 0.0
    @SerializedName("longitude")
    @Expose
    var longitude: Double = 0.0
    @SerializedName("note")
    @Expose
    var note: String = ""
    @SerializedName("address")
    @Expose
    var address: String = ""
    @SerializedName("buyer")
    @Expose
    lateinit var buyer: User
    @SerializedName("items")
    @Expose
    lateinit var items: List<Item>
    @SerializedName("distance")
    @Expose
    var distance: Int = 0
}