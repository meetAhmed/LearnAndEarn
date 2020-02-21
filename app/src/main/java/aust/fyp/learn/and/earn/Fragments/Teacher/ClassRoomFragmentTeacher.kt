package aust.fyp.learn.and.earn.Fragments.Student


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Activities.VideoCall
import aust.fyp.learn.and.earn.Adapters.StudentRegisteredCourseAdapter
import aust.fyp.learn.and.earn.Adapters.TeacherClassAdapter
import aust.fyp.learn.and.earn.Models.StudentRegisteredCourses
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.Models.TeacherClassStudentModel

import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import io.realm.RealmResults
import org.json.JSONObject

class ClassRoomFragmentTeacher : Fragment() {

    var TAG = "ClassRoomFragmentTeacher"
    lateinit var realm: Realm
    lateinit var recView: RecyclerView
    lateinit var realmResults: RealmResults<TeacherClassStudentModel>
    lateinit var list: ArrayList<TeacherClassStudentModel>
    lateinit var adapter: TeacherClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
        list = ArrayList()
        adapter = TeacherClassAdapter(list)
        realmResults = realm.where(TeacherClassStudentModel::class.java).findAll()
        processModels(realmResults)
        realmResults.addChangeListener { realmResults ->
            processModels(realmResults)
        }

        fetchRecordsFromServer()
    }

    private fun processModels(realmResults: RealmResults<TeacherClassStudentModel>?) {
        list.clear()
        adapter.notifyDataSetChanged()
        if (realmResults != null) {
            realmResults.forEach { model ->
                list.add(model)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_class_room_fragment_student, container, false)

        recView = view.findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(context)
        recView.adapter = adapter

        return view
    }

    fun fetchRecordsFromServer() {

        var request = object : StringRequest(
            Request.Method.POST, URLs.TEACHER_CLASS_STUDENTS,
            Response.Listener { response ->
                try {
                    Log.i(TAG, "response : $response")
                    var mainOb = JSONObject(response)
                    val error = mainOb.getBoolean("error")

                    if (error) {
                        // error
                        Toast.makeText(
                            context,
                            mainOb.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        var jsonArray = mainOb.getJSONArray("data")

                        realm.executeTransaction { realm ->
                            realm.delete(TeacherClassStudentModel::class.java)
                            realm.createAllFromJson(TeacherClassStudentModel::class.java, jsonArray)
                        }

                    }
                } catch (e: Exception) {
                    Log.i(TAG, "exception : $e")
                    Dialogs.showMessage(activity!!, Constants.error_message_exception)
                }
            },
            Response.ErrorListener { error ->
                Log.i(TAG, "error : $error")
                Dialogs.showMessage(activity!!, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["teacher_id"] =
                    PreferenceManager.getInstance(context!!)!!.getUserId().toString()
                return map
            }
        }

        RequestHandler.getInstance(context!!)!!.addToRequestQueue(request)
    }


}
