package aust.fyp.learn.and.earn.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class LoginActivity : AppCompatActivity() {
    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var teacherAccount : Button
    lateinit var studentAccount : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        teacherAccount = findViewById(R.id.teacherAccount)
        studentAccount = findViewById(R.id.studentAccount)
    }
    fun login(view: View){

        val email_str = email.text.toString().trim()
        val password_str = password.text.toString().trim()

        var isError = false

        if(password_str.isEmpty() || password_str.length <8){
            isError = true
            password.error = "please enter the password having minium 8 charcters"
            password.requestFocus()
        }

        if(email_str.isEmpty()){
            isError = true
            email.error = "please enter your email"
            email.requestFocus()
        }

        if(!isError){
            openapplication(email_str,password_str)
        }

    }

    private fun openapplication(emailStr: String, passwordStr: String) {

        val URLs = URLs()
        var request = object : StringRequest(
            Request.Method.POST, URLs.create_account,
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
  fun teacher_account(view: View){
      val intent = Intent(this, CreateTeacherAccount::class.java)
      startActivity(intent)

  }
    fun student_account(view: View){
        val  intent = Intent(this, CreateAccountStudent::class.java )
        startActivity(intent)

    }
}

