package id.strade.android.seller

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import id.strade.android.seller.homefragment.HomeFragment
import id.strade.android.seller.network.ApiClient
import id.strade.android.seller.network.response.BaseResponse
import id.strade.android.seller.network.service.ProductService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.*
import retrofit2.HttpException
import java.io.ByteArrayOutputStream


@EActivity(R.layout.activity_create_product)
open class CreateProductActivity : AppCompatActivity() {
    companion object {
        private val RC_CAMERA = 3000
        private val REQUEST_IMAGE_CAPTURE = 123
    }

    @ViewById
    lateinit var nameEditText: EditText
    @ViewById
    lateinit var priceEditText: EditText
    @ViewById
    lateinit var productImageView: ImageView

    @Bean
    lateinit var apiClient: ApiClient

    private lateinit var encodedImage: String
    private lateinit var dialog: ProgressDialog


    @AfterViews
    fun init() {
        dialog = ProgressDialog(this)
        encodedImage = ""
    }

    @Click
    fun takePicture() {
        Log.d("wahyu", "take picture button is clicked")
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, RC_CAMERA)
        } else {
            captureImage()
        }
    }

    private fun showDialog() {
        dialog.run {
            setTitle(null)
            setMessage("Loading...")
            show()
        }
    }

    @Click
    fun create() {
        showDialog()
        val name = nameEditText.text.toString()
        val price = priceEditText.text.toString()

        val createProductObs = apiClient.getService(ProductService::class.java)
                .createProducts(name = name, price = price, image = encodedImage)
        createProductObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ baseResponse: BaseResponse ->
                    onCreateProductSuccess(baseResponse)
                }, { e: Throwable ->
                    onCreateProductFailed(e)
                })
    }

    private fun onCreateProductFailed(e: Throwable) {
        if (e is HttpException) {
            val rawResponse = e.response().errorBody()?.charStream()?.readText()
            val resp = Gson().fromJson(rawResponse, BaseResponse::class.java)
            Toast.makeText(applicationContext, resp.message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
        dialog.dismiss()
    }

    private fun onCreateProductSuccess(baseResponse: BaseResponse) {
        if (baseResponse.status) {
            Toast.makeText(applicationContext, "Berhasil membuat product!", Toast.LENGTH_SHORT).show()
            setResult(HomeFragment.SUCCESS_RESULT_CODE)
            finish()
        }
        Log.d("wahyu", Gson().toJson(baseResponse))
        dialog.dismiss()
    }


    private fun captureImage() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePhotoIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == RC_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("wahyu", "wahyu")
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras.get("data") as Bitmap
            productImageView.setImageBitmap(imageBitmap)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val b = baos.toByteArray()
            encodedImage = Base64.encodeToString(b, Base64.NO_WRAP)
            Log.d("wahyu", encodedImage)
        }
    }
}
