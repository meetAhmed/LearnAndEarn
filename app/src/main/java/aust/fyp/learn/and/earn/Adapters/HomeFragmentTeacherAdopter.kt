package aust.fyp.learn.and.earn.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R


class HomeFragmentTeacherAdopter(var list: ArrayList<SubjectModel>) :


        RecyclerView.Adapter<aust.fyp.learn.and.earn.Adapters.HomeFragmentTeacherAdopter.myViewsHolder>() {

        class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {
            var category : TextView = view.findViewById(R.id.category)
            var subject_name: TextView = view.findViewById(R.id.subject_name)
            var description: TextView = view.findViewById(R.id.description)
            var price_per_month: TextView = view.findViewById(R.id.price_per_month)

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): aust.fyp.learn.and.earn.Adapters.HomeFragmentTeacherAdopter.myViewsHolder {
            var view: View =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.single_view_teacher_own_subject, null)

            view.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            return myViewsHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }



    override fun onBindViewHolder(holder: myViewsHolder, position: Int) {
        holder.category.setText("category : " + list.get(position).category)
        holder.description.setText("Description : " + list.get(position).description)
        holder.subject_name.setText("subject_name : " + list.get(position).subject_name)
        holder.price_per_month.setText("subject_name : " + list.get(position).price_per_month)


    }
}
