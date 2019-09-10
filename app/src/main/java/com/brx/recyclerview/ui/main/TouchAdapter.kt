package com.brx.recyclerview.ui.main

interface TouchAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
}
