package id.strade.android.seller

import android.Manifest
import android.app.ProgressDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.system.ErrnoException
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import id.strade.android.seller.network.ApiClient
import id.strade.android.seller.network.response.BaseResponse
import id.strade.android.seller.network.service.ProductService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.*
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException


@EActivity(R.layout.activity_create_product)
open class CreateProductActivity : AppCompatActivity() {
    private val REQUEST_CODE_PICKER = 8723
    private val RC_CAMERA = 3000

    @ViewById
    lateinit var name: EditText
    @ViewById
    lateinit var price: EditText
    @ViewById
    lateinit var image: ImageView
    private lateinit var encImage: String


    @Bean
    lateinit var apiClient: ApiClient
    lateinit var dialog: ProgressDialog

    @AfterViews
    fun init() {
        dialog = ProgressDialog(this)
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
        val n = name.text.toString()
        val p = price.text.toString()
        val img = encImage

        val createProductObs = apiClient.getService(ProductService::class.java).createProducts(name = n, price = p, image = img)
        createProductObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ baseResponse: BaseResponse ->
                    onLoginSuccess(baseResponse)
                }, { e: Throwable ->
                    onLoginFailed(e)
                })
    }

    private fun onLoginFailed(e: Throwable) {
        if (e is HttpException) {
            val rawResponse = e.response().errorBody()?.charStream()?.readText()
            val resp = Gson().fromJson(rawResponse, BaseResponse::class.java)
            Toast.makeText(applicationContext, resp.message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
        dialog.dismiss()
    }

    private fun onLoginSuccess(baseResponse: BaseResponse) {
        if (baseResponse.status) {
            Toast.makeText(applicationContext, "Success gan!", Toast.LENGTH_SHORT).show()
//            HomeActivity_.intent(applicationContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start()
            setResult(21)
            finish()
        }
        Log.d("wahyu", Gson().toJson(baseResponse))
        dialog.dismiss()
    }

    @Click
    fun takePicture() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, RC_CAMERA)
        } else {
            captureImage()
        }
    }

    private val REQUEST_IMAGE_CAPTURE = 123

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


    private fun getPickImageResultUri(data: Intent): Uri? {
        var isCamera = true
        if (data.data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }
        if (isCamera) {
            return getCaptureImageOutputUri()
        } else {

        }
        return data.data
    }

    private fun isUriRequiresPermissions(uri: Uri): Boolean {
        try {
            val resolver = contentResolver
            val stream = resolver.openInputStream(uri)
            stream.close()
            return false
        } catch (e: FileNotFoundException) {
            if (e.cause is ErrnoException) {
                return true
            }
        } catch (e: Exception) {
        }
        return false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("wahyu", "wahyu")
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data!!.extras
            val imageBitmap = extras.get("data") as Bitmap
            image.setImageBitmap(imageBitmap)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val b = baos.toByteArray()
            encImage = Base64.encodeToString(b, Base64.NO_WRAP)
            Log.d("wahyu", encImage)

        }

    }


    private fun getCaptureImageOutputUri(): Uri? {
        var outputFileUri: Uri? = null
        val getImage = externalCacheDir
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage.path, "pickImageResult.jpeg"))
        }
        return outputFileUri
    }

    private fun getPickImageChooserIntent(): Intent {

        val outputFileUri = getCaptureImageOutputUri()

        val allIntents = ArrayList<Intent>()
        val packageManager = packageManager

        val captureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName (res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }

        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.`package` = res.activityInfo.packageName
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            }
            allIntents.add(intent)
        }
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        val listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (res in listGallery) {
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.`package` = res.activityInfo.packageName
            allIntents.add(intent)
        }

        var mainIntent = allIntents[allIntents.size - 1]
        for (intent in allIntents) {
            if (intent.component.className == "com.android.documentsui.DocumentsActivity") {
                mainIntent = intent
                break
            }
        }
        allIntents.remove(mainIntent)
        val chooserIntent = Intent.createChooser(mainIntent, "Select source")

//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray( Parcelable[allIntents.size()]))
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents)
        return chooserIntent
    }
}
