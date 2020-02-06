package aust.fyp.learn.and.earn.Fragments.Student


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Adapters.HomeFragmentStudentAdapter
import aust.fyp.learn.and.earn.Models.CategoryModel

import aust.fyp.learn.and.earn.R
import aust.fyp.learn.and.earn.StoreRoom.PreferenceManager
import aust.fyp.learn.and.earn.StoreRoom.Store

class HomeFragmentStudent : Fragment() {

    lateinit var recView: RecyclerView
    lateinit var list: ArrayList<CategoryModel>
    lateinit var adapter: HomeFragmentStudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list = Store.getCategories()
        adapter = HomeFragmentStudentAdapter(list)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_home_fragment_student, container, false)
        recView = view.findViewById(R.id.recView)
        recView.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        recView.adapter = adapter
        return view
    }


}
