package aust.fyp.learn.and.earn.Services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import aust.fyp.learn.and.earn.Activities.LoginActivity
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.RequestHandler
import aust.fyp.learn.and.earn.StoreRoom.URLs
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.i(TAG, "${remoteMessage.data.toString()}")
        try {

            var data: Map<String, String> = remoteMessage.data
            var mainOb = JSONObject(data)
            var title = mainOb.getString("title")
            var text = mainOb.getString("text")
            var time = mainOb.getLong("time")
            var userID = mainOb.getInt("userID")

            if (PreferenceManager.getInstance(applicationContext!!)!!.isUserActive()) {
                if (PreferenceManager.getInstance(applicationContext!!)!!.getUserId() == userID) {
                    notificationDialog(title, text, time)
                }
            }

        } catch (e: Exception) {
            Log.i(TAG, "=Exception: $e")
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun notificationDialog(
        title: String,
        text: String,
        time: Long
    ) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "notifications"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") val notificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_MAX
                )
            // Configure the notification channel.
            notificationChannel.description = "Notification from " + getString(R.string.app_name)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.YELLOW
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(time)
            .setSmallIcon(R.mipmap.app_icon)
            .setTicker(getString(R.string.app_name))
            .setPriority(Notification.PRIORITY_MAX)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
        notificationManager.notify(0, notificationBuilder.build())
    }

}