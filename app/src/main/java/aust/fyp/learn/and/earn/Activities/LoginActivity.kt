package aust.fyp.learn.and.earn.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import aust.fyp.learn.and.earn.R
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

                    if (mainOb.getBoolean("error")) {
                        // error
                    } else {

                        var userOb = mainOb.getJSONObject("user")

                    }

                } catch (e: Exception) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }



                Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["email"] = emailStr
                map["password"] = passwordStr
                map["type"] = "login"
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

