package aust.fyp.learn.and.earn.Activities

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import aust.fyp.learn.and.earn.R
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import com.opentok.android.Session
import com.opentok.android.Stream
import com.opentok.android.Publisher
import com.opentok.android.PublisherKit
import com.opentok.android.Subscriber
import com.opentok.android.OpentokError
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import android.opengl.GLSurfaceView
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import aust.fyp.learn.and.earn.StoreRoom.Dialogs
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.SocketConnectionHandler
import com.github.nkzawa.socketio.client.Ack
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONObject
import javax.microedition.khronos.opengles.GL

class VideoCall : AppCompatActivity(), Session.SessionListener, PublisherKit.PublisherListener {

    val perms = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    lateinit var mPublisherViewContainer: FrameLayout
    lateinit var mSubscriberViewContainer: FrameLayout

    lateinit var mSession: Session
    lateinit var mPublisher: Publisher
    var mSubscriber: Subscriber? = null

    companion object {
        var API_KEY: String = "46901874"
         var SESSION_ID: String = "2_MX40NjkwMTg3NH5-MTU5ODc3NzQ4NTA5MH5jZFFEOGtYM2ovY2FZejN4ZWpKdTV5N3Z-fg"
         var TOKEN: String = "T1==cGFydG5lcl9pZD00NjkwMTg3NCZzaWc9YjZjZDAyNWQ2YTQ5OTgyOTdhNzVlYzNhYjJiMDE5YmQ4Njc2MmM2YzpzZXNzaW9uX2lkPTJfTVg0ME5qa3dNVGczTkg1LU1UVTVPRGMzTnpRNE5UQTVNSDVqWkZGRU9HdFlNMm92WTJGWmVqTjRaV3BLZFRWNU4zWi1mZyZjcmVhdGVfdGltZT0xNTk4Nzc3NTU5Jm5vbmNlPTAuNTc1NDQzNzA0MzkzODI5JnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE2MDEzNjk1NTgmaW5pdGlhbF9sYXlvdXRfY2xhc3NfbGlzdD0="
        var LOG_TAG: String = "VideoCall"
        var RC_SETTINGS_SCREEN_PERM: Int = 123
        var RC_VIDEO_APP_PERM: Int = 124
        var permissions_code = 200
    }

    var TAG = "VideoCall"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call)

        mPublisherViewContainer = findViewById(R.id.mPublisherViewContainer)
        mSubscriberViewContainer = findViewById(R.id.mSubscriberViewContainer)

        startClassRoom()
    }

    fun startClassRoom() {
        if (permissionsAllow()) {
            establishSession()
        } else {
            requestPermissions()
        }
    }

    fun permissionsAllow(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(this, perms, permissions_code)
    }

    fun establishSession() {
        mSession = Session.Builder(this, API_KEY, SESSION_ID).build()
        mSession.setSessionListener(this)
        mSession.connect(TOKEN)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissions_code) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(applicationContext, "Granted Successfully", Toast.LENGTH_SHORT)
                    .show()
                establishSession()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Cant work without permissions",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onStreamDropped(p0: Session?, p1: Stream?) {
        if (mSubscriber != null) {
            mSubscriber = null
            mSubscriberViewContainer.removeAllViews()
        }
    }

    override fun onStreamReceived(p0: Session?, stream: Stream?) {
        if (mSubscriber == null) {
            mSubscriber = Subscriber.Builder(this, stream).build()
            mSession.subscribe(mSubscriber)
            mSubscriberViewContainer.addView(mSubscriber!!.getView())
        }
    }

    override fun onConnected(p0: Session?) {

        mPublisher = Publisher.Builder(this).build()
        mPublisher.setPublisherListener(this)

        mPublisherViewContainer.addView(mPublisher.getView())

        if (mPublisher.getView() is GLSurfaceView) {
            (mPublisher.view as GLSurfaceView).setZOrderOnTop(true)
        }

        mSession.publish(mPublisher)

    }

    override fun onDisconnected(p0: Session?) {

    }

    override fun onError(p0: Session?, p1: OpentokError?) {
        Log.i(TAG,"error: $p0")
        Log.i(TAG,"error: $p1")
    }

    override fun onStreamCreated(p0: PublisherKit?, p1: Stream?) {

    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {

    }

    override fun onError(p0: PublisherKit?, p1: OpentokError?) {
        Log.i(TAG,"error: $p0")
        Log.i(TAG,"error: $p1")
    }

}
