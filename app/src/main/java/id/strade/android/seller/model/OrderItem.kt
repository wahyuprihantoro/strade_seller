package id.strade.android.seller.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by wahyu on 25 November 2017.
 */
class OrderItem : Serializable{
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("count")
    @Expose
    var count: Int = 0
    @SerializedName("item")
    @Expose
    lateinit var product: Product
}