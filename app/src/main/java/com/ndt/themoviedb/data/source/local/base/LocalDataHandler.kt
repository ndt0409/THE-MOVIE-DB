package com.ndt.themoviedb.data.source.local.base

interface LocalDataHandler<P, T> {
    fun execute(params: P): T
}
