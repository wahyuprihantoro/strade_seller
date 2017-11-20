package id.strade.android.seller.network

import id.strade.android.seller.storage.Prefs
import okhttp3.OkHttpClient
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by wahyu on 19 October 2017.
 */
@EBean
open class ApiClient {

    //    val BASE_URL = "http://159.89.200.247/api/"
    private val BASE_URL = "http://10.0.3.2:8000/api/"

    @Bean
    lateinit var prefs: Prefs

    fun <T> getService(service: Class<T>): T {
        return getRetrofit().create(service)
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "jwt " + prefs.token)
                    .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        return httpClient.build()
    }

}