package com.ndt.themoviedb.ui.utils.extension

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ndt.themoviedb.R
import com.ndt.themoviedb.data.model.Movie
import com.ndt.themoviedb.ui.details.MovieDetailsFragment
import com.ndt.themoviedb.ui.listmovie.ListMovieFragment
import com.ndt.themoviedb.ui.utils.NetworkUtil

fun Fragment.addFragment(movie: Movie) {
    activity?.let {
        val fragment =
            MovieDetailsFragment.getInstance(movie.id, movie.title)
        if (NetworkUtil.isConnectedToNetwork(it)) {
            it.supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            val message = getString(R.string.check_internet_fail)
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}

fun Fragment.addFragment(type: String, query: String, title: String) {
    activity?.let {
        val fragment = ListMovieFragment.getInstance(type, query, title)
        if (NetworkUtil.isConnectedToNetwork(it)) {
            it.supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_frame_layout, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            val message = getString(R.string.check_internet_fail)
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }
}
