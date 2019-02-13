package com.brx.recyclerview.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
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
        val adapter = ListAdapter(list)
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearLayoutManager

        val snapHelper = LinearSnapHelper()

        snapHelper.attachToRecyclerView(recyclerView)

        viewModel.loadData.observe(this, android.arch.lifecycle.Observer { data ->
            data?.let {
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()

                val centerPosition = it.size / 2
                linearLayoutManager.scrollToPosition(centerPosition)
                snapToCenter(linearLayoutManager, centerPosition, snapHelper)
            }
        })
    }

    private fun snapToCenter(
        linearLayoutManager: LinearLayoutManager,
        centerPosition: Int,
        snapHelper: LinearSnapHelper
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
        })
    }

    private fun customSnap(): LinearSnapHelper {
        return object : LinearSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager?,
                velocityX: Int,
                velocityY: Int
            ): Int {
                val centerView = findSnapView(layoutManager!!) ?: return RecyclerView.NO_POSITION
                val position = layoutManager.getPosition(centerView)
                var targetPosition = -1

                if (layoutManager.canScrollHorizontally()) {
                    targetPosition = if (velocityX < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    targetPosition = if (velocityY < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }

                val firstItem = 0
                val lastItem = layoutManager.itemCount - 1
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem))

                return targetPosition
            }
        }
    }
}
