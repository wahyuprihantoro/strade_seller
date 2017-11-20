package id.strade.android.seller.homefragment

import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.google.gson.Gson
import id.strade.android.seller.CreateProductActivity_
import id.strade.android.seller.R
import id.strade.android.seller.adapter.ProductAdapter
import id.strade.android.seller.network.ApiClient
import id.strade.android.seller.network.response.BaseResponse
import id.strade.android.seller.network.response.GetProductResponse
import id.strade.android.seller.network.service.ProductService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.*
import retrofit2.HttpException

@EFragment(R.layout.fragment_home)
open class HomeFragment : Fragment() {

    @ViewById
    lateinit var rv: RecyclerView

    @Bean
    lateinit var apiClient: ApiClient

    @AfterViews
    fun init() {
        rv.layoutManager = LinearLayoutManager(context)
        val producsResponseObs = apiClient.getService(ProductService::class.java).getProducts()
        producsResponseObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp: GetProductResponse ->
                    if (resp.status) {
                        val products = resp.products
                        val adapter = ProductAdapter(context, products)
                        rv.adapter = adapter
                    }
                }, { e: Throwable ->
                    if (e is HttpException) {
                        val rawResponse = e.response().errorBody()?.charStream()?.readText()
                        val resp = Gson().fromJson(rawResponse, BaseResponse::class.java)
                        Toast.makeText(context, resp.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                })
    }

    @Click
    fun fab() {
        CreateProductActivity_.intent(context).start()
    }
}