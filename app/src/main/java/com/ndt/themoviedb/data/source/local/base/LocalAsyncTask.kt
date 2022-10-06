package com.ndt.themoviedb.data.source.local.base

import android.content.res.Resources
import android.os.AsyncTask
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import java.io.IOException

class LocalAsyncTask<P, T>(
    private val handler: LocalDataHandler<P, T>,
    private val callback: OnDataLoadedCallback<T>
) : AsyncTask<P, Void, T?>() {

    private var exception: Exception? = null

    override fun doInBackground(vararg params: P): T? {
        return try {
            handler.execute(params[0]) ?: throw IllegalArgumentException("error handler data")
        } catch (e: IOException) {
            e.message
            null
        }
    }

    override fun onPostExecute(result: T?) {
        result?.let {
            callback.onSuccess(result)
        } ?: callback.onError(
            exception ?: Exception(
                Resources.getSystem().getString(R.string.message_null_result)
            )
        )
    }
}
