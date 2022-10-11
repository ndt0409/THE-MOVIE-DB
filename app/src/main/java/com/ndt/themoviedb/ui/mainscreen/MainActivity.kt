package com.ndt.themoviedb.ui.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ndt.themoviedb.R
import com.ndt.themoviedb.ui.navigation.ContainerFragment

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

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.main_frame_layout)
        if (fragment is ContainerFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
