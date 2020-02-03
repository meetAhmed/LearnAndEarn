package aust.fyp.learn.and.earn.Adapters

import android.util.Log
import androidx.appcompat.widget.DialogTitle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

open class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var fragmentList = ArrayList<Fragment>()
    var fragmentTitles = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles.get(position)
    }

    fun addFragment(fragment: Fragment, title: String) {
        Log.i("tester123", "" + fragmentList.size)
        fragmentList.add(fragment)
        fragmentTitles.add(title)
    }

}