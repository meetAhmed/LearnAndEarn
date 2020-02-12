package aust.fyp.learn.and.earn.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.SocketConnectionHandler
import com.github.nkzawa.socketio.client.Ack
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {

    lateinit var message: EditText
    lateinit var recView: RecyclerView
    lateinit var receiver_name: TextView
    var receiver_id: Int = 0
    var socket: Socket? = null
    var TAG = "ChatActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar!!.hide()

        socket = SocketConnectionHandler.getDefaultSocket()

        receiver_name = findViewById(R.id.receiver_name)
        recView = findViewById(R.id.recView)
        message = findViewById(R.id.message)

        receiver_name.setText(intent!!.getStringExtra("teacher_name"))
        receiver_id = intent!!.getIntExtra("ID", 0)
    }

    fun sendMessage(view: View) {
        var message_str = message.text.toString().trim()
        if (!message_str.isEmpty()) {
            try {

                var json = JSONObject()
                json.put(
                    "sender_id",
                    PreferenceManager.getInstance(applicationContext!!)!!.getUserId()
                )
                json.put(
                    "sender_name",
                    PreferenceManager.getInstance(applicationContext!!)!!.getUserName()
                )
                json.put(
                    "receiver_id",
                    receiver_id
                )
                json.put(
                    "receiver_name",
                    receiver_name.text.toString().trim()
                )
                json.put(
                    "message",
                    message_str
                )

                socket!!.emit("send-message", json, Ack { args ->
                    runOnUiThread {
                        var data = args[0] as JSONObject
                        Log.i(TAG, "{$data}")
                        message.setText("")
                    }
                })

            } catch (e: Exception) {
                Log.i(TAG, "exception: $e")
            }
        }
    }
}
