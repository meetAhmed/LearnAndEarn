package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.Models.AddSubjectModel
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import org.json.JSONObject

class AddNewSubject : AppCompatActivity() {

    lateinit var subject_name : EditText
    lateinit var description : EditText
    lateinit var price_per_month :EditText
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_subject)

        subject_name = findViewById(R.id.subject_name)
        description = findViewById(R.id.description)
        price_per_month = findViewById(R.id.price_per_month)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)



    }

    fun save(view : View){

        progressDialog.setMessage("your subject is adding ")
        progressDialog.show()


        var request = object : StringRequest(
            Request.Method.POST, URLs.SUBJECT,
            Response.Listener { response ->

                try {

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss()
                    }

                    var mainOb = JSONObject(response)
                    var message = mainOb.getString("message")
                    val error = mainOb.getBoolean("error")

                    if (error) {
                        // error
                        Dialogs.showMessage(this, message)
                    } else {

                        var subject_name_str =subject_name.text.toString().trim()
                        var description_str =description.text.toString().trim()
                        var price_per_month_str =price_per_month.text.toString().trim()



                        Realm.getDefaultInstance().executeTransaction { realm ->

                            var model: AddSubjectModel =
                                realm.createObject(
                                    AddSubjectModel::class.java,
                                    mainOb.getInt("insertId")
                                )

                            model.apply {
                                userID =
                                    PreferenceManager.getInstance(applicationContext!!)!!.getUserId()
                                subject_name = subject_name_str
                                description = description_str
                                price_per_month = price_per_month_str
                            }

                            realm.insert(model)

                            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                    }
                } catch (e: Exception) {
                    Log.i(LoginActivity.TAG, "exception : " + e);
                    Dialogs.showMessage(this, Constants.error_message_exception)
                }
            },
            Response.ErrorListener { error ->
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss()
                }
                Log.i(LoginActivity.TAG, "error : " + error);
                Dialogs.showMessage(this, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["description"] = description.text.toString().trim()
                map["userId"] =
                    PreferenceManager.getInstance(applicationContext)!!.getUserId().toString()
                map["subject_name"] = subject_name.text.toString().trim()
                map["price_per_month"] = price_per_month.text.toString().trim()

                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)

    }
    }

