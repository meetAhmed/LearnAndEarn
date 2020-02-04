package aust.fyp.learn.and.earn.Settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.R

class EducationHistory : AppCompatActivity() {

    lateinit var recView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_history)

        supportActionBar!!.hide()

        recView = findViewById(R.id.recView)
    }

    fun addNewRecord(view: View) {
        startActivity(Intent(this, AddEducationHistory::class.java))
    }

}
