package com.ndt.themoviedb.ui.base

interface BasePresenter<T> {
    fun onStart()
    fun onStop()
    fun setView(view: T)
}
