package com.ndt.themoviedb.ui.base

import com.ndt.themoviedb.ui.home.HomeFragment

interface BasePresenter<T> {
    fun onStart()
    fun onStop()
    fun setView(view: HomeFragment)
}
