package aust.fyp.learn.and.earn.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Activities.TeacherDetailsActivity
import aust.fyp.learn.and.earn.Activities.UpdateTeacherSubject
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
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
        var delete: Button = view.findViewById(R.id.delete)
        var update: Button = view.findViewById(R.id.update)


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

        holder.update.setOnClickListener {

            var intent = Intent(context, UpdateTeacherSubject::class.java)
            intent.putExtra("ID", list.get(position).ID)
            intent.putExtra("subject_name", list.get(position).subject_name)
            intent.putExtra("description", list.get(position).description)
            intent.putExtra("price_per_month", list.get(position).price_per_month)
            intent.putExtra("userID", list.get(position).userID)
            intent.putExtra("category", list.get(position).category)
            context.startActivity(intent)

        }

        holder.delete.setOnClickListener{
            removeItem(list.get(position).ID,list.get(position).subject_name)
        }
    }

    private fun removeItem(id: Int,subject_name:String) {

        var request = object : StringRequest(
            Request.Method.POST, URLs.DELETE_TEACHER_SUBJECT,
            Response.Listener { response ->
                try {

                    var mainOb = JSONObject(response)
                    Log.i(TAG, mainOb.toString())

                    if(mainOb.getBoolean("error")){
                        Dialogs.showMessage(context,mainOb.getString("message"))
                    }else{
                     Realm.getDefaultInstance().executeTransaction { realm ->
                         var model = realm.where(SubjectModel::class.java).equalTo("ID",id).findFirst();
                         if(model != null){
                             model.deleteFromRealm()
                             Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show()
                         }
                     }
                    }
                } catch (e: Exception) {
                    Dialogs.showMessage(context,Constants.error_message_exception)
                    Log.i(TAG, "exception : " + e);
                }
            },
            Response.ErrorListener { error ->
                Log.i(TAG, "error : " + error)
                Dialogs.showMessage(context,Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["userID"] = PreferenceManager.getInstance(context!!)!!.getUserId().toString()
                map["subject_id"] = id.toString()
                return map
            }
        }

        RequestHandler.getInstance(context)!!.addToRequestQueue(request)
    }


}
