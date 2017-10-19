package id.strade.android.seller

import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_main)
open class MainActivity : AppCompatActivity() {

    @AfterViews
    fun init() {
        Log.d("wahyu", "masuk activity")
    }
}
