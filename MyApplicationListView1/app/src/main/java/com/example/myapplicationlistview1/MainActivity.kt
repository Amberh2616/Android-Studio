package com.example.myapplicationlistview1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        go2()
    }

    fun go2() {
        var intent = Intent(this, ListActivity::class.java)
        startActivity(intent)

    }
}