package com.ndt.themoviedb.ui.mainscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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
            showAppExitDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun showAppExitDialog() = with(AlertDialog.Builder(this)) {
        setIcon(R.drawable.ic_exit)
        setTitle(getString(R.string.title_exit_app))
        setMessage(getString(R.string.notification_exit_app))

        setPositiveButton(context.getString(R.string.title_yes)) { _, _ ->
            finish()
        }
        setNegativeButton(context.getString(R.string.title_no)) { _, _ ->
        }
        show()
    }
}
