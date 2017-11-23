package id.strade.android.seller.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by wahyu on 18 November 2017.
 */
class Product {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("nameEditText")
    @Expose
    lateinit var name: String
    @SerializedName("priceEditText")
    @Expose
    var price: Int = 0
    @SerializedName("image_url")
    @Expose
    lateinit var imageUrl: String
}