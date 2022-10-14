package com.ndt.themoviedb.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ndt.themoviedb.data.model.Favorite
import com.ndt.themoviedb.utils.constant.UrlConstant

class DatabaseHelper(val context: Context) : SQLiteOpenHelper(
    context,
    UrlConstant.BASE_DATABASE_NAME,
    null,
    UrlConstant.BASE_DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(Favorite.MovieEntry.SQL_FAVORITE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        context.deleteDatabase(UrlConstant.BASE_DATABASE_NAME);
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: DatabaseHelper? = null
        fun getInstance(context: Context): DatabaseHelper =
            instance ?: DatabaseHelper(context).also { instance = it }
    }
}
