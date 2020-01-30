package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VerificationActivity : AppCompatActivity() {

    lateinit var code: EditText
    lateinit var log_out_view: TextView
    lateinit var email: String
    lateinit var progressDialog: ProgressDialog

    companion object {
        var TAG = "VerificationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)

        code = findViewById(R.id.code)
        log_out_view = findViewById(R.id.log_out)

        if (intent.extras!!.getString("state").equals("loggeg_in")) {
            email = PreferenceManager.getInstance(applicationContext)!!.getUserEmail()!!
        } else {
            email = intent.extras!!.getString("email")!!
            log_out_view.visibility = View.GONE
        }

    }

    fun verify_code(view: View) {


        if (code.text.toString().trim().isEmpty()) {
            code.setError("please provide code")
            code.requestFocus()
        } else {

            progressDialog.setMessage("verifying code")
            progressDialog.show()

            var request = object : StringRequest(
                Request.Method.POST, URLs.VERIFY_CODE,
                Response.Listener { response ->
                    try {


                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss()
                        }

                        var mainOb = JSONObject(response)
                        var message = mainOb.getString("message")
                        if (mainOb.getBoolean("error")) {
                            Dialogs.showMessage(this, message)
                        } else {

                            if (intent.extras!!.getString("state").equals("loggeg_in")) {
                                PreferenceManager.getInstance(applicationContext)!!.setAccountStatus(
                                    "verified"
                                )
                                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                                    .show()
                                if (PreferenceManager.getInstance(applicationContext)!!.getUserAccountType()!!.trim().equals(
                                        Constants.STUDENT
                                    )
                                ) {
                                    startActivity(Intent(this, MainActivityStudent::class.java))
                                } else {
                                    startActivity(Intent(this, MainActivityTeacher::class.java))
                                }

                                finish()
                            } else {

                                var intent = Intent(this, SetNewPassword::class.java)
                                var pack = Bundle()
                                pack.putString("email", email)
                                intent.putExtras(pack)
                                startActivity(intent)
                                finish()
                            }

                        }
                    } catch (e: Exception) {
                        Log.i(TAG, "exception : " + e);
                        Dialogs.showMessage(this, Constants.error_message_exception)
                    }
                },
                Response.ErrorListener { error ->

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss()
                    }
                    Log.i(TAG, "error : " + error);
                    Dialogs.showMessage(this, Constants.error_message_volley)
                }) {
                override fun getParams(): MutableMap<String, String> {
                    var map = HashMap<String, String>()
                    map["code"] = code.text.toString().trim()
                    map["email"] = email
                    return map
                }
            }

            RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)

        }

    }

    fun resend_email(view: View) {

        progressDialog.setMessage("resending email")
        progressDialog.show()

        var request = object : StringRequest(
            Request.Method.POST, URLs.RESEND_EMAIL,
            Response.Listener { response ->
                try {

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss()
                    }
                    var mainOb = JSONObject(response)
                    var message = mainOb.getString("message")
                    if (mainOb.getBoolean("error")) {
                        Dialogs.showMessage(this, message)
                    } else {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "exception : " + e);
                    Dialogs.showMessage(this, Constants.error_message_exception)
                }
            },
            Response.ErrorListener { error ->

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss()
                }
                Log.i(TAG, "error : " + error);
                Dialogs.showMessage(this, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["email"] = email
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)

    }


    fun log_out(view: View) {

        PreferenceManager.getInstance(applicationContext)!!.removeActiveUser()
        var intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }


}
