package aust.fyp.learn.and.earn.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Activities.TeacherDetailsActivity
import aust.fyp.learn.and.earn.Activities.Teacher_Call_activity
import aust.fyp.learn.and.earn.Activities.VideoCall
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.Models.StudentRegisteredCourses
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.Models.TeacherClassStudentModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.squareup.picasso.Picasso

class TeacherClassAdapter(var list: ArrayList<TeacherClassStudentModel>) :
    RecyclerView.Adapter<TeacherClassAdapter.myViewsHolder>() {

    lateinit var context: Context

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subject_name: TextView = view.findViewById(R.id.subject_name)
        var student_name: TextView = view.findViewById(R.id.student_name)
        var video_call: ImageView = view.findViewById(R.id.video_call)
        var class_time: TextView = view.findViewById(R.id.class_time)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherClassAdapter.myViewsHolder {
        var view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.single_teacher_registered_course_row, null)

        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        context = view.context

        return myViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(
        holder: TeacherClassAdapter.myViewsHolder,
        position: Int
    ) {
        holder.subject_name.setText(list.get(position).subject_name)
        holder.student_name.setText(list.get(position).student_name)

        holder.itemView.setOnClickListener {
            var intent = Intent(context, Teacher_Call_activity::class.java)
            intent.putExtra("student_id", list.get(position).student_id)
            intent.putExtra("subject_name", list.get(position).subject_name)
            intent.putExtra("student_name", list.get(position).student_name)
            context.startActivity(intent)
        }
    }
}