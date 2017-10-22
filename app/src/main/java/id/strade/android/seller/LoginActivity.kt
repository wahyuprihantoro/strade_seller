package id.strade.android.seller

import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import id.strade.android.seller.network.ApiClient_
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity


@EActivity(R.layout.activity_login)
open class LoginActivity : AppCompatActivity() {

    @AfterViews
    fun init() {

    }

    @Click(R.id.btn_login_api)
    fun login(){
        var username = findViewById<EditText>(R.id.username_edit_text).text.toString()
        var password = findViewById<EditText>(R.id.password_edit_text).text.toString()
        Log.i("login", "user :" +username+ " LOGIN API")
        var api = ApiClient_.getInstance_(this)
        api.login(username,password,"seller")
    }
}
