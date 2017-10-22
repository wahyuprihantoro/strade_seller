package id.strade.android.seller

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.roughike.bottombar.BottomBar
import id.strade.android.seller.homefragment.AccountFragment_
import id.strade.android.seller.homefragment.HomeFragment_
import id.strade.android.seller.homefragment.MapFragment_
import id.strade.android.seller.homefragment.RequestFragment_
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
    }


    fun setUpBottomBar() {
        bottomBar.setOnTabSelectListener { tabId ->
            when (tabId) {
                R.id.tab_home -> viewPager.currentItem = 0
                R.id.tab_map -> viewPager.currentItem = 1
                R.id.tab_request -> viewPager.currentItem = 2
                R.id.tab_account -> viewPager.currentItem = 3
            }
        }
    }

    private inner class ViewPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return HomeFragment_.builder().build()
                1 -> return MapFragment_.builder().build()
                2 -> return RequestFragment_.builder().build()
                else -> return AccountFragment_.builder().build()
            }
        }

        override fun getCount(): Int {
            return 4
        }
    }

}
