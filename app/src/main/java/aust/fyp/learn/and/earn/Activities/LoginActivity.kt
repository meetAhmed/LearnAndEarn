package aust.fyp.learn.and.earn.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var teacheraccount: Button
    lateinit var studentaccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        teacheraccount = findViewById(R.id.teacheraccount);
        studentaccount = findViewById(R.id.studentaccount);
    }

    fun login_account_click(view: View) {

        val email_str = email.text.toString().trim()
        val password_str = password.text.toString().trim()


        var isError = false


        if (password_str.isEmpty() || password_str.length < 8) {
            isError = true
            password.error = "Please enter valid password of length minimum 8 characters"
            password.requestFocus()
        }

        if (email_str.isEmpty()) {
            isError = true
            email.error = "Please enter valid email address"
            email.requestFocus()
        }

        if (!isError) {
            executeRequestForlogin_account(email_str, password_str);
        }

    }

    private fun executeRequestForlogin_account(

        emailStr: String,
        passwordStr: String)


    {

        val URLs = URLs()
        var request = object : StringRequest(
            Request.Method.POST, URLs.login_account,
            Response.Listener { response ->




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

}
