package com.devgram.pokewiki.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.devgram.pokewiki.R
import com.devgram.pokewiki.fragments.SignInFragment
import com.devgram.pokewiki.fragments.SignUpFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * The number of pages (wizard steps) to show in this demo.
 */
private const val NUM_PAGES = 2

class InitialActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout : TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        tabLayout = findViewById(R.id.tabLayout)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position){
                0 -> tab.text = getString(R.string.SignIn)
                else -> tab.text = getString(R.string.SignUp)
            }
        }.attach()

    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }



    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment{
            return when (position){
                0 -> SignInFragment()
                else -> SignUpFragment()
            }
        }
    }

}
