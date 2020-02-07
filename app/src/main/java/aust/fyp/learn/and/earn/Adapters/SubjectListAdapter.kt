package aust.fyp.learn.and.earn.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R

class SubjectListAdapter(var list: ArrayList<SubjectModel>) :
    RecyclerView.Adapter<SubjectListAdapter.myViewsHolder>() {

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subject_name: TextView = view.findViewById(R.id.subject_name)
        var teacher_name: TextView = view.findViewById(R.id.teacher_name)
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
        return myViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: SubjectListAdapter.myViewsHolder, position: Int) {
        holder.subject_name.setText("Title : " + list.get(position).subject_name)
        holder.teacher_name.setText("Teacher : " + list.get(position).teacher_name)
    }
}