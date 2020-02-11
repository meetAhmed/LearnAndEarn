package aust.fyp.learn.and.earn.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Models.EmploymentHistroyModel
import aust.fyp.learn.and.earn.R

class EmploymentListAdopter(var list: ArrayList<EmploymentHistroyModel>) : RecyclerView.Adapter<EmploymentListAdopter.myViewsHolder>() {

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {

        var work_as_a: TextView = view.findViewById(R.id.work_as_a)
        var organization_name: TextView = view.findViewById(R.id.organization_name)
        var experience: TextView = view.findViewById(R.id.experience)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmploymentListAdopter.myViewsHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.single_view_employment, null)
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return EmploymentListAdopter.myViewsHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: EmploymentListAdopter.myViewsHolder, position: Int) {
        holder.work_as_a.setText("Title : " + list.get(position).work_as_a)
        holder.organization_name.setText("Title : " + list.get(position).organization_name)
        holder.experience.setText("Title : " + list.get(position).experience)

    }
}