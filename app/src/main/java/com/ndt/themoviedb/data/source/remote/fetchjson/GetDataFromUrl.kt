package com.ndt.themoviedb.data.source.remote.fetchjson

import android.os.AsyncTask
import com.ndt.themoviedb.data.source.remote.OnDataLoadedCallback
import org.json.JSONException
import java.io.IOException

class GetDataFromUrl<T>(
    private val handler: ParseDataWithJson<T>,
    private val listener: OnDataLoadedCallback<T>
) : AsyncTask<String?, Void?, T?>() {

    override fun onPostExecute(result: T?) {
        super.onPostExecute(result)
        try {
            listener.onSuccess(result)
        } catch (e: JSONException) {
            e.message
        }
    }

    override fun doInBackground(vararg params: String?): T? {
        return try {
            handler.getJsonFromUrl(params[0])
        } catch (e: IOException) {
            listener.onError(e)
            null
        }
    }
}
