package id.strade.android.seller.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by wahyu on 18 November 2017.
 */
open class BaseResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean = false
    @SerializedName("message")
    @Expose
    lateinit var message: String
}