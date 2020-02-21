package aust.fyp.learn.and.earn.Activities

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
import aust.fyp.learn.and.earn.StoreRoom.Dialogs
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.SocketConnectionHandler
import com.github.nkzawa.socketio.client.Ack
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONObject

class Student_call_activity : AppCompatActivity() {

    lateinit var techer_name: TextView
    lateinit var subject_name: TextView
    var TAG = "StudentCall"
    var teacher_id = 0
    var socket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_call_activity)

        techer_name = findViewById(R.id.techer_name)
        subject_name = findViewById(R.id.subject_name)
        socket = SocketConnectionHandler.getDefaultSocket()

        try {
            var mainOb = JSONObject(intent.getStringExtra("data"))
            teacher_id = mainOb.getInt("teacher_id")
            techer_name.setText("Teacher : ${mainOb.getString("teacher_name")}")
            subject_name.setText("Subject : ${mainOb.getString("subject_name")}")
        } catch (e: Exception) {
            Log.i(TAG, "$e")
        }

    }

    fun accept(view: View) {
        try {
            var reqObject = JSONObject()
            reqObject.put(
                "teacher_id",
                PreferenceManager.getInstance(applicationContext!!)!!.getUserId()
            )
            socket!!.emit("class-req-accepted", reqObject, Ack { args ->
                var data = args[0] as JSONObject
                Log.i(TAG, "{$data}")
                if (data.getBoolean("error")) {
                  runOnUiThread {
                      Dialogs.showMessage(
                          this@Student_call_activity,
                          data.getString("message"),
                          "Ok",
                          object : AlertDialogInterface {
                              override fun positiveButtonClick(dialogInterface: DialogInterface) {
                                  dialogInterface.dismiss()
                                  finish()
                              }

                              override fun negativeButtonClick(dialogInterface: DialogInterface) {

                              }

                          })
                  }
                } else {
                   runOnUiThread {
                       startActivity(Intent(applicationContext, VideoCall::class.java))
                   }
                }
            })

            socket!!.on("class-req-cancelled") { args ->
               runOnUiThread {
                   Toast.makeText(applicationContext, "call cancelled by teacher", Toast.LENGTH_SHORT)
                       .show()
                   finish()
               }
            }


        } catch (e: Exception) {
            Log.i(TAG, "Exception: $e")
        }
    }

    fun reject(view: View) {
        try {

            var reqObject = JSONObject()
            reqObject.put(
                "teacher_id",
                PreferenceManager.getInstance(applicationContext!!)!!.getUserId()
            )
            socket!!.emit("class-req-rejected", reqObject)
           runOnUiThread {  finish() }
        } catch (e: Exception) {
            Log.i(TAG, "Exception: $e")
        }
    }

}
