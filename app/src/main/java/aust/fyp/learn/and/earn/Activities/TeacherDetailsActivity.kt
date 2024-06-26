package aust.fyp.learn.and.earn.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Adapters.EducationListAdapter
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmResults
import org.json.JSONObject

class TeacherDetailsActivity : AppCompatActivity() {

    lateinit var profile_image: ImageView
    lateinit var teacher_name: TextView
    lateinit var price_per_month: TextView
    lateinit var subject_name: TextView
    lateinit var description: TextView
    lateinit var employmenthistory: TextView
    lateinit var educationhistory: TextView
    lateinit var realm: Realm
    lateinit var realmList: RealmResults<EducationHistoryModel>
    lateinit var list: ArrayList<EducationHistoryModel>
    lateinit var adapter: EducationListAdapter

    lateinit var employment_history: RecyclerView
    lateinit var education_history: RecyclerView
    var TAG = "TeacherDetailAcitvity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_details)

        supportActionBar!!.hide()

        profile_image = findViewById(R.id.profile_image)

        teacher_name = findViewById(R.id.teacher_name)
        price_per_month = findViewById(R.id.price_per_month)
        description = findViewById(R.id.description)
        profile_image = findViewById(R.id.profile_image)

        teacher_name.setText(intent.getStringExtra("teacher_name"))
        price_per_month.setText("$ " + intent.getDoubleExtra("price_per_month", 0.0).toString())
        description.setText(intent.getStringExtra("description"))

        employmenthistory = findViewById(R.id.employmenthistory)
        educationhistory = findViewById(R.id.educationhistory)
        employment_history = findViewById(R.id.employment_history)
        education_history = findViewById(R.id.education_history)

        employment_history.layoutManager = LinearLayoutManager(this)
        education_history.layoutManager = LinearLayoutManager(this)

        realm = Realm.getDefaultInstance()
        list = ArrayList<EducationHistoryModel>()
        adapter = EducationListAdapter(list)

        Picasso.get()
            .load(URLs.getImageUrl(intent.getStringExtra("profile_address")))
            .into(profile_image)

        realmList = realm.where(EducationHistoryModel::class.java)
            .equalTo("userID", intent.getIntExtra("userID", 0))
            .findAll()
        processList(realmList)

        realmList.addChangeListener { realmList ->
            processList(realmList)
        }

        education_history.adapter = adapter

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
                map["userID"] = intent.getIntExtra("userID", 0).toString()
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)
    }

    fun open_checkout(view: View) {
        var intent_to = Intent(this, CheckoutActivity::class.java)
        intent_to.putExtra("ID", intent.getIntExtra("ID", 0))
        intent_to.putExtra("subject_name", intent.getStringExtra("subject_name"))
        intent_to.putExtra("price_per_month", intent.getDoubleExtra("price_per_month", 0.0))
        intent_to.putExtra("teacher_name", intent.getStringExtra("teacher_name"))
        intent_to.putExtra("teacher_id", intent.getIntExtra("userID", 0))
        startActivity(intent_to)
    }

    fun open_chat(view: View) {
        var intent_to = Intent(this, ChatActivity::class.java)
        intent_to.putExtra("ID", intent.getIntExtra("userID", 0))
        intent_to.putExtra("receiver_name", intent.getStringExtra("teacher_name"))
        startActivity(intent_to)
    }
}

