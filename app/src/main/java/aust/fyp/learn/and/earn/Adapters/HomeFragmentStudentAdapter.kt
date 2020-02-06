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
import aust.fyp.learn.and.earn.Activities.SubjectList
import aust.fyp.learn.and.earn.Models.CategoryModel
import aust.fyp.learn.and.earn.R


class HomeFragmentStudentAdapter(var list: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<HomeFragmentStudentAdapter.myViewsHolder>() {

    lateinit var context: Context

    class myViewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title)
        var image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeFragmentStudentAdapter.myViewsHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.single_view_category, null)

        context = view.context

        val scale: Float = context.getResources().getDisplayMetrics().density
        val pixels = (165 * scale + 0.5f).toInt()

        view.layoutParams = LinearLayout.LayoutParams(
            pixels,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return myViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HomeFragmentStudentAdapter.myViewsHolder, position: Int) {
        holder.title.setText(list.get(position).title)
        holder.image.setImageResource(list.get(position).image)

        holder.itemView.setOnClickListener {
            var intent = Intent(context, SubjectList::class.java)
            intent.putExtra("category", list.get(position).title.trim().toLowerCase())
            context.startActivity(intent)
        }

    }
}