package id.strade.android.seller.network

import android.util.Log
import org.androidannotations.annotations.EBean
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * Created by wahyu on 19 October 2017.
 */
@EBean
open class ApiClient {

    val BASE_URL: String = "http://159.89.200.247/api/"

    fun <T> getService(service: Class<T>): T {
        return getRetrofit().create(service)
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun login(username:String,password:String,role:String){
        var response:JSONObject
        val retrofit = getRetrofit()
        val loginService = retrofit.create(LoginService::class.java)
        val call = loginService.login(username,password,role)
        var body: Response<JSONObject>? = null
        try {
            body = call.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try{
            response = body!!.body()!!
            val isSuccess = Integer.toString(body!!.code())
            Log.i("SUCCESS", isSuccess)
            Log.i("BODY", body!!.message())
        } catch (e: Exception){
            e.printStackTrace()
        }

    }
}