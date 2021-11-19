package com.gx.app.gappxutils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gx.app.gappxutils.utils.logd
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun queryTimingLog(view: android.view.View) {
        LogQueryTimeActivity.startActivity(this)

    }

    fun queryUser(view: android.view.View) {
        UserActivity.startActivity(this)
    }

    fun queryTimeTask(view: android.view.View) {
        TaskActivity.startActivity(this)
    }

    fun queryfile(view: android.view.View) {
        FileActivity.startActivity(this)

    }


}