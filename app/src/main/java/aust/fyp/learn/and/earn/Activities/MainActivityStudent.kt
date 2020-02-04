package aust.fyp.learn.and.earn.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import aust.fyp.learn.and.earn.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityStudent : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_student)

        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNav)



    }
}
