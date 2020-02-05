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
import aust.fyp.learn.and.earn.Activities.EditProfile

import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.Settings.ChangeProfilePicture
import aust.fyp.learn.and.earn.Settings.EducationHistory
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings_teacher.*

class SettingsFragment : Fragment() {

    lateinit var layoutView: View
    lateinit var user_name_textView: TextView
    lateinit var user_dp: ImageView
    lateinit var profile_image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutView = inflater.inflate(R.layout.fragment_settings_teacher, container, false)
        user_name_textView = layoutView.findViewById(R.id.user_name)
        user_dp = layoutView.findViewById(R.id.profile_image)
        profile_image = layoutView.findViewById(R.id.profile_image)

        layoutView.findViewById<LinearLayout>(R.id.education_history_view).setOnClickListener {
            startActivity(Intent(context, EducationHistory::class.java))
        }
        layoutView.findViewById<LinearLayout>(R.id.edit_profile).setOnClickListener {
            startActivity(
                Intent(context, EditProfile::class.java)
            )
        }

        layoutView.findViewById<LinearLayout>(R.id.change_profile_picture).setOnClickListener {
            startActivity(Intent(context, ChangeProfilePicture::class.java))
        }

        return layoutView
    }

    override fun onResume() {
        super.onResume()

        user_name.text = PreferenceManager.getInstance(context!!)!!.getUserName()

        Picasso.get()
            .load(URLs.getImageUrl(PreferenceManager.getInstance(context!!)!!.getUserProfile()))
            .into(profile_image)
        Picasso.get()
            .load(URLs.getImageUrl(PreferenceManager.getInstance(context!!)!!.getUserProfile()))
            .into(user_dp)

    }


}
