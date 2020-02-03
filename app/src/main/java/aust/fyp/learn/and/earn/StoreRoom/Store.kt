package aust.fyp.learn.and.earn.StoreRoom

import android.graphics.Bitmap


object Store {

    fun resizeImage(image: Bitmap?): Bitmap? {
        var image: Bitmap? = image ?: return null
        val maxWidth = 1200
        val maxHeight = 1200
        return if (maxHeight > 0 && maxWidth > 0) {
            val width = image!!.width
            val height = image!!.height
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            image = Bitmap.createScaledBitmap(image!!, finalWidth, finalHeight, true)
            image
        } else {
            image
        }
    } // function ends here


}