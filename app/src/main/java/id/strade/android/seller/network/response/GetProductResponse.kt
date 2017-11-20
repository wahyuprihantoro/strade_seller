package id.strade.android.seller.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.strade.android.seller.model.Product

/**
 * Created by wahyu on 22 October 2017.
 */
class GetProductResponse : BaseResponse() {
    @SerializedName("products")
    @Expose
    lateinit var products: List<Product>
}