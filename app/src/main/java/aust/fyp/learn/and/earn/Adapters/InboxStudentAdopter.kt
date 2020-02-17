package aust.fyp.learn.and.earn.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Activities.ChatActivity
import aust.fyp.learn.and.earn.Models.ChatHeadModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.Store

class InboxStudentAdopter(var list: ArrayList<ChatHeadModel>, var currentUser: Int) :
    RecyclerView.Adapter<InboxStudentAdopter.myViewsHolder>() {

    lateinit var context: Context

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {

        var user_name: TextView = view.findViewById(R.id.receiver_name)
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

        var other_user_id = 0

        if (currentUser == list.get(position).sender_id) {
            holder.user_name.setText(list.get(position).receiver_name)
            other_user_id = list.get(position).receiver_id
        } else {
            holder.user_name.setText(list.get(position).sender_name)
            other_user_id = list.get(position).sender_id
        }

        holder.message.setText(list.get(position).message)
        holder.date.setText(Store.getTime(list.get(position).date))


        holder.itemView.setOnClickListener {
            var intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("receiver_name", holder.user_name.text.toString())
            intent.putExtra("ID",other_user_id)
            context.startActivity(intent)
        }
    }
}


