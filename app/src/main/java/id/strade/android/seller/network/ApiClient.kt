package id.strade.android.seller.network

import org.androidannotations.annotations.EBean
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

}