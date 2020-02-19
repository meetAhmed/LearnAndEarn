package aust.fyp.learn.and.earn.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import aust.fyp.learn.and.earn.Adapters.ViewPagerAdapter
import aust.fyp.learn.and.earn.Fragments.Teacher.HomeFragmentTeacher
import aust.fyp.learn.and.earn.Fragments.Teacher.InboxFragmentTeacher
import aust.fyp.learn.and.earn.Fragments.SettingsFragment
import aust.fyp.learn.and.earn.Fragments.Student.ClassRoomFragmentTeacher
import aust.fyp.learn.and.earn.Fragments.Student.InboxFragmentStudent
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityTeacher : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var bottomNav: BottomNavigationView
    lateinit var viewPagerAdapter: ViewPagerAdapter
    var currentFragment = "home"
    var previousItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_teacher)

        viewPager = findViewById(R.id.viewPager)
        bottomNav = findViewById(R.id.bottomNav)

        supportActionBar!!.hide()

        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(HomeFragmentTeacher(), "Home")
        viewPagerAdapter.addFragment(InboxFragmentStudent(), "Inbox")
        viewPagerAdapter.addFragment(ClassRoomFragmentTeacher(), "Class Room")
        viewPagerAdapter.addFragment(SettingsFragment(), "Settings")
        viewPager.adapter = viewPagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

                if (previousItem == null) {
                    bottomNav.menu.getItem(0).setChecked(false)
                } else {
                    previousItem?.setChecked(false)
                }
                bottomNav.menu.getItem(position).setChecked(true)
                previousItem = bottomNav.menu.getItem(position)

                currentFragment = when (position) {
                    0 -> "home"
                    1 -> "inbox"
                    2 -> "class_room"
                    3 -> "settings"
                    else -> "some_other_frag"
                }

            }

        })

        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                R.id.home -> {
                    viewPager.setCurrentItem(0)
                    currentFragment = "home"
                }
                R.id.inbox -> {
                    Log.i("tester123", "inbox : clicked " + viewPagerAdapter.fragmentList.size)
                    viewPager.currentItem = 1
                    currentFragment = "inbox"
                }
                R.id.class_room -> {
                    viewPager.setCurrentItem(2)
                    currentFragment = "class_room"
                }
                R.id.settings -> {
                    viewPager.setCurrentItem(3)
                    currentFragment = "settings"
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (currentFragment.equals("home")) {
            super.onBackPressed()
        } else {
            viewPager.setCurrentItem(0)
            bottomNav.selectedItemId = R.id.home
            currentFragment = "home"
        }
    }


    fun log_out(view: View) {
        PreferenceManager.getInstance(applicationContext)!!.removeActiveUser()
        var intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

}
