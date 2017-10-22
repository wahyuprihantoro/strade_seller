package id.strade.android.seller

import android.support.v7.app.AppCompatActivity
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity

@EActivity(R.layout.activity_home)
open class HomeActivity : AppCompatActivity() {

    @AfterViews
    fun init() {

    }
}
