package aust.fyp.learn.and.earn.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import aust.fyp.learn.and.earn.Activities.LoginActivity

import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.Settings.ChangeProfilePicture
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    lateinit var layoutView: View
    lateinit var user_name_textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutView = inflater.inflate(R.layout.fragment_settings, container, false)
        user_name_textView = layoutView.findViewById(R.id.user_name)
        return layoutView
    }

    override fun onResume() {
        super.onResume()

        user_name.text = PreferenceManager.getInstance(context!!)!!.getUserName()
        layoutView.findViewById<LinearLayout>(R.id.change_profile_picture).setOnClickListener {
            startActivity(Intent(context, ChangeProfilePicture::class.java))
        }

    }


}
