package aust.fyp.learn.and.earn.Fragments.Teacher


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
import aust.fyp.learn.and.earn.Activities.AddNewSubject
import aust.fyp.learn.and.earn.Adapters.HomeFragmentStudentAdapter
import aust.fyp.learn.and.earn.Adapters.HomeFragmentTeacherAdopter
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_home_teacher.*
import org.json.JSONArray
import org.json.JSONObject

class HomeFragmentTeacher : Fragment() {

    lateinit var recView: RecyclerView
    lateinit var realm: Realm
    lateinit var realmList: RealmResults<SubjectModel>
    lateinit var list: ArrayList<SubjectModel>
    lateinit var adapter: HomeFragmentTeacherAdopter
    var TAG = "TeacherSubject"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
        list = ArrayList<SubjectModel>()
        adapter = HomeFragmentTeacherAdopter(list)

        realmList = realm.where(SubjectModel::class.java)
            .equalTo("userID", PreferenceManager.getInstance(context!!)!!.getUserId())
            .findAll()
        processList(realmList)

        realmList.addChangeListener { realmList ->
            processList(realmList)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home_teacher, container, false)

        view.findViewById<Button>(R.id.addSubject).setOnClickListener {
            startActivity(Intent(activity, AddNewSubject::class.java))
        }

        recView = view.findViewById<RecyclerView>(R.id.recView)
        recView.layoutManager = LinearLayoutManager(context)


        recView.adapter = adapter

        fetchRecordsFromServer()
        return view
    }

    fun processList(realmResults: RealmResults<SubjectModel>) {
        list.clear()
        adapter.notifyDataSetChanged()
        if (realmResults != null) {
            realmResults.forEach { model ->
                list.add(model)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun addNewRecord(view: View) {
        startActivity(Intent(context, AddNewSubject::class.java))
    }

    fun fetchRecordsFromServer() {

        var request = object : StringRequest(
            Request.Method.POST, URLs.FETCH_SUBJECT_TEACHER,
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
                            realm.delete(SubjectModel::class.java)

                            realm.createAllFromJson(SubjectModel::class.java, jsonArray)

                        }

                    }
                } catch (e: Exception) {
                    Log.i(TAG, "exception : " + e);
                    Dialogs.showMessage(activity!!, Constants.error_message_exception)
                }
            },
            Response.ErrorListener { error ->
                Log.i(TAG, "error : " + error);
                Dialogs.showMessage(activity!!, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["userID"] =
                    PreferenceManager.getInstance(context!!)!!.getUserId().toString()
                return map
            }
        }

        RequestHandler.getInstance(context!!)!!.addToRequestQueue(request)
    }


}
