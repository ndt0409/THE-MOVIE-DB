package com.ndt.themoviedb.data.source.remote.fetchjson

import com.ndt.themoviedb.utils.constant.UrlConstant
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface ParseDataWithJson<T> {


    fun parseToObject(jsonData: String): T


    fun getJsonFromUrl(urlString: String?): T {
        val responseData: T?
        val url = URL(urlString)
        val httpURLConnection: HttpURLConnection =
            (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = UrlConstant.BASE_TIME_OUT
                readTimeout = UrlConstant.BASE_TIME_OUT
                requestMethod = UrlConstant.BASE_METHOD_GET
                doOutput = true
                connect()
            }
        val bufferedReader =
            BufferedReader(InputStreamReader(url.openStream()))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        bufferedReader.close()
        httpURLConnection.disconnect()
        responseData = parseToObject(stringBuilder.toString())
        return responseData
    }
}
