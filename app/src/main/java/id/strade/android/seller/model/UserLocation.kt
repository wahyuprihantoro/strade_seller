package id.strade.android.seller.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by wahyu on 25 November 2017.
 */
class UserLocation : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int = 0
    @SerializedName("latitude")
    @Expose
    var latitude: Double = 0.0
    @SerializedName("longitude")
    @Expose
    var longitude: Double = 0.0
    @SerializedName("current_address")
    @Expose
    lateinit var currentAddress: String
}