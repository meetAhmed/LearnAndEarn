package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlinx.android.synthetic.main.single_view.*
import org.json.JSONObject

class EditProfile : AppCompatActivity() {


    lateinit var name_edit: EditText
    lateinit var address_edit: EditText
    lateinit var phone_number_edit: EditText
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        name_edit = findViewById(R.id.name_edit)
        address_edit = findViewById(R.id.address_edit)
        phone_number_edit = findViewById(R.id.phone_number_edit)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)

        name_edit.setText(PreferenceManager.getInstance(this)!!.getUserName())
        name_edit.setSelection(name_edit.text.toString().trim().length)

        address_edit.setText(PreferenceManager.getInstance(this)!!.getUserAddress())
        phone_number_edit.setText(PreferenceManager.getInstance(this)!!.getUserPhone())


    }

    fun update_edit_profile(view: View) {
        progressDialog.setMessage("update profiling")
        progressDialog.show()

        var request = object : StringRequest(
            Request.Method.POST, URLs.EDIT_PROFILE,
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
                        Dialogs.showMessage(this, message, "OK", object : AlertDialogInterface {
                            override fun positiveButtonClick(dialogInterface: DialogInterface) {
                                dialogInterface.dismiss()
                            }

                            override fun negativeButtonClick(dialogInterface: DialogInterface) {
                            }
                        })
                    } else {

                        PreferenceManager.getInstance(applicationContext)!!.setUserName(name_edit.text.toString().trim())
                        PreferenceManager.getInstance(applicationContext)!!.setUserAddress(
                            address_edit.text.toString().trim()
                        )
                        PreferenceManager.getInstance(applicationContext)!!.setUserPhone(
                            phone_number_edit.text.toString().trim()
                        )

                        Dialogs.showMessage(this, message, "OK", object : AlertDialogInterface {
                            override fun positiveButtonClick(dialogInterface: DialogInterface) {
                                dialogInterface.dismiss()
                                finish()
                            }

                            override fun negativeButtonClick(dialogInterface: DialogInterface) {
                            }
                        })
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
                map["userid"] =
                    PreferenceManager.getInstance(applicationContext)!!.getUserId().toString()
                map["name"] = name_edit.text.toString().trim()
                map["address"] = address_edit.text.toString().trim()
                map["phone_number"] = phone_number_edit.text.toString().trim()

                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)
    }


}

