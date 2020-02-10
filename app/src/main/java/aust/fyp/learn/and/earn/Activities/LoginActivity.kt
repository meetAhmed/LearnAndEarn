package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.realm.Realm
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var progressDialog: ProgressDialog

    companion object {
        var TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)


        Realm.getDefaultInstance().executeTransaction { realm ->
            realm.deleteAll()
        }

        if (PreferenceManager.getInstance(applicationContext)!!.isUserActive()) {
            if (PreferenceManager.getInstance(applicationContext)!!.getAccountStatus()!!.trim().equals(
                    "unverified"
                )
            ) {
                var intent = Intent(this, VerificationActivity::class.java)
                var pack = Bundle()
                pack.putString("state", "loggeg_in")
                intent.putExtras(pack)
                startActivity(intent)
                finish()
            } else {

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

    }

    fun login(view: View) {

        val email_str = email.text.toString().trim()
        val password_str = password.text.toString().trim()

        var isError = false

        if (password_str.isEmpty() || password_str.length < 8) {
            isError = true
            password.error = "please enter the password having minium 8 charcters"
            password.requestFocus()
        }

        if (email_str.isEmpty()) {
            isError = true
            email.error = "please enter your email"
            email.requestFocus()
        }

        if (!isError) {
            openapplication(email_str, password_str)
        }

    }

    fun restore_password(view: View) {

        password.setError(null)
        password.clearFocus()

        if (email.text.toString().trim().isEmpty()) {
            email.error = "please enter your email"
            email.requestFocus()
        } else {


            progressDialog.setMessage("sending verification email")
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
                            var intent = Intent(this, VerificationActivity::class.java)
                            var pack = Bundle()
                            pack.putString("state", "forgot_password")
                            pack.putString("email", email.text.toString().trim())
                            intent.putExtras(pack)
                            startActivity(intent)
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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
                    map["email"] = email.text.toString().trim()
                    return map
                }
            }

            RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)
        }
    }

    private fun openapplication(emailStr: String, passwordStr: String) {

        progressDialog.setMessage("checking account")
        progressDialog.show()

        var request = object : StringRequest(
            Request.Method.POST, URLs.LOGIN,
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
                        var userOb = mainOb.getJSONObject("user")

                        PreferenceManager.getInstance(applicationContext)!!.setActiveUser()
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserId(userOb.getInt("ID"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserName(userOb.getString("name"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserAddress(userOb.getString("address"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserPhone(userOb.getString("phone_number"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserEmail(userOb.getString("email_address"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserPassword(userOb.getString("password"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserProfile(userOb.getString("profile_addresss"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserAccountType(userOb.getString("account_type"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setAccountStatus(userOb.getString("status"))

                        if (PreferenceManager.getInstance(applicationContext)!!.getAccountStatus()!!.trim().equals(
                                "unverified"
                            )
                        ) {
                            var intent = Intent(this, VerificationActivity::class.java)
                            var pack = Bundle()
                            pack.putString("state", "loggeg_in")
                            intent.putExtras(pack)
                            startActivity(intent)
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                            finish()
                        } else {

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
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss()
                }
                Log.i(TAG, "error : " + error);
                Dialogs.showMessage(this, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["email"] = emailStr
                map["password"] = passwordStr
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)
    }

    fun teacher_account(view: View) {
        val intent = Intent(this, CreateTeacherAccount::class.java)
        startActivity(intent)

    }

    fun student_account(view: View) {
        val intent = Intent(this, CreateAccountStudent::class.java)
        startActivity(intent)

    }
}

