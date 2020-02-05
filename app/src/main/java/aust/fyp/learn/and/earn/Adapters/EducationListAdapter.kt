package aust.fyp.learn.and.earn.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.R

class EducationListAdapter(var list: ArrayList<EducationHistoryModel>) :
    RecyclerView.Adapter<EducationListAdapter.myViewsHolder>() {

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var description: TextView = view.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EducationListAdapter.myViewsHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.single_view_education, null)

        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return myViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EducationListAdapter.myViewsHolder, position: Int) {
        holder.title.setText("Title : " + list.get(position).degree_title)
        holder.description.setText("Description : " + list.get(position).description)
    }
}