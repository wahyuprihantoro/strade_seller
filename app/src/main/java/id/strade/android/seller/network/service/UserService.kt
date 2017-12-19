package id.strade.android.seller.network.service

import id.strade.android.seller.network.response.ListUserResponse
import id.strade.android.seller.network.response.LocationResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by wahyu on 19 December 2017.
 */
interface UserService {
    @FormUrlEncoded
    @POST("user/location")
    fun updateLocation(@Field("latitude") latitude: Double,
                       @Field("longitude") longitude: Double): Observable<LocationResponse>

    @GET("users")
    fun getUsers(): Observable<ListUserResponse>
}