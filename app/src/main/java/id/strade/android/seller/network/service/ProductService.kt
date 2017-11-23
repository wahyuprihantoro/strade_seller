package id.strade.android.seller.network.service

import id.strade.android.seller.network.response.BaseResponse
import id.strade.android.seller.network.response.GetProductResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by wahyu on 18 November 2017.
 */
interface ProductService {
    @GET("products")
    fun getProducts(): Observable<GetProductResponse>

    @FormUrlEncoded
    @POST("products")
    fun createProducts(@Field("nameEditText") name: String,
                       @Field("priceEditText") price: String,
                       @Field("productImageView") image: String): Observable<BaseResponse>
}