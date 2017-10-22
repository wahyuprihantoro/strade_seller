package id.strade.android.seller

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Click
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById
import java.util.*
import kotlin.concurrent.timerTask


@EActivity(R.layout.activity_intro)
open class IntroActivity : AppCompatActivity() {

    @ViewById(R.id.view_pager)
    lateinit var viewPager: ViewPager

    @ViewById(R.id.dots_layout)
    lateinit var dotsLayout: LinearLayout

    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    private lateinit var dots: Array<TextView>
    private val layouts: IntArray = intArrayOf(R.layout.fragment_intro_1, R.layout.fragment_intro_2, R.layout.fragment_intro_3)

    @AfterViews
    fun init() {
        if (layouts.size > 0) {
            addBottomDots(0)
        }

        introViewPagerAdapter = IntroViewPagerAdapter()
        viewPager.adapter = introViewPagerAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                addBottomDots(position)
            }
        })
        animateSlider()
    }

    private fun animateSlider() {
        val timer = Timer()
        var page = viewPager.currentItem + 1
        timer.schedule(timerTask {
            if (page > 2) page = 0
            runOnUiThread {
                viewPager.setCurrentItem(page, true)
            }
            animateSlider()
        }, 3000)
    }


    @Click(R.id.btn_register)
    fun register() {
        RegisterActivity_.intent(this).start()
        finish()
    }

    @Click(R.id.btn_login)
    fun login() {
        LoginActivity_.intent(this).start()
        finish()
    }

    private fun addBottomDots(currentPage: Int) {
        dots = Array(3, { TextView(this) })
        dotsLayout.removeAllViews()
        for (i in 0 until dots.size) {
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].setTextSize(35F)
            dots[i].setTextColor(resources.getColor(R.color.white))
            dotsLayout.addView(dots[i])
        }

        if (dots.size > 0)
            dots[currentPage].setTextColor(resources.getColor(R.color.colorPrimary))
    }

    inner class IntroViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

}
