package aust.fyp.learn.and.earn.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager

class MainActivityTeacher : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_teacher)

    }

    fun log_out(view: View) {

        PreferenceManager.getInstance(applicationContext)!!.removeActiveUser()
        var intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

}
