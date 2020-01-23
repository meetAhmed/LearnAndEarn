package aust.fyp.learn.and.earn.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Adapters.Adapter
import aust.fyp.learn.and.earn.Models.Person
import aust.fyp.learn.and.earn.R

class MainActivity : AppCompatActivity() {

    var recView = findViewById<RecyclerView>(R.id.recView)
    var adapter_: Adapter? = null // object
    var listOfPersons: ArrayList<Person> = ArrayList<Person>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recView.layoutManager = LinearLayoutManager(this)
        adapter_ = Adapter(listOfPersons)
        recView.adapter = adapter_

        listOfPersons.add(
            Person(
                name = "Ahmed",
                age = 12,
                country = "Pakistan"
            )
        )
        listOfPersons.add(
            Person(
                name = "Ahmed",
                country = "Pakistan",
                age = 12
            )
        )
        listOfPersons.add(
            Person(
                name = "Ahmed",
                age = 12,
                country = "Pakistan"
            )
        )
        listOfPersons.add(
            Person(
                "Ahmed",
                12,
                "Pakistan"
            )
        )

    }

}
