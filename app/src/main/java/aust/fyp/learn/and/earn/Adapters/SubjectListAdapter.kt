package aust.fyp.learn.and.earn.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Activities.SubjectList
import aust.fyp.learn.and.earn.Activities.TeacherList
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R

class SubjectListAdapter(var list: ArrayList<SubjectModel>) :
    RecyclerView.Adapter<SubjectListAdapter.myViewsHolder>() {

    lateinit var context: Context

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subject_name: TextView = view.findViewById(R.id.subject_name)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectListAdapter.myViewsHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.single_view_siubject, null)

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

    override fun onBindViewHolder(holder: SubjectListAdapter.myViewsHolder, position: Int) {
        holder.subject_name.setText(list.get(position).subject_name)

        holder.itemView.setOnClickListener {
            var intent = Intent(context, TeacherList::class.java)
            intent.putExtra("category", list.get(position).category.trim().toLowerCase())
            intent.putExtra("subject", list.get(position).subject_name.trim().toLowerCase())
            context.startActivity(intent)
        }


    }
}