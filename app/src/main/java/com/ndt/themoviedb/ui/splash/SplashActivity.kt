package com.ndt.themoviedb.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ndt.themoviedb.R
import com.ndt.themoviedb.ui.mainscreen.MainActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer().schedule(DELAY_TIME) {
            this@SplashActivity.runOnUiThread {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
        }
    }

    companion object {
       const val DELAY_TIME = 1500L
    }
}
