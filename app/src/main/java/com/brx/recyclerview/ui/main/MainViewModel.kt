package com.brx.recyclerview.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.util.*


class MainViewModel : ViewModel() {

    var loadData: MutableLiveData<List<Date>> = MutableLiveData()

    init {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, 2019)
        cal.set(Calendar.MONTH, Calendar.JANUARY)
        cal.set(Calendar.DAY_OF_MONTH, 1)

        val list = mutableListOf<Date>()
        for (i in 0..20) {
            list.add(cal.time.addDays(i))
        }

        loadData.value = list
    }
}
