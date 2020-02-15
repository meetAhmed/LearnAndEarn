package aust.fyp.learn.and.earn.Fragments.Student


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aust.fyp.learn.and.earn.Adapters.InboxStudentAdopter
import aust.fyp.learn.and.earn.Adapters.Subject_teachersListAdapter
import aust.fyp.learn.and.earn.Models.MessageModel
import aust.fyp.learn.and.earn.Models.SubjectModel

import aust.fyp.learn.and.earn.R
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_chat.*

class InboxFragmentStudent : Fragment() {

    lateinit var message:String
    lateinit var date:String
    lateinit var sender_name:String
    lateinit var layoutView: View
    lateinit var recView : RecyclerView
    lateinit var realm:Realm
    lateinit var realmList: RealmResults<MessageModel>
    lateinit var list: ArrayList<MessageModel>
    lateinit var adapter: InboxStudentAdopter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutView = inflater.inflate(R.layout.fragment_inbox_fragment_student, container, false)


        list = ArrayList<MessageModel>()
        adapter =InboxStudentAdopter(list)
        recView =layoutView.findViewById(R.id.recView)
       /* sender_name =layoutView.findViewById<>(R.id.sender_name)
        recView.layoutManager = LinearLayoutManager(context)
        recView.adapter = adapter
        message = layoutView.findViewById<>(R.id.message)
        date = layoutView.findViewById<>(R.id.date) */


        return layoutView


    }


}
