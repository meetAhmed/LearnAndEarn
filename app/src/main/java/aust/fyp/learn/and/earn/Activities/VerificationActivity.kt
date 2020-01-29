package aust.fyp.learn.and.earn.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.Constants
import aust.fyp.learn.and.earn.StoreRoom.Dialogs
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VerificationActivity : AppCompatActivity() {

    lateinit var code: EditText
    lateinit var email: String

    companion object {
        var TAG = "VerificationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_)

        code = findViewById(R.id.code)

        if (intent.extras!!.getString("state").equals("loggeg_in")) {
            email = PreferenceManager.getInstance(applicationContext)!!.getUserEmail()!!
        }

    }

    fun verify_code(view: View) {

        if (code.text.toString().trim().isEmpty()) {
            code.setError("please provide code")
            code.requestFocus()
        } else {

            var request = object : StringRequest(
                Request.Method.POST, URLs.VERIFY_CODE,
                Response.Listener { response ->
                    try {

                        var mainOb = JSONObject(response)
                        var message = mainOb.getString("message")
                        if (mainOb.getBoolean("error")) {
                            Dialogs.showMessage(this, message)
                        } else {

                            if (intent.extras!!.getString("state").equals("loggeg_in")) {
                                PreferenceManager.getInstance(applicationContext)!!.setAccountStatus("verified")
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
                    map["code"] = code.text.toString().trim()
                    map["email"] = email
                    return map
                }
            }

            Volley.newRequestQueue(applicationContext).add(request)

        }

    }

}
