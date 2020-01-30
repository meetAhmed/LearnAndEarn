package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.Constants
import aust.fyp.learn.and.earn.StoreRoom.Dialogs
import aust.fyp.learn.and.earn.StoreRoom.RequestHandler
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject

class SetNewPassword : AppCompatActivity() {

    lateinit var password: EditText
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_password)

        password = findViewById(R.id.password)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
    }

    fun save_password(view: View) {

        if (password.text.toString().trim().isEmpty()) {
            password.setError("please enter password")
            password.requestFocus()
        } else {


            progressDialog.setMessage("changing password")
            progressDialog.show()

            var request = object : StringRequest(
                Request.Method.POST, URLs.FORGOT_PASSWORD,
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
                    map["email"] = intent.extras!!.getString("email")!!
                    map["password"] = password.text.toString().trim()
                    return map
                }
            }

            RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)
        }

    }

}
