package aust.fyp.learn.and.earn.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Adapters.MessageAdapter
import aust.fyp.learn.and.earn.Models.MessageModel
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.SocketConnectionHandler
import com.github.nkzawa.socketio.client.Ack
import com.github.nkzawa.socketio.client.Socket
import io.realm.Realm
import io.realm.RealmResults
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {

    lateinit var message: EditText
    lateinit var recView: RecyclerView
    lateinit var receiver_name: TextView
    var receiver_id: Int = 0
    var socket: Socket? = null
    var TAG = "ChatActivity"

    lateinit var realm: Realm
    lateinit var realmResults: RealmResults<MessageModel>
    lateinit var list: ArrayList<MessageModel>
    lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar!!.hide()

        socket = SocketConnectionHandler.getDefaultSocket()
        realm = Realm.getDefaultInstance()
        receiver_id = intent!!.getIntExtra("ID", 0)

        list = ArrayList()
        adapter =
            MessageAdapter(list, PreferenceManager.getInstance(applicationContext!!)!!.getUserId())

        recView = findViewById(R.id.recView)
        recView.layoutManager = LinearLayoutManager(this)
        recView.adapter = adapter

        realmResults = realm.where(MessageModel::class.java)
            .equalTo("sender_id", PreferenceManager.getInstance(applicationContext!!)!!.getUserId())
            .and()
            .equalTo("receiver_id", receiver_id)
            .or()
            .equalTo("sender_id", receiver_id)
            .and()
            .equalTo(
                "receiver_id",
                PreferenceManager.getInstance(applicationContext!!)!!.getUserId()
            )
            .findAll()

        proccesModels(realmResults)

        realmResults.addChangeListener { realmResults ->
            proccesModels(realmResults)
        }


        receiver_name = findViewById(R.id.receiver_name)

        message = findViewById(R.id.message)

        receiver_name.setText(intent!!.getStringExtra("teacher_name"))


    }

    private fun proccesModels(realmResults: RealmResults<MessageModel>?) {
        list.clear()
        adapter.notifyDataSetChanged()
        if (realmResults != null) {
            realmResults.forEach { model ->
                list.add(model)
                adapter.notifyDataSetChanged()
            }
        }

        if (list.size > 1) {
            recView.scrollToPosition(list.size - 1)
        }

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

                        if (data.getBoolean("error")) {
                            Toast.makeText(
                                applicationContext,
                                data.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            message.setText("")
                            realm.executeTransaction { realm ->
                                var prev_model = realm.where(MessageModel::class.java)
                                    .equalTo("ID", data.getJSONObject("message_ob").getInt("ID"))
                                    .findFirst()

                                if (prev_model != null) {
                                    prev_model.deleteFromRealm()
                                }

                                realm.createObjectFromJson(
                                    MessageModel::class.java,
                                    data.getJSONObject("message_ob")
                                )

                            }

                        }
                    }
                })

            } catch (e: Exception) {
                Log.i(TAG, "exception: $e")
            }
        }
    }
}
