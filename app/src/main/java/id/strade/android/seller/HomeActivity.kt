package id.strade.android.seller

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.roughike.bottombar.BottomBar
import id.strade.android.seller.homefragment.AccountFragment_
import id.strade.android.seller.homefragment.HomeFragment_
import id.strade.android.seller.homefragment.MapFragment_
import id.strade.android.seller.homefragment.OrderFragment_
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById


@EActivity(R.layout.activity_home)
open class HomeActivity : AppCompatActivity() {
    @ViewById(R.id.bottom_bar)
    lateinit var bottomBar: BottomBar
    @ViewById(R.id.view_pager)
    lateinit var viewPager: ViewPager

    @AfterViews
    fun init() {
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        setUpBottomBar()
        Log.d("wahyu fb", FirebaseInstanceId.getInstance().token)
    }

    fun changeTitle(item: Int, title: String) {
        supportActionBar?.title = title
        viewPager.currentItem = item
    }

    private fun setUpBottomBar() {
        bottomBar.setOnTabSelectListener { tabId ->
            when (tabId) {
                R.id.tab_home -> changeTitle(0, "Daftar Produk")
                R.id.tab_map -> changeTitle(1, "Maps Pelanggan")
                R.id.tab_request -> changeTitle(2, "Daftar Pesanan")
                R.id.tab_account -> changeTitle(3, "Setting")
            }
        }
    }

    private inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment_.builder().build()
                1 -> MapFragment_.builder().build()
                2 -> OrderFragment_.builder().build()
                else -> AccountFragment_.builder().build()
            }
        }

        override fun getCount() = 4
    }

}
