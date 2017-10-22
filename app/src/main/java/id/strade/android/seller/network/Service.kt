package id.strade.android.seller.network

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST




/**
 * Created by ARSnova on 22/10/2017.
 */
interface LoginService {
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("username") username: String,
              @Field("password") password: String,
              @Field("role") role: String): Call<JSONObject>
}