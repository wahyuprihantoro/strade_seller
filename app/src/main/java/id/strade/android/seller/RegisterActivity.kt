package id.strade.android.seller

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import id.strade.android.seller.network.ApiClient
import id.strade.android.seller.network.response.BaseResponse
import id.strade.android.seller.network.response.UserResponse
import id.strade.android.seller.network.service.AuthService
import id.strade.android.seller.storage.Prefs
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.*
import retrofit2.HttpException

@EActivity(R.layout.activity_register)
open class RegisterActivity : AppCompatActivity() {

    @Bean
    lateinit var prefs: Prefs
    @Bean
    lateinit var apiClient: ApiClient
    @ViewById(R.id.username_edit_text)
    lateinit var usernameEditText: EditText
    @ViewById(R.id.password_edit_text)
    lateinit var passwordEditText: EditText
    @ViewById(R.id.name_edit_text)
    lateinit var nameEditText: EditText
    @ViewById(R.id.phone_number_edit_text)
    lateinit var phoneNumberEditText: EditText

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

    @Click(R.id.register)
    fun register() {
        showDialog()
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val name = nameEditText.text.toString()
        val registerObs = apiClient.getAuthService().register(username = username, password = password,
                role = "seller", phoneNumber = phoneNumber, name = name)
        registerObs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userResponse: UserResponse ->
                    onRegisterSuccess(userResponse)
                }, { e: Throwable ->
                    onRegisterFailed(e)
                })
    }

    private fun onRegisterFailed(e: Throwable) {
        if (e is HttpException) {
            val resp = e.response().errorBody()?.charStream()?.readText()
            var baseResponse = Gson().fromJson(resp, BaseResponse::class.java)
            Toast.makeText(applicationContext, baseResponse.message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
        dialog.dismiss()
    }

    private fun onRegisterSuccess(userResponse: UserResponse) {
        if (userResponse.status) {
            prefs.token = userResponse.token
            prefs.user = userResponse.user
            Toast.makeText(applicationContext, "Selamat datang, ${userResponse.user.fullName}!", Toast.LENGTH_SHORT).show()
            HomeActivity_.intent(applicationContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start()
            finish()
        }
        Log.d("wahyu", Gson().toJson(userResponse))
    }
}
