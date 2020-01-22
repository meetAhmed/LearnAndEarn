package aust.fyp.learn.and.earn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(var list: ArrayList<Person>) : RecyclerView.Adapter<Adapter.myViewsHolder>() {

    class myViewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.name)
        var country: TextView = itemView.findViewById(R.id.country)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.myViewsHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.single_view, null)
        return myViewsHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Adapter.myViewsHolder, position: Int) {
        holder.name.text = list.get(position).name
        holder.country.text = list.get(position).country
    }
}