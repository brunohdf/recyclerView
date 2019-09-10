package com.brx.recyclerview.ui.main

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.brx.recyclerview.R
import java.util.*

class ListAdapter(private val context: Context, private val list: MutableList<Date>) :
    RecyclerView.Adapter<ListViewHolder>(), TouchAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ListViewHolder(context, item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(list, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(list, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

}

class ListViewHolder(val context: Context, val view: View) : RecyclerView.ViewHolder(view),
    TouchViewHolder {
    fun bind(date: Date) {
        val title = view.findViewById<TextView>(R.id.text)
        title.text = date.toString(SIMPLE_DATE)
    }

    override fun onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY)
    }

    override fun onItemClear() {
        itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_yellow))
    }
}