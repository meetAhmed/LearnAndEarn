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

class Teacher_Call_activity : AppCompatActivity() {

    lateinit var student_name: TextView
    lateinit var subject_name: TextView
    var TAG = "TeacherCall"
    var student_id = 0
    var socket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher__call_activity)

        student_id = intent.getIntExtra("student_id", 0)

        socket = SocketConnectionHandler.getDefaultSocket()

        student_name = findViewById(R.id.student_name)
        subject_name = findViewById(R.id.subject_name)

        subject_name.setText("Subject : " + intent.getStringExtra("subject_name"))
        student_name.setText("Student : " + intent.getStringExtra("student_name"))

        try {

            var reqObject = JSONObject()
            reqObject.put("student_id", student_id)
            reqObject.put(
                "teacher_id",
                PreferenceManager.getInstance(applicationContext!!)!!.getUserId()
            )
            reqObject.put(
                "teacher_name",
                PreferenceManager.getInstance(applicationContext!!)!!.getUserName()
            )

            reqObject.put(
                "subject_name",
                intent.getStringExtra("subject_name")
            )

            reqObject.put(
                "TOKEN",
                "unique"
            )

            reqObject.put(
                "SESSION_ID",
                "unique"
            )

            socket!!.emit("start-class", reqObject, Ack { args ->
                var data = args[0] as JSONObject
                Log.i(TAG, "{$data}")
                if (data.getBoolean("error")) {
                    runOnUiThread {
                        Dialogs.showMessage(
                            this@Teacher_Call_activity,
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
                }
            })

            socket!!.on("class-req-rejected") { args ->
                runOnUiThread {
                    Dialogs.showMessage(
                        this@Teacher_Call_activity,
                        "Student has rejected class request",
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
            }

            socket!!.on("class-req-accepted") { args ->
                Log.i(TAG,"class-req-accepted")
                runOnUiThread {
                    Toast.makeText(applicationContext, "call started", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, VideoCall::class.java))
                }
            }

        } catch (e: Exception) {
            Log.i(TAG, "Exception: $e")
        }
    }

    fun cancel(view: View) {
        try {

            var reqObject = JSONObject()
            reqObject.put("student_id", student_id)
            socket!!.emit("class-req-cancelled", reqObject)
            runOnUiThread { finish() }
        } catch (e: Exception) {
            Log.i(TAG, "Exception: $e")
        }
    }
}
