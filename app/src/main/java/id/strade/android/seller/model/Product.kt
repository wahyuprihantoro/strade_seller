package id.strade.android.seller.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by wahyu on 18 November 2017.
 */
class Product : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("name")
    @Expose
    lateinit var name: String
    @SerializedName("price")
    @Expose
    var price: Int = 0
    @SerializedName("image_url")
    @Expose
    lateinit var imageUrl: String
}