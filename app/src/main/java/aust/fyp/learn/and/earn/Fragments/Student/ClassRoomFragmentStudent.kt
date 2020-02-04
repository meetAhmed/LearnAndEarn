package aust.fyp.learn.and.earn.Fragments.Student


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import aust.fyp.learn.and.earn.R

/**
 * A simple [Fragment] subclass.
 */
class ClassRoomFragmentStudent : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_room_fragment_student, container, false)
    }


}
