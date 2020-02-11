package aust.fyp.learn.and.earn.StoreRoom

import android.R.attr.data
import android.content.Context
import android.util.Log
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

        fun init() {
            if (INSTANCE == null) {
                socket = IO.socket(URLs.PROTOCOL + URLs.IP_ADDRESS + URLs.PORT)
                socket!!.connect()
                INSTANCE = SocketConnectionHandler()

                try {

                    var json = JSONObject()
                    
                    socket!!.on("ping", { args ->
                        var data = args[0] as JSONObject
                        Log.i(TAG, "{$data}")




                        socket!!.emit("pong", json, Emitter.Listener { args ->
                            var data = args[0] as JSONObject
                            Log.i(TAG, "{$data}")
                        })
                    })

                } catch (e: Exception) {

                }
            }
        }

        fun getDefaultInstance(): SocketConnectionHandler? = INSTANCE

        fun getDefaultSocket(): Socket? = socket

    }


}