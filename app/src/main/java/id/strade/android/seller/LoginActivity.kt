package id.strade.android.seller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import id.strade.android.seller.network.ApiClient
import id.strade.android.seller.network.response.UserResponse
import id.strade.android.seller.network.service.AuthService
import id.strade.android.seller.storage.Prefs
import id.strade.android.seller.storage.Prefs_
import org.androidannotations.annotations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@EActivity(R.layout.activity_login)
open class LoginActivity : AppCompatActivity() {

    @Bean
    lateinit var prefs: Prefs
    @Bean
    lateinit var apiClient: ApiClient
    @ViewById(R.id.username_edit_text)
    lateinit var usernameEditText: EditText
    @ViewById(R.id.password_edit_text)
    lateinit var passwordEditText: EditText

    @AfterViews
    fun init() {

    }

    @Click(R.id.btn_login_api)
    fun login() {
        var username = usernameEditText.text.toString()
        var password = passwordEditText.text.toString()
        apiClient.getService(AuthService::class.java).login(username, password, "seller").enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    var resp = response.body()
                    if (resp != null && resp.status!!) {
                        Log.d("wahyu", Gson().toJson(resp))
                        prefs.token = resp.token
                        prefs.user = resp.user
                        Toast.makeText(applicationContext, "Berhasil login", Toast.LENGTH_SHORT).show()
                        HomeActivity_.intent(applicationContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start()
                    } else {
                        Toast.makeText(applicationContext, resp?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "gagal login, ada kesalahan pada server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
