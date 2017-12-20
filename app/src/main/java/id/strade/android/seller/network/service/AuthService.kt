package id.strade.android.seller.network.service

import id.strade.android.seller.network.response.UserResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by wahyu on 22 October 2017.
 */
interface AuthService {
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("username") username: String,
              @Field("password") password: String,
              @Field("role") role: String): Observable<UserResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(@Field("username") username: String,
                 @Field("name") name: String,
                 @Field("phone_number") phoneNumber: String,
                 @Field("password") password: String,
                 @Field("role") role: String): Observable<UserResponse>
}