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
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.squareup.picasso.Picasso

class Subject_teachersListAdapter(var list: ArrayList<SubjectModel>) :
    RecyclerView.Adapter<Subject_teachersListAdapter.myViewsHolder>() {

    lateinit var context: Context

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var teacher_name: TextView = view.findViewById(R.id.teacher_name)
        var price_per_month: TextView = view.findViewById(R.id.price_per_month)
        var description: TextView = view.findViewById(R.id.description)
        var profile_image: ImageView = view.findViewById(R.id.profile_image)
        var subject_name: TextView = view.findViewById(R.id.subject_name)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Subject_teachersListAdapter.myViewsHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.single_view_siubject_teacher, null)

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
        holder: Subject_teachersListAdapter.myViewsHolder,
        position: Int
    ) {
        holder.teacher_name.setText(list.get(position).teacher_name)
        holder.subject_name.setText(list.get(position).subject_name)
        holder.description.setText(list.get(position).description)
        holder.price_per_month.setText("$ " + list.get(position).price_per_month)

        Picasso.get()
            .load(URLs.getImageUrl(list.get(position).profile_addresss))
            .into(holder.profile_image)


        holder.itemView.setOnClickListener {

            var intent = Intent(context, TeacherDetailsActivity::class.java)
            intent.putExtra("ID", list.get(position).ID)
            intent.putExtra("subject_name", list.get(position).subject_name)
            intent.putExtra("description", list.get(position).description)
            intent.putExtra("price_per_month", list.get(position).price_per_month)
            intent.putExtra("userID", list.get(position).userID)
            intent.putExtra("category", list.get(position).category)
            intent.putExtra("teacher_name", list.get(position).teacher_name)
            intent.putExtra("profile_address", list.get(position).profile_addresss)
            context.startActivity(intent)

        }

    }
}