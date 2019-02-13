package com.brx.recyclerview.ui.main

import java.text.SimpleDateFormat
import java.util.*

const val FULL_DATE = "dd/MM/yyyy"
const val SIMPLE_DATE = "dd/MM"

fun Date.toString(pattern : String = FULL_DATE): String{
    val sdf= SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}

fun Date.add(field: Int, amount: Int): Date{
    val cal = Calendar.getInstance()
    cal.time=this
    cal.add(field, amount)

    this.time = cal.time.time

    cal.clear()

    return this
}

fun Date.addDays(days: Int): Date{
    return add(Calendar.DAY_OF_MONTH, days)
}