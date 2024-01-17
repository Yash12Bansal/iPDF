package com.yash.pdfconverter

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(fn:FragmentManager,behavior: Int): FragmentPagerAdapter(fn,behavior) {

    var fragmentAL=ArrayList<Fragment>()
    var titleAL=ArrayList<String>()
    override fun getCount(): Int {

        return fragmentAL.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentAL.get(position)
    }

    fun addFragment(fragment:Fragment,title:String){
        fragmentAL.add(fragment)
        titleAL.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
//        return super.getPageTitle(position)
        return titleAL.get(position)
    }


}