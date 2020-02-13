package aust.fyp.learn.and.earn.Settings

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import aust.fyp.learn.and.earn.Activities.LoginActivity
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.Models.EmploymentHistroyModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import org.json.JSONObject

class AddEmploymentHistroy : AppCompatActivity() {

    lateinit var work_as_a: EditText
    lateinit var organization_name: EditText
    lateinit var experience: EditText
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employment_histroy)

        supportActionBar!!.hide()


        work_as_a = findViewById(R.id.work_as_a)
        organization_name = findViewById(R.id.organization_name)
        experience = findViewById(R.id.experience)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)


    }

    fun save(view: View) {
        if (experience.toString().trim().isEmpty()) {
            experience.setError("please enter the experience in selected organization")
            experience.requestFocus()
        }
        if (organization_name.toString().trim().isEmpty()) {
            organization_name.setError("please enter the organization name ")
            organization_name.requestFocus()
        }

        if (work_as_a.toString().trim().isEmpty()) {
            work_as_a.setError("please enter the Work As")
            work_as_a.requestFocus()
        } else {

            progressDialog.setMessage("saving record")
            progressDialog.show()



            var request = object : StringRequest(
                Request.Method.POST, URLs.ADD_EMPLOYMENT_HISTORY,
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

                            var wrok_str = work_as_a.text.toString().trim()
                            var org_str = organization_name.text.toString().trim()
                            var exp_str = experience.text.toString().trim()


                            Realm.getDefaultInstance().executeTransaction { realm ->

                                var model: EmploymentHistroyModel =
                                    realm.createObject(
                                        EmploymentHistroyModel::class.java,
                                        mainOb.getInt("insertId")
                                    )

                                model.apply {
                                    userID =
                                        PreferenceManager.getInstance(applicationContext!!)!!.getUserId()
                                    work_as_a = wrok_str
                                    organization_name = org_str
                                    experience = exp_str
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
                    map["work_as_a"] = work_as_a.text.toString().trim()
                    map["organization_name"] = organization_name.text.toString().trim()
                    map["experience"] = experience.text.toString().trim()

                    map["userId"] =
                        PreferenceManager.getInstance(applicationContext)!!.getUserId().toString()
                    return map
                }
            }

            RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)

        }



    }
    }

