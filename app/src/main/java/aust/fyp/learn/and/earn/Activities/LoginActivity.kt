package aust.fyp.learn.and.earn.Activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
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

class LoginActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
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

    private fun openapplication(emailStr: String, passwordStr: String) {

        var request = object : StringRequest(
            Request.Method.POST, URLs.LOGIN,
            Response.Listener { response ->

                try {

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

                        PreferenceManager.getInstance(applicationContext).setActiveUser()
                        PreferenceManager.getInstance(applicationContext)
                            .setUserId(userOb.getInt("ID"))
                        PreferenceManager.getInstance(applicationContext)
                            .setUserName(userOb.getString("name"))
                        PreferenceManager.getInstance(applicationContext)
                            .setUserAddress(userOb.getString("address"))
                        PreferenceManager.getInstance(applicationContext)
                            .setUserPhone(userOb.getString("phone_number"))
                        PreferenceManager.getInstance(applicationContext)
                            .setUserEmail(userOb.getString("email_address"))
                        PreferenceManager.getInstance(applicationContext)
                            .setUserPassword(userOb.getString("password"))
                        PreferenceManager.getInstance(applicationContext)
                            .setUserProfile(userOb.getString("profile_addresss"))
                        PreferenceManager.getInstance(applicationContext)
                            .setUserAccountType(userOb.getString("account_type"))
                        PreferenceManager.getInstance(applicationContext)
                            .setAccountStatus(userOb.getString("status"))

                        if (PreferenceManager.getInstance(applicationContext).getUserAccountType()!!.trim().equals(
                                Constants.TEACHER
                            )
                        ) {
                            startActivity(Intent(this, MainActivityTeacher::class.java))
                        } else {
                            startActivity(Intent(this, MainActivityStudent::class.java))
                        }
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["email"] = emailStr
                map["password"] = passwordStr
                return map
            }
        }

        Volley.newRequestQueue(applicationContext).add(request)
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

