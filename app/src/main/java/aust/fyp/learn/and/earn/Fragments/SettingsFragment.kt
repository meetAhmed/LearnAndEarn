package aust.fyp.learn.and.earn.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import aust.fyp.learn.and.earn.Activities.EditProfile
import aust.fyp.learn.and.earn.Activities.LoginActivity

import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager

class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_settings, container, false)



        fun edit_profile(view: View){
            val intent = Intent(context,EditProfile::class.java)
            startActivity(intent)
        }
        return view
    }


}
