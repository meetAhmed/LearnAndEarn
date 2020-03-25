package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class CreateAccountStudent : AppCompatActivity() {

    lateinit var full_name: EditText
    lateinit var address: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var phonenumber: EditText
    lateinit var progressDialog: ProgressDialog

    companion object {
        var TAG = "CreateAccountStudent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account_student)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)


        phonenumber = findViewById(R.id.phonenumber)
        full_name = findViewById(R.id.full_name)
        password = findViewById(R.id.password)
        address = findViewById(R.id.address)
        email = findViewById(R.id.email)

    }

    fun create_account_click(view: View) {

        val full_name_str = full_name.text.toString().trim()
        val address_str = address.text.toString().trim()
        val email_str = email.text.toString().trim()
        val password_str = password.text.toString().trim()
        val phonenumber_str = phonenumber.text.toString().trim()

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


        if (phonenumber_str.isEmpty()) {
            isError = true
            phonenumber.error = "Please enter valid phone number"
            phonenumber.requestFocus()
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
            executeRequestForAccountCreation(
                full_name_str,
                address_str,
                email_str,
                password_str,
                phonenumber_str
            )
        }


    }

    private fun executeRequestForAccountCreation(
        fullNameStr: String,
        addressStr: String,
        emailStr: String,
        passwordStr: String,
        phonenumber_str: String
    ) {

        progressDialog.setMessage("Please wait - creating account")
        progressDialog.show()

        var request = object : StringRequest(Request.Method.POST, URLs.CREATE_ACCOUNT,
            Response.Listener { response ->

                Log.i(CreateTeacherAccount.TAG, "response : " + response)

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss()
                }

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

                        PreferenceManager.getInstance(applicationContext)!!.setActiveUser()

                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserPhone(userOb.getString("phone_number"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserId(userOb.getInt("ID"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserName(userOb.getString("name"))
                        PreferenceManager.getInstance(applicationContext)!!
                            .setUserAddress(userOb.getString("address"))
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

                        var intent = Intent(this, VerificationActivity::class.java)
                        var pack = Bundle()
                        pack.putString("state", "loggeg_in")
                        intent.putExtras(pack)
                        startActivity(intent)
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Log.i(CreateTeacherAccount.TAG, "Exception : " + e);
                    Dialogs.showMessage(this, Constants.error_message_volley)
                }
            }, Response.ErrorListener { error ->

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss()
                }

                Log.i(CreateTeacherAccount.TAG, "error : " + error)
                Dialogs.showMessage(this, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["name"] = fullNameStr
                map["address"] = addressStr
                map["email"] = emailStr
                map["phone_number"] = phonenumber_str
                map["password"] = passwordStr
                map["type"] = "student"
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)
    }

}
