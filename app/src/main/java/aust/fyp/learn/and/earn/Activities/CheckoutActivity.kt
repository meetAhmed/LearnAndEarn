package aust.fyp.learn.and.earn.Activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import aust.fyp.learn.and.earn.StoreRoom.Dialogs.showMessage
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity() {

    var subject_id: Int = 0
    lateinit var cardInputWidget: CardInputWidget
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        supportActionBar!!.hide()

        cardInputWidget = findViewById(R.id.cardInputWidget)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)

        subject_id = intent.getIntExtra("ID", 0)
        findViewById<TextView>(R.id.subject_name).setText(intent.getStringExtra("subject_name"))
        findViewById<TextView>(R.id.teacher_name).setText(intent.getStringExtra("teacher_name"))
        findViewById<TextView>(R.id.price_per_month).setText(
            "$ " + intent.getDoubleExtra(
                "price_per_month",
                0.0
            )
        )

    }

    fun payment(view: View) {

        var card = cardInputWidget.card

        if (card == null) {
            Toast.makeText(this, "Please enter card details", Toast.LENGTH_SHORT).show()
            return
        }

        progressDialog.setMessage("Please wait")
        progressDialog.show()

        var stripe = Stripe(applicationContext, "pk_test_KJZS9UwuM7ldqAQ1etwch2zJ00rq3yzYoL")

        stripe.createToken(card!!, object : TokenCallback {
            override fun onSuccess(token: Token) {
                makeApiCall(token)
            }

            override fun onError(error: Exception) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss()
                }
                showMessage(this@CheckoutActivity, "Exception: $error");
            }

        })

    }

    private fun makeApiCall(token: Token) {

        var request = object : StringRequest(
            Request.Method.POST, URLs.PAY,
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
                        showMessage(this, message)
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        finish()
                    }
                } catch (e: Exception) {
                    Log.i(LoginActivity.TAG, "exception : " + e);
                    showMessage(this, Constants.error_message_exception)
                }

            },
            Response.ErrorListener { error ->
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss()
                }
                Log.i(LoginActivity.TAG, "error : " + error);
                Dialogs.showMessage(this, Constants.error_message_volley)
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["stripe_token"] = token.id
                map["student_id"] =
                    PreferenceManager.getInstance(applicationContext!!)!!.getUserId().toString()
                map["student_name"] =
                    PreferenceManager.getInstance(applicationContext!!)!!.getUserName().toString()
                map["student_email"] =
                    PreferenceManager.getInstance(applicationContext!!)!!.getUserEmail().toString()
                map["subject_id"] = subject_id.toString()
                map["amount"] = intent.getDoubleExtra(
                    "price_per_month",
                    0.0
                ).toString()
                map["teacher_id"] = intent.getIntExtra(
                    "teacher_id",
                    0
                ).toString()
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)


    }

}
