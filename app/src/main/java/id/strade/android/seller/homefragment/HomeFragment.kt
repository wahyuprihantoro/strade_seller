package id.strade.android.seller.homefragment

import android.content.Intent
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

    companion object {
        val CREATE_TASK_REQUEST_CODE = 800
        val SUCCESS_RESULT_CODE = 200
    }

    @AfterViews
    fun init() {
        rv.layoutManager = LinearLayoutManager(context)
        apiClient.getProductService().getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp: GetProductResponse ->
                    showProducts(resp)
                }, { e: Throwable ->
                    showGetProductFailed(e)
                })
    }

    private fun showGetProductFailed(e: Throwable) {
        if (e is HttpException) {
            val rawResponse = e.response().errorBody()?.charStream()?.readText()
            val resp = Gson().fromJson(rawResponse, BaseResponse::class.java)
            Toast.makeText(context, resp.message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProducts(resp: GetProductResponse) {
        if (resp.status) {
            val products = resp.products
            val adapter = ProductAdapter(context, products)
            rv.adapter = adapter
        }
    }

    @Click
    fun fab() {
        startActivityForResult(CreateProductActivity_.intent(context).get(), CREATE_TASK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_TASK_REQUEST_CODE && resultCode == SUCCESS_RESULT_CODE) {
            init()
        }
    }
}