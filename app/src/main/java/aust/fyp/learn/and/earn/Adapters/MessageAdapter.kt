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
import aust.fyp.learn.and.earn.Models.MessageModel
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.Store

class MessageAdapter(var list: ArrayList<MessageModel>, var current_user_id: Int) :
    RecyclerView.Adapter<MessageAdapter.myViewsHolder>() {

    lateinit var context: Context

    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var messageText: TextView = view.findViewById(R.id.messageText)
        var messageTime: TextView = view.findViewById(R.id.messageTime)
    }

    override fun getItemViewType(position: Int): Int {
        if (list.get(position).receiver_id == current_user_id) {
            return 1
        }
        return 0
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageAdapter.myViewsHolder {

        var view: View = when (viewType) {
            1 -> LayoutInflater.from(parent.context).inflate(
                R.layout.single_view_layout_receiver,
                null
            )
            else -> LayoutInflater.from(parent.context).inflate(
                R.layout.single_view_layout_sender,
                null
            )
        }


        context = view.context
        return myViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageAdapter.myViewsHolder, position: Int) {
        holder.messageText.setText(list.get(position).message)
        holder.messageTime.setText(Store.getTime(list.get(position).date))



    }
}