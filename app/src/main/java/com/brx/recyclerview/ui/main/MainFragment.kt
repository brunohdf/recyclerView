package com.brx.recyclerview.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brx.recyclerview.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        val list = mutableListOf<Date>()
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = LinearSnapHelper()

        val adapter = ListAdapter(context!!, list)
        adapter.listener = object : ListListener {
            override fun onClick(date: Date, position: Int) {
                snapToCenter(adapter, linearLayoutManager, position, snapHelper, true)
            }

            override fun onSelected(date: Date, selectionByClick: Boolean) {
                // during selection by click this event will be called twice
                if (!selectionByClick) {
                    label.text = date.toString(FULL_DATE)
                }
            }
        }

        setupRecyclerView(adapter, linearLayoutManager)

        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var snapPosition = RecyclerView.NO_POSITION

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val snapPosition = snapHelper.getSnapPosition(recyclerView)
                val snapPositionChanged = this.snapPosition != snapPosition
                if (snapPositionChanged) {
                    adapter.setSelectDate(snapHelper.getSnapPosition(recyclerView))
                    this.snapPosition = snapPosition
                }
            }
        })

        viewModel.loadData.observe(this, android.arch.lifecycle.Observer { data ->
            data?.let {
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()

                val centerPosition = it.size / 2
                linearLayoutManager.scrollToPosition(centerPosition)
                snapToCenter(adapter, linearLayoutManager, centerPosition, snapHelper)
            }
        })
    }

    private fun setupRecyclerView(
        adapter: ListAdapter,
        linearLayoutManager: LinearLayoutManager
    ) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        val padding = metrics.xdpi.toInt()
        recyclerView.setPadding(padding, 0, padding, 0)
    }

    private fun snapToCenter(
        adapter: ListAdapter,
        linearLayoutManager: LinearLayoutManager,
        centerPosition: Int,
        snapHelper: LinearSnapHelper,
        selectionByClick: Boolean = false
    ): Boolean {
        return recyclerView.post(Runnable {
            val view = linearLayoutManager.findViewByPosition(centerPosition)
            if (view == null) {
                Log.e("POC", "Cant find target View for final snap")
                return@Runnable
            }

            val snapDistance = snapHelper.calculateDistanceToFinalSnap(linearLayoutManager, view)
            snapDistance?.let {
                if (it[0] != 0 || it[1] != 0) recyclerView.smoothScrollBy(it[0], it[1])
            }

            adapter.setSelectDate(centerPosition, selectionByClick)
        })
    }

    fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
        val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        return layoutManager.getPosition(snapView)
    }
}
