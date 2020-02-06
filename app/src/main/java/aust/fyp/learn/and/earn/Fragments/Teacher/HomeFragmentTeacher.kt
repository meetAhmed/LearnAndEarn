package aust.fyp.learn.and.earn.Fragments.Teacher


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import aust.fyp.learn.and.earn.Activities.AddNewSubject

import aust.fyp.learn.and.earn.R

class HomeFragmentTeacher : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home_teacher, container, false)

        view.findViewById<Button>(R.id.addSubject).setOnClickListener {
            startActivity(Intent(activity, AddNewSubject::class.java))
        }

        return view;
    }


}
