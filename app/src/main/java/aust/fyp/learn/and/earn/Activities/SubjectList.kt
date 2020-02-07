package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Adapters.EducationListAdapter
import aust.fyp.learn.and.earn.Adapters.SubjectListAdapter
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import io.realm.RealmResults
import org.json.JSONObject

class SubjectList : AppCompatActivity() {

    lateinit var category: String
    lateinit var realm: Realm
    lateinit var realmList: RealmResults<SubjectModel>
    lateinit var list: ArrayList<SubjectModel>
    lateinit var adapter: SubjectListAdapter
    lateinit var recView: RecyclerView
    var TAG = "SubjectList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_list)

        supportActionBar!!.hide()

        category = intent.getStringExtra("category")
        realm = Realm.getDefaultInstance()

        if (category == null) {
            finish()
        } else {
            fetchDataFromServer()
        }

        list = ArrayList<SubjectModel>()
        adapter = SubjectListAdapter(list)

        realmList = realm.where(SubjectModel::class.java)
            .equalTo("category", category)
            .findAll()
        processList(realmList)

        Log.i(TAG, category)

        realmList.addChangeListener { realmList ->
            processList(realmList)
        }

        recView = findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = adapter
    }

    fun processList(realmResults: RealmResults<SubjectModel>) {
        list.clear()
        adapter.notifyDataSetChanged()
        if (realmResults != null) {
            realmResults.forEach { model ->
                var isAlreadyPresent = false

                Log.i(TAG, model.category + " " + model.subject_name)

                list.forEach { listModel ->
                    if ((listModel.subject_name.toLowerCase().equals(model.subject_name.toLowerCase()))) {
                        isAlreadyPresent = true
                    }
                }

                if (!isAlreadyPresent) {
                    list.add(model)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun fetchDataFromServer() {

        var request = object : StringRequest(
            Request.Method.POST, URLs.FETCH_SUBJECT,
            Response.Listener { response ->

                try {

                    Log.i(TAG, response)

                    var mainOb = JSONObject(response)
                    val error = mainOb.getBoolean("error")

                    if (error) {
                        // error
                        Dialogs.showMessage(this, mainOb.getString("message"))
                    } else {
                        realm.executeTransaction { realm ->
                            realm.delete(SubjectModel::class.java)
                            realm.createAllFromJson(
                                SubjectModel::class.java,
                                mainOb.getJSONArray("data")
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.i(LoginActivity.TAG, "exception : " + e);
                    Dialogs.showMessage(this@SubjectList, Constants.error_message_exception)
                }
            },
            Response.ErrorListener { error ->
                Log.i(LoginActivity.TAG, "error : " + error);
                Dialogs.showMessage(this@SubjectList, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["category"] = category
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)

    }
}
