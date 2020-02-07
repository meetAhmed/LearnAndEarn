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
import aust.fyp.learn.and.earn.Adapters.Subject_teachersListAdapter
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import org.json.JSONObject

class TeacherList : AppCompatActivity() {

    lateinit var subject: String
    lateinit var category: String
    lateinit var realm: Realm
    lateinit var realmList: RealmResults<SubjectModel>
    lateinit var list: ArrayList<SubjectModel>
    lateinit var adapter: Subject_teachersListAdapter
    lateinit var recView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_list)

        supportActionBar!!.hide()

        subject = intent.getStringExtra("subject")
        category = intent.getStringExtra("category")

        realm = Realm.getDefaultInstance()

        if (subject == null || category == null) {
            finish()
        }

        list = ArrayList<SubjectModel>()
        adapter = Subject_teachersListAdapter(list)

        realmList = realm.where(SubjectModel::class.java)
            .equalTo("subject_name", subject, Case.INSENSITIVE)
            .findAll()
        processList(realmList)

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
                list.add(model)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
