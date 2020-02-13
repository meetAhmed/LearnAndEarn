package aust.fyp.learn.and.earn.StoreRoom

import android.graphics.Bitmap
import aust.fyp.learn.and.earn.Models.CategoryModel
import aust.fyp.learn.and.earn.R
import java.text.SimpleDateFormat

object Store {

    fun getCategories(): ArrayList<CategoryModel> {
        var list = ArrayList<CategoryModel>()
        list.add(CategoryModel("MATRIC", R.drawable.matric))
        list.add(CategoryModel("FSC", R.drawable.fsc))
        list.add(CategoryModel("BS", R.drawable.bs))
        list.add(CategoryModel("MS", R.drawable.ms))
        list.add(CategoryModel("Others", R.drawable.filled_grey))
        return list
    }

    fun getTime(time: Long): String {
        return SimpleDateFormat("EEE, d-MMM-yyyy ' at ' h:mm a").format(time)
    }

}