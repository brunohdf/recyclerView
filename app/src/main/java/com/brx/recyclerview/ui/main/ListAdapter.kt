package com.brx.recyclerview.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.brx.recyclerview.R
import java.util.*

class ListAdapter(private val list: List<Date>) : RecyclerView.Adapter<ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ListViewHolder(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position])
    }

}

class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(date: Date) {
        val title = view.findViewById<TextView>(R.id.text)
        title.text = date.toString(SIMPLE_DATE)
    }
}