package com.brx.recyclerview.ui.main

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.brx.recyclerview.R
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class ListAdapter(
    private val context: Context,
    private val list: List<Date>
) : RecyclerView.Adapter<ListViewHolder>() {

    private var holders = hashMapOf<Int, RecyclerView.ViewHolder>()
    private var selectedPosition: Int? = null

    lateinit var listener: ListListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ListViewHolder(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(list[position], position, listener)

        holders[position] = holder
    }

    fun setSelectDate(position: Int, selectionByClick: Boolean = false) {
        val date = list[position]

        switchToNormal()
        selectedPosition = list.indexOf(date)
        switchToHighlighted()

        listener.onSelected(date, selectionByClick)
    }

    private fun switchToHighlighted() {
        selectedPosition?.let {
            val view = holders[it]?.itemView
            view?.text?.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        }
    }

    private fun switchToNormal() {
        selectedPosition?.let {
            val view = holders[it]?.itemView
            view?.text?.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    }
}

interface ListListener {
    fun onClick(date: Date, position: Int)
    fun onSelected(date: Date, selectionByClick: Boolean)
}

class ListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(date: Date, position: Int, listener: ListListener) {
        val card = view.findViewById<TextView>(R.id.text)
        card.text = date.toString(SIMPLE_DATE)

        card.setOnClickListener {
            listener.onClick(date, position)
        }
    }
}