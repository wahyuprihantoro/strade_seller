package id.strade.android.seller.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by wahyu on 22 October 2017.
 */
class User : Serializable {
    @SerializedName("id")
    @Expose
    var id: Long? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("full_name")
    @Expose
    var fullName: String? = null
    @SerializedName("role")
    @Expose
    var role: Long? = null
    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null
    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
    @SerializedName("store")
    @Expose
    var store: Store? = null
    @SerializedName("location")
    @Expose
    var location: UserLocation = UserLocation()
}