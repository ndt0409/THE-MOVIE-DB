package com.ndt.themoviedb.data.source.remote

interface OnDataLoadedCallback<T> {
    fun onSuccess(data: T?)
    fun onError(e: Exception)
}
