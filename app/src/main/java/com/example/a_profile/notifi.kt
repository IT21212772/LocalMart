package com.example.a_profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class notifi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifi)

        //imageView13
        val btn5 = findViewById<ImageView>(R.id.imageView13)
        btn5.setOnClickListener {
            startActivity(Intent(this@notifi, admin_profile::class.java))
        }
    }
}