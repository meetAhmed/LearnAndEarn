package aust.fyp.learn.and.earn.Fragments.Student


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import aust.fyp.learn.and.earn.Adapters.InboxStudentAdopter
import aust.fyp.learn.and.earn.Adapters.Subject_teachersListAdapter
import aust.fyp.learn.and.earn.Models.ChatHeadModel
import aust.fyp.learn.and.earn.Models.MessageModel
import aust.fyp.learn.and.earn.Models.SubjectModel

import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_chat.*
import org.json.JSONObject

class InboxFragmentStudent : Fragment() {

    lateinit var layoutView: View
    lateinit var recView: RecyclerView
    lateinit var realm: Realm
    lateinit var realmList: RealmResults<ChatHeadModel>
    lateinit var list: ArrayList<ChatHeadModel>
    lateinit var adapter: InboxStudentAdopter
    lateinit var reload: SwipeRefreshLayout

    var TAG = "InboxFragmentS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()

        list = ArrayList<ChatHeadModel>()
        adapter = InboxStudentAdopter(list,PreferenceManager.getInstance(context!!)!!.getUserId())

        realmList = realm.where(ChatHeadModel::class.java).sort("ID", Sort.DESCENDING).findAll()
        processModel(realmList)

        realmList.addChangeListener { realmList ->
            processModel(realmList)
        }


        fetchRecordsFromServer()
    }

    private fun processModel(realmList: RealmResults<ChatHeadModel>?) {
        list.clear()
        adapter.notifyDataSetChanged()

        if (realmList != null) {

            realmList.forEach { model ->
                list.add(model)
                adapter.notifyDataSetChanged()
            }

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutView = inflater.inflate(R.layout.fragment_inbox_fragment_student, container, false)

        recView = layoutView.findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(context)
        recView.adapter = adapter

        reload = layoutView.findViewById(R.id.reload)
        reload.setOnRefreshListener {
            reload.isRefreshing = false
            fetchRecordsFromServer()
        }

        return layoutView

    }

    fun fetchRecordsFromServer() {

        var request = object : StringRequest(
            Request.Method.POST, URLs.HEADS,
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
                            realm.delete(ChatHeadModel::class.java)
                            realm.createAllFromJson(ChatHeadModel::class.java, jsonArray)
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
