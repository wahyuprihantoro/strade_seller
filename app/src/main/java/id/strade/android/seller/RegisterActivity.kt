package id.strade.android.seller

import android.app.ProgressDialog
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
import org.androidannotations.annotations.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        var username = usernameEditText.text.toString()
        var password = passwordEditText.text.toString()
        var phoneNumber = phoneNumberEditText.text.toString()
        var name = nameEditText.text.toString()
        apiClient.getService(AuthService::class.java).register(username = username, password = password,
                role = "seller", phoneNumber = phoneNumber, name = name)
                .enqueue(object : Callback<UserResponse> {
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if (response.isSuccessful) {
                            var resp = response.body()
                            if (resp != null && resp.status!!) {
                                Log.d("wahyu", Gson().toJson(resp))
                                prefs.token = resp.token
                                prefs.user = resp.user
                                Toast.makeText(applicationContext, "Welcome, ${resp.user?.fullName} :)", Toast.LENGTH_SHORT).show()
                                HomeActivity_.intent(applicationContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start()
                                finish()
                            } else {
                                Toast.makeText(applicationContext, resp?.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, "gagal register, ada kesalahan pada server", Toast.LENGTH_SHORT).show()
                        }
                        dialog.dismiss()
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                })
    }
}
