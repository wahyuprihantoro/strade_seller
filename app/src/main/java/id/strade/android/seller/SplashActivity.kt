package id.strade.android.seller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import id.strade.android.seller.storage.Prefs
import id.strade.android.seller.storage.Prefs_
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import java.util.*
import kotlin.concurrent.timerTask

@EActivity(R.layout.activity_splash)
open class SplashActivity : AppCompatActivity() {

    @Bean
    lateinit var prefs: Prefs

    @AfterViews
    fun init() {
        val timer = Timer()
        timer.schedule(timerTask {
            if (prefs.token.isEmpty()) {
                IntroActivity_.intent(applicationContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start()
            } else {
                HomeActivity_.intent(applicationContext).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start()
            }
        }, 1500)
    }
}
