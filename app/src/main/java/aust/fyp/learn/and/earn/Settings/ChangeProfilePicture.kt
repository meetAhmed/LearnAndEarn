package aust.fyp.learn.and.earn.Settings

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.*
import com.android.volley.Request.Method.POST
import com.koushikdutta.async.future.FutureCallback
import com.koushikdutta.ion.Ion
import com.koushikdutta.ion.ProgressCallback
import com.squareup.picasso.Picasso
import designer.ahmed.android_class.batutils.helper
import org.json.JSONObject
import java.io.File
import java.lang.Exception
import java.net.URL


class ChangeProfilePicture : AppCompatActivity() {

    lateinit var profile_image: ImageView
    var permissionArr = arrayOf(READ_EXTERNAL_STORAGE)
    var storageReqCode: Int = 101
    var permissionCode: Int = 100
    var file_path: String? = null
    var isGalleryOpenRequest: Boolean = false
    lateinit var progressDialog: ProgressDialog
    var TAG = "ChangeProfilePicture"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile_picture)

        profile_image = findViewById(R.id.profile_image)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)

        supportActionBar!!.hide()

        Picasso.get()
            .load(URLs.getImageUrl(PreferenceManager.getInstance(applicationContext!!)!!.getUserProfile()))
            .into(profile_image);

        if (!isReadStorageAllowed()) {
            ActivityCompat.requestPermissions(this, permissionArr, permissionCode)
        }

        profile_image.setOnClickListener {
            if (!isReadStorageAllowed()) {
                isGalleryOpenRequest = true;
                ActivityCompat.requestPermissions(this, permissionArr, permissionCode)
            } else {
                //open gallery
                isGalleryOpenRequest = false;
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, storageReqCode)
            }
        }

    }

    fun isReadStorageAllowed(): Boolean {
        return ActivityCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                var bitmap = helper.getBitmapFromUri(data.data, applicationContext)
                bitmap = helper.resizeImage(bitmap)
                profile_image.setImageBitmap(bitmap)
                file_path = helper.getFilePathFromUri(data.data, applicationContext)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == permissionCode) {
            if (grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                if (isGalleryOpenRequest) {
                    // open gallery
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, storageReqCode)
                } else {
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Dialogs.showMessage(
                    this,
                    "We will not be able to update profile picture without access to storage."
                )
            }
        }

    }

    fun upload_picture(view: View) {
        if (file_path == null) {
            Toast.makeText(applicationContext, "Please select Image first", Toast.LENGTH_SHORT)
                .show()
        } else {
            progressDialog.setMessage("Uploading profile image")
            progressDialog.show()

            var file = File(file_path)
            Ion.with(applicationContext).load("POST", URLs.UPLOAD)
                .uploadProgressHandler(object : ProgressCallback {
                    override fun onProgress(downloaded: Long, total: Long) {
                        var percent = (downloaded / total) * 100;
                        progressDialog.setMessage("Uploading profile image $percent %")
                    }
                })
                .setMultipartFile("image", file)
                .setMultipartParameter("action", "profile_update")
                .setMultipartParameter(
                    "userId",
                    PreferenceManager.getInstance(applicationContext)!!.getUserId().toString()
                )
                .asString()
                .setCallback(object : FutureCallback<String> {
                    override fun onCompleted(e: Exception?, result: String?) {
                        Log.i(TAG, "Exception: $e :: result: $result");
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss()
                        }

                        if (result != null) {
                            var mainObject = JSONObject(result)

                            if (mainObject.getBoolean("error")) {
                                Dialogs.showMessage(
                                    this@ChangeProfilePicture,
                                    mainObject.getString("message")
                                )
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    mainObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()

                                PreferenceManager.getInstance(applicationContext)!!.setUserProfile(
                                    mainObject.getString("profile")
                                )

                                finish()

                            }

                        } else {
                            Dialogs.showMessage(
                                this@ChangeProfilePicture,
                                Constants.error_message_volley
                            )
                        }
                    }
                })
        }
    }

}
