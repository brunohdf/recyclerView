package com.brx.recyclerview.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

// based on https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(com.brx.recyclerview.R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        context?.let { ctx ->
            val list = mutableListOf<Date>()
            val adapter = ListAdapter(ctx, list)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(getActivity(), 3)

            val callback = TouchHelperCallback(adapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(recyclerView)

            viewModel.loadData.observe(this, android.arch.lifecycle.Observer { data ->
                data?.let {
                    list.clear()
                    list.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }

}
