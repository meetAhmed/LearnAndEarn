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
        list.add(CategoryModel("PHD", R.drawable.phd))
        list.add(CategoryModel("Medical", R.drawable.medical))
        list.add(CategoryModel("Primary-Class", R.drawable.primary))
        list.add(CategoryModel("Secondary-Class", R.drawable.secondary))
        list.add(CategoryModel("Science", R.drawable.science))
        list.add(CategoryModel("Engineering", R.drawable.engineering))
        list.add(CategoryModel("Diploma", R.drawable.diploma))
        list.add(CategoryModel("Arts", R.drawable.arts))
        list.add(CategoryModel("Religion", R.drawable.religion))
        list.add(CategoryModel("Cooking", R.drawable.cooking))
        list.add(CategoryModel("Others", R.drawable.others))
        return list
    }

    fun getTime(time: Long): String {
        return SimpleDateFormat("EEE, d-MMM-yyyy ' at ' h:mm a").format(time)
    }

}