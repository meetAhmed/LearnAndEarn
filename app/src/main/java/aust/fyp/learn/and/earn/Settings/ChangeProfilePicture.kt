package aust.fyp.learn.and.earn.Settings

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
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
import aust.fyp.learn.and.earn.StoreRoom.Dialogs
import aust.fyp.learn.and.earn.StoreRoom.Store
import designer.ahmed.android_class.batutils.helper


class ChangeProfilePicture : AppCompatActivity() {

    lateinit var profile_image: ImageView
    var permissionArr = arrayOf(READ_EXTERNAL_STORAGE)
    var storageReqCode: Int = 101
    var permissionCode: Int = 100
    var file_path: String? = null
    var isGalleryOpenRequest: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile_picture)

        profile_image = findViewById(R.id.profile_image)

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

              //  Log.i("test", file_path)
               // Log.i("test", "" + bitmap)
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

    }

}
