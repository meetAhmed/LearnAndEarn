package aust.fyp.learn.and.earn.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Activities.TeacherDetailsActivity
import aust.fyp.learn.and.earn.Fragments.Student.InboxFragmentStudent
import aust.fyp.learn.and.earn.Models.MessageModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.Store

class InboxStudentAdopter (var list: ArrayList<MessageModel>) : RecyclerView.Adapter<InboxStudentAdopter.myViewsHolder>() {

    lateinit var context: Context

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {

        var sender_name: TextView = view.findViewById(R.id.sender_name)
        var message: TextView = view.findViewById(R.id.message)
        var date: TextView = view.findViewById(R.id.date)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InboxStudentAdopter.myViewsHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.single_view_student_inbox, null)
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        context = view.context
        return InboxStudentAdopter.myViewsHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: InboxStudentAdopter.myViewsHolder, position: Int) {
        holder.sender_name.setText( list.get(position).sender_name)
        holder.message.setText( list.get(position).message)
        holder.date.setText(Store.getTime(list.get(position).date))


        holder.itemView.setOnClickListener {

            var intent = Intent(context, InboxFragmentStudent::class.java)
            intent.putExtra("sender_name", list.get(position).sender_name)
            intent.putExtra("message", list.get(position).message)
            intent.putExtra("date", list.get(position).date)
            context.startActivity(intent)
        }
        }
}


