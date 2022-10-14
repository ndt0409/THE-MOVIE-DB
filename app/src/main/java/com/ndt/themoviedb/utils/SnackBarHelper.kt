package com.ndt.themoviedb.utils

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.ndt.themoviedb.R

fun FragmentActivity.showSnackBar(msgId: Int) {
    val view = this.findViewById(android.R.id.content) as View
    val snackBar = Snackbar.make(
        view, this.getString(msgId),
        Snackbar.LENGTH_LONG
    )
    val snackBarView = snackBar.view
    snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_sliver))
    val textView =
        snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
    snackBar.show()
}
