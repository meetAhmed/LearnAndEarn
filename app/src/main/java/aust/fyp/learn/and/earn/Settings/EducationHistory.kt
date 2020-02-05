package aust.fyp.learn.and.earn.Settings

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Activities.LoginActivity
import aust.fyp.learn.and.earn.Activities.VerificationActivity
import aust.fyp.learn.and.earn.Adapters.EducationListAdapter
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults
import org.json.JSONObject

class EducationHistory : AppCompatActivity() {

    lateinit var recView: RecyclerView
    lateinit var realm: Realm
    lateinit var realmList: RealmResults<EducationHistoryModel>
    lateinit var list: ArrayList<EducationHistoryModel>
    lateinit var adapter: EducationListAdapter
    var TAG = "EducationHistory"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_history)

        supportActionBar!!.hide()

        recView = findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(this)

        realm = Realm.getDefaultInstance()
        list = ArrayList<EducationHistoryModel>()
        adapter = EducationListAdapter(list)

        realmList = realm.where(EducationHistoryModel::class.java)
            .equalTo("userID", PreferenceManager.getInstance(applicationContext!!)!!.getUserId())
            .findAll()
        processList(realmList)

        realmList.addChangeListener { realmList ->
            processList(realmList)
        }

        recView.adapter = adapter

        fetchRecordsFromServer()
    }

    fun processList(realmResults: RealmResults<EducationHistoryModel>) {
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
        startActivity(Intent(this, AddEducationHistory::class.java))
    }

    fun fetchRecordsFromServer() {

        var request = object : StringRequest(
            Request.Method.POST, URLs.FETCH_RECORDS,
            Response.Listener { response ->
                try {
                    Log.i(TAG, "response : $response")
                    var mainOb = JSONObject(response)
                    val error = mainOb.getBoolean("error")

                    if (error) {
                        // error
                        Toast.makeText(
                            applicationContext,
                            mainOb.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        var jsonArray = mainOb.getJSONArray("data")

                        realm.executeTransaction { realm ->
                            realm.delete(EducationHistoryModel::class.java)

                            for (i in 0..(jsonArray.length() - 1)) {
                                var ob = jsonArray.getJSONObject(i)

                                var model: EducationHistoryModel =
                                    realm.createObject(
                                        EducationHistoryModel::class.java,
                                        ob.getInt("ID")
                                    )
                                model.degree_title = ob.getString("type")
                                model.userID = ob.getInt("userID")
                                model.description = ob.getString("text")

                                realm.insert(model)
                            }

                        }

                    }
                } catch (e: Exception) {
                    Log.i(TAG, "exception : " + e);
                    Dialogs.showMessage(this, Constants.error_message_exception)
                }
            },
            Response.ErrorListener { error ->
                Log.i(TAG, "error : " + error);
                Dialogs.showMessage(this, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["userID"] =
                    PreferenceManager.getInstance(applicationContext!!)!!.getUserId().toString()
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)
    }

}
