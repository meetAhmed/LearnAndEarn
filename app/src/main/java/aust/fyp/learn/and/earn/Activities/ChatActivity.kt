package aust.fyp.learn.and.earn.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.SocketConnectionHandler
import com.github.nkzawa.socketio.client.Socket

class ChatActivity : AppCompatActivity() {

    lateinit var message: EditText
    lateinit var recView: RecyclerView
    lateinit var receiver_name: TextView
    var receiver_id: Int = 0
    var socket: Socket? = null

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

    fun send_message(view: View) {

    }
}
