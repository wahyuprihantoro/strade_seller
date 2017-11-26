package id.strade.android.seller.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by wahyu on 22 October 2017.
 */
class Store : Serializable {
    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("status")
    @Expose
    var status: Long? = null
    @SerializedName("nameEditText")
    @Expose
    var name: String? = null
    @SerializedName("category")
    @Expose
    var category: Long? = null
    @SerializedName("open_time")
    @Expose
    var openTime: String? = null
    @SerializedName("close_time")
    @Expose
    var closeTime: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
}