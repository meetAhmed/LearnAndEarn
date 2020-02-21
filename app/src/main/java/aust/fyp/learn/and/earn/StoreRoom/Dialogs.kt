package aust.fyp.learn.and.earn.StoreRoom

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import aust.fyp.learn.and.earn.Interfaces.AlertDialogInterface
import java.security.AccessControlContext

object Dialogs {

    fun showMessage(
        activity: Activity,
        message: String,
        positiveButtonText: String,
        listener: AlertDialogInterface
    ) {

        var builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText, { dialogInterface, i ->
            listener.positiveButtonClick(dialogInterface)
        })

        var alert = builder.create()
        alert.show()
    }


    fun showMessage(
        activity: Activity,
        message: String
    ) {

        var builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
        builder.setPositiveButton("OK", { dialogInterface, i ->
            dialogInterface.dismiss()
        })

        var alert = builder.create()
        alert.show()
    }


    fun showMessage(
        context: Context,
        message: String
    ) {

        var builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton("OK", { dialogInterface, i ->
            dialogInterface.dismiss()
        })

        var alert = builder.create()
        alert.show()
    }

}