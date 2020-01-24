package aust.fyp.learn.and.earn.Activities

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.URLs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.net.URL

class CreateTeacherAccount : AppCompatActivity() {
    lateinit var full_name: EditText
    lateinit var address: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var employment_history:EditText
    lateinit var certifications:EditText
    lateinit var phone_number:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_teacher_account)


        full_name = findViewById(R.id.full_name)
        password = findViewById(R.id.password)
        address = findViewById(R.id.address)
        email = findViewById(R.id.email)
        employment_history = findViewById(R.id.employment_history)
        phone_number = findViewById(R.id.phone_number)
        certifications=findViewById(R.id.certifications)

    }

    fun create_account_click(view: View) {

        val full_name_str = full_name.text.toString().trim()
        val address_str = address.text.toString().trim()
        val email_str = email.text.toString().trim()
        val password_str = password.text.toString().trim()
        val employment_history_str = employment_history.text.toString().trim()
        val phone_number_str = phone_number.text.toString().trim()
        val certifications_str = certifications.text.toString().trim()


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
        if(employment_history_str.isEmpty()){
            isError = true 
            employment_history.error = "please enter the employment history"
            employment_history.requestFocus()
        }
        if(certifications_str.isEmpty()){
            isError = true
            certifications.error = "please enter the employment history"
            certifications.requestFocus()
        }
        if(phone_number_str.isEmpty()){
            isError = true
            phone_number.error = "please enter the employment history"
            phone_number.requestFocus()
        }

        if (address_str.isEmpty()) {
            isError = true
            address.error = "Please enter valid address"
            address.requestFocus()
        }

        if (full_name_str.isEmpty()) {
            isError = true
            full_name.error = "Please enter valid full name"
            full_name.requestFocus()
        }

        if (!isError) {
            executeRequestForAccountCreation(full_name_str, address_str,phone_number_str,certifications_str,employment_history_str, email_str, password_str);
        }


    }

    private fun executeRequestForAccountCreation(
        fullNameStr: String,
        addressStr: String,
        phoneNumberStr: String,
        certificationsStr: String,
        employmentHistoryStr: String,
        emailStr: String,
        passwordStr: String
    ) {

        val URLs = URLs()
        var request = object : StringRequest(Request.Method.POST,URLs.create_account ,
            Response.Listener { responce ->

        }, Response.ErrorListener {error ->
                Toast.makeText(applicationContext , "something went wrong ",Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["name"] = fullNameStr
                map["address"] = addressStr
                map["phone_number"] = phoneNumberStr
                map["certification"] = certificationsStr
                map["employment_history"] = employmentHistoryStr
                map["email"] = emailStr
                map["password"] = passwordStr
                map["type"] = "teacher"
                return map
            }
        }
        Volley.newRequestQueue(applicationContext).add(request)
    }


}