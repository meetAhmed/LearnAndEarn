package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.Models.EducationHistoryModel
import aust.fyp.learn.and.earn.Models.SubjectModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.realm.Realm
import org.json.JSONObject

class UpdateTeacherSubject : AppCompatActivity() {

    lateinit var category:Spinner
    lateinit var subject_name:EditText
    lateinit var description:EditText
    lateinit var price_per_month:EditText
    lateinit var progressDialog:ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_teacher_subject)

        supportActionBar!!.hide()
        category = findViewById(R.id.category)
        subject_name = findViewById(R.id.subject_name)
        description = findViewById(R.id.description)
        price_per_month = findViewById(R.id.price_per_month)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)


        subject_name.setText(intent.getStringExtra("subject_name"))
        description.setText(intent.getStringExtra("description"))
        price_per_month.setText(intent.getDoubleExtra("price_per_month",0.0).toString())

    var category_array = resources.getStringArray(R.array.categories)
        for(i in 0..(category_array.size-1)){
            if(intent.getStringExtra("category").trim().equals(category_array.get(i))){
                category.setSelection(i)
                break
            }
        }


    }
    fun update_teacher_subject(view : View){
        progressDialog.setMessage("updating subject detail")
        progressDialog.show()

        var request = object : StringRequest(
            Request.Method.POST, URLs.UPDATE_TEACHER_SUBJECT,
            Response.Listener { response ->

                try {

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss()
                    }

                    var mainOb = JSONObject(response)
                    val error = mainOb.getBoolean("error")

                    if (error) {
                        // error
                        Dialogs.showMessage(this, mainOb.getString("message"), "OK", object : AlertDialogInterface {
                            override fun positiveButtonClick(dialogInterface: DialogInterface) {
                                dialogInterface.dismiss()
                            }

                            override fun negativeButtonClick(dialogInterface: DialogInterface) {
                            }
                        })
                    } else {

                        Realm.getDefaultInstance().executeTransaction { realm ->

                            var model = realm.where(SubjectModel::class.java).equalTo("ID",intent.getIntExtra("ID",0)).findFirst();
                            if(model != null){

                                model.category = category.selectedItem.toString().toLowerCase().trim()
                                model.subject_name = subject_name.text.toString()
                                model.description = description.text.toString()
                                model.price_per_month  = (price_per_month.text.toString().trim()).toDouble()

                                Toast.makeText(applicationContext, "updated successfully", Toast.LENGTH_SHORT).show()
                                finish()

                            }

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
                map["userid"] = PreferenceManager.getInstance(applicationContext)!!.getUserId().toString()
                map["category"] = category.selectedItem.toString().toLowerCase().trim()
                map["subject_name"] = subject_name.text.toString().trim()
                map["description"] = description.text.toString().trim()
                map["price_per_month"] = price_per_month.text.toString().trim()
                map["subject_id"] = intent.getIntExtra("ID",0).toString()
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)

    }
}
