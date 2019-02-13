package com.brx.recyclerview.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel() {

    var loadData: MutableLiveData<List<Date>> = MutableLiveData()

    init {
        val list = mutableListOf<Date>()
        for (i in 1..10) {
            list.add(Date().addDays(i))
        }

        loadData.value = list
    }
}
