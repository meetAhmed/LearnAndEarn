package aust.fyp.learn.and.earn.Settings

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import aust.fyp.learn.and.earn.Activities.LoginActivity
import aust.fyp.learn.and.earn.Activities.VerificationActivity
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import io.realm.annotations.RealmClass
import org.json.JSONObject

class AddEducationHistory : AppCompatActivity() {

    lateinit var degree_title: Spinner
    lateinit var description: EditText
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_education_history)

        supportActionBar!!.hide()

        description = findViewById(R.id.description)
        degree_title = findViewById(R.id.degree_title)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
    }

    fun save(view: View) {

        if (description.text.toString().trim().isEmpty()) {
            description.setError("Please enter description")
            description.requestFocus()
        } else {
            progressDialog.setMessage("Saving record")
            progressDialog.show()

            var request = object : StringRequest(
                Request.Method.POST, URLs.ADD_EDUCATION_HISTORY,
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

                            var degree_title_str = degree_title.selectedItem.toString()
                            var desc_str = description.text.toString().trim()

                            Realm.getDefaultInstance().executeTransaction { realm ->

                                var model: EducationHistoryModel =
                                    realm.createObject(
                                        EducationHistoryModel::class.java,
                                        mainOb.getInt("insertId")
                                    )

                                model.apply {
                                    userID =
                                        PreferenceManager.getInstance(applicationContext!!)!!.getUserId()
                                    degree_title = degree_title_str
                                    description = desc_str
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
                    map["degree_title"] = degree_title.selectedItem.toString()
                    return map
                }
            }

            RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)

        }

    }


}
