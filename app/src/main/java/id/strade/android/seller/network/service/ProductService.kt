package id.strade.android.seller.network.service

import id.strade.android.seller.network.response.GetProductResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by wahyu on 18 November 2017.
 */
interface ProductService {
    @GET("products")
    fun getProducts(): Observable<GetProductResponse>
}