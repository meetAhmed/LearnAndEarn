package aust.fyp.learn.and.earn.Activities

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

class CreateAccountTeacher : AppCompatActivity() {


    lateinit var full_name: EditText
    lateinit var address: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var phonenumber:EditText
    lateinit var city:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_teacher)
        full_name=findViewById(R.id.full_name)
        address=findViewById(R.id.address)
        email=findViewById(R.id.email)
        phonenumber=findViewById(R.id.phonenumber)
        city=findViewById(R.id.City)


    }
    fun create_account_click(view: View){

        val full_name_str = full_name.text.toString().trim()
        val address_str = address.text.toString().trim()
        val email_str = email.text.toString().trim()
        val phonenumber_str=phonenumber.text.toString().trim()
        val city_str=city.text.toString().trim()
        val password_str = password.text.toString().trim()

        var isError = false

        if (password_str.isEmpty() || password_str.length < 8) {
            isError = true
            password.error = "Please enter valid password of length minimum 8 characters"
            password.requestFocus()
        }


        if (city_str.isEmpty()) {
            isError = true
            city.error = "Please enter valid city"
            city.requestFocus()
        }

        if (phonenumber_str.isEmpty()) {
            isError = true
            phonenumber.error = "Please enter valid Phone Number"
            phonenumber.requestFocus()
        }

        if (email_str.isEmpty()) {
            isError = true
            email.error = "Please enter valid email address"
            email.requestFocus()
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
            executeRequestForAccountCreation(full_name_str, address_str, email_str, password_str,phonenumber_str,city_str);
        }


    }

    private fun executeRequestForAccountCreation(
        fullNameStr: String,
        addressStr: String,
        emailStr: String,
        passwordStr: String,
        phonenumberstr:String,
        citystr:String
    )
    {

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
                map["name"] = fullNameStr
                map["address"] = addressStr
                map["email"] = emailStr
                map["phonenumber"]=phonenumberstr
                map["city"]=citystr
                map["password"] = passwordStr
                map["type"] = "teacher"
                return map
            }
        }

        Volley.newRequestQueue(applicationContext).add(request)
    }

}