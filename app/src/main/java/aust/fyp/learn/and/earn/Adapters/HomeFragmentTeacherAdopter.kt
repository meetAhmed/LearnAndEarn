package aust.fyp.learn.and.earn.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Activities.TeacherDetailsActivity
import aust.fyp.learn.and.earn.Activities.UpdateTeacherSubject
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.RequestHandler
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject


class HomeFragmentTeacherAdopter(var list: ArrayList<SubjectModel>) :


    RecyclerView.Adapter<aust.fyp.learn.and.earn.Adapters.HomeFragmentTeacherAdopter.myViewsHolder>() {
    var TAG = "HomeFragmentTeacherAdopter"
    lateinit var context: Context


    class myViewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        var category: TextView = view.findViewById(R.id.category)
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
        context = view.context
        return myViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: myViewsHolder, position: Int) {
        holder.category.setText("Category : " + list.get(position).category)
        holder.description.setText(list.get(position).description)
        holder.subject_name.setText(list.get(position).subject_name)
        holder.price_per_month.setText("$ " + list.get(position).price_per_month)


        holder.itemView.setOnClickListener {

            var intent = Intent(context, UpdateTeacherSubject::class.java)
            intent.putExtra("ID", list.get(position).ID)
            intent.putExtra("subject_name", list.get(position).subject_name)
            intent.putExtra("description", list.get(position).description)
            intent.putExtra("price_per_month", list.get(position).price_per_month)
            intent.putExtra("userID", list.get(position).userID)
            intent.putExtra("category", list.get(position).category)
            context.startActivity(intent)

        }
    }
    fun delete(){

     /*   var request = object : StringRequest(
            Request.Method.POST, URLs.DELETE_TEACHER_SUBJECT,
            Response.Listener { response ->

                try {

                    var mainOb = JSONObject(response)
                    Log.i(TAG, mainOb.toString())
                } catch (e: Exception) {
                    Log.i(TAG, "exception : " + e);
                }


            },
            Response.ErrorListener { error ->
                Log.i(TAG, "error : " + error);
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["token"] = token
                map["userID"] =
                    PreferenceManager.getInstance(applicationContext!!)!!.getUserId().toString()
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)*/

    }
}
