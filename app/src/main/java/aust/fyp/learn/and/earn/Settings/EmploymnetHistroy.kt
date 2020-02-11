package aust.fyp.learn.and.earn.Settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Adapters.EmploymentListAdopter
import aust.fyp.learn.and.earn.Models.EmploymentHistroyModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import io.realm.RealmResults
import org.json.JSONObject

class EmploymnetHistroy : AppCompatActivity() {

    lateinit var recView: RecyclerView
    lateinit var realm: Realm
    lateinit var realmList: RealmResults<EmploymentHistroyModel>
    lateinit var list: ArrayList<EmploymentHistroyModel>
    lateinit var adapter: EmploymentListAdopter
    var TAG = "EmploymentHistroy"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employmnet_histroy)




            supportActionBar!!.hide()

            recView = findViewById(R.id.recView)
            recView.layoutManager = LinearLayoutManager(this)

            realm = Realm.getDefaultInstance()
            list = ArrayList<EmploymentHistroyModel>()
            adapter = EmploymentListAdopter(list)

            realmList = realm.where(EmploymentHistroyModel::class.java)
                .equalTo("userID", PreferenceManager.getInstance(applicationContext!!)!!.getUserId())
                .findAll()
            processList(realmList)

            realmList.addChangeListener { realmList ->
                processList(realmList)
            }

            recView.adapter = adapter

            fetchRecordsFromServer()
        }

        fun processList(realmResults: RealmResults<EmploymentHistroyModel>) {
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
            startActivity(Intent(this, AddEmploymentHistroy::class.java))
        }

        fun fetchRecordsFromServer() {

            var request = object : StringRequest(
                Request.Method.POST, URLs.FETCH_EMPLOYMNET_RECORDS,
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
                                realm.delete(EmploymentHistroyModel::class.java)

                                for (i in 0..(jsonArray.length() - 1)) {
                                    var ob = jsonArray.getJSONObject(i)

                                    var model: EmploymentHistroyModel =
                                        realm.createObject(
                                            EmploymentHistroyModel::class.java,
                                            ob.getInt("ID")
                                        )
                                    model.work_as_a = ob.getString("work_as_a")
                                    model.userID = ob.getInt("userID")
                                    model.experience = ob.getString("experience")
                                    model.organization_name =ob.getString("organization_name")

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



