package aust.fyp.learn.and.earn.Services

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import aust.fyp.learn.and.earn.Activities.LoginActivity
import aust.fyp.learn.and.earn.Activities.VerificationActivity
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class FirebaseService : FirebaseMessagingService() {

    var TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "${token}")

        if (PreferenceManager.getInstance(applicationContext!!)!!.isUserActive()) {
            uploadToken(token)
        }

    }

    fun uploadToken(token: String) {
        var request = object : StringRequest(
            Request.Method.POST, URLs.ADD_TOKEN,
            Response.Listener { response ->

                try {

                    var mainOb = JSONObject(response)
                    Log.i(TAG, mainOb.toString())
                } catch (e: Exception) {
                    Log.i(TAG, "exception : " + e);
                }


            },
            Response.ErrorListener { error ->
                Log.i(TAG, "error : " + error);
            }) {
            override fun getParams(): MutableMap<String, String> {
                var map = HashMap<String, String>()
                map["token"] = token
                map["userID"] =
                    PreferenceManager.getInstance(applicationContext!!)!!.getUserId().toString()
                return map
            }
        }

        RequestHandler.getInstance(applicationContext)!!.addToRequestQueue(request)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.i(TAG, "${remoteMessage.data}")
    }
}