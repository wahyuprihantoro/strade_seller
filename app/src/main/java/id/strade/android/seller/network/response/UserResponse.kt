package id.strade.android.seller.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.strade.android.seller.model.User


/**
 * Created by wahyu on 22 October 2017.
 */
class UserResponse : BaseResponse() {
    @SerializedName("user")
    @Expose
    lateinit var user: User
    @SerializedName("token")
    @Expose
    lateinit var token: String
}