package com.ndt.themoviedb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ndt.themoviedb.R
import com.ndt.themoviedb.ui.home.ContainerFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(
            R.id.main_frame_layout,
            ContainerFragment()
        )
            .commit()
    }
}
