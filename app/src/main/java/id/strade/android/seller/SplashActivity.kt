package id.strade.android.seller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import java.util.*
import kotlin.concurrent.timerTask

@EActivity(R.layout.activity_splash)
open class SplashActivity : AppCompatActivity() {

    @AfterViews
    fun init() {
        val timer = Timer()
        timer.schedule(timerTask {
            IntroActivity_.intent(applicationContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start()
        }, 1500)
    }
}
