package id.strade.android.seller.homefragment

import android.support.v4.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import id.strade.android.seller.IntroActivity_
import id.strade.android.seller.R
import id.strade.android.seller.storage.Prefs
import org.androidannotations.annotations.*


@EFragment(R.layout.fragment_account)
open class AccountFragment : Fragment() {

    @Bean
    lateinit var prefs: Prefs
    @ViewById(R.id.photo_profile)
    lateinit var profilePictureImageView: ImageView
    @ViewById(R.id.cover)
    lateinit var coverImageView: ImageView
    @ViewById(R.id.name)
    lateinit var nameView: TextView

    @AfterViews
    fun init() {
        val user = prefs.user
        Glide.with(this)
                .load(user.imageUrl)
                .into(profilePictureImageView)
        Glide.with(this)
                .load(user.store?.imageUrl)
                .into(coverImageView)
        nameView.text = user.fullName
    }


    @Click
    fun logout() {
        IntroActivity_.intent(activity).start()
        activity.finish()
        prefs.logout()
    }

}