package aust.fyp.learn.and.earn.StoreRoom

import android.R.attr.data
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import aust.fyp.learn.and.earn.Activities.Student_call_activity
import aust.fyp.learn.and.earn.Activities.VideoCall
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.StoreRoom.Constants.MESSAGE_RECEIVED
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Ack
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONObject


class SocketConnectionHandler private constructor() {

    companion object {

        private var socket: Socket? = null
        private var INSTANCE: SocketConnectionHandler? = null
        private var TAG = "SocketConnectionHandler"
        private var context: Context? = null

        fun init(ctx: Context) {

            if (INSTANCE == null) {

                context = ctx

                socket = IO.socket(URLs.PROTOCOL + URLs.IP_ADDRESS + URLs.PORT)
                socket!!.connect()
                INSTANCE = SocketConnectionHandler()

                try {

                    var json = JSONObject()
                    json.put("userID", PreferenceManager.getInstance(context!!)!!.getUserId())
                    json.put("user_name", PreferenceManager.getInstance(context!!)!!.getUserName())

                    socket!!.emit("join", json)

                    socket!!.on("ping") { args ->
                        var data = args[0] as JSONObject
                        Log.i(TAG, "{$data}")

                        socket!!.emit("pong", json, Ack { args ->
                            var data = args[0] as JSONObject
                            Log.i(TAG, "{$data}")

                        })
                    }

                    socket!!.on("message-received") { args ->
                        var data = args[0] as JSONObject
                        Log.i(TAG, "{$data}")
                        var intent = Intent(MESSAGE_RECEIVED)
                        intent.putExtra("data", data.toString())
                        LocalBroadcastManager.getInstance(context!!).sendBroadcast(intent)
                    }

                    socket!!.on("class-start") { args ->
                        var data = args[0] as JSONObject
                        Log.i(TAG, "{$data}")
                        var intent = Intent(context!!, Student_call_activity::class.java)
                        intent.putExtra("data", data.toString())
                        context!!.startActivity(intent)
                    }

                    socket!!.on("class-req-accepted") { args ->
                        Log.i(TAG, "class-req-accepted")
                    }

                    socket!!.on("class-req-rejected") { args ->
                        Log.i(TAG, "class-req-rejected")
                    }

                } catch (e: Exception) {
                    Log.i(TAG, "Exception: ${e}")
                }
            }
        }

        fun getDefaultInstance(): SocketConnectionHandler? = INSTANCE

        fun getDefaultSocket(): Socket? = socket

    }


}