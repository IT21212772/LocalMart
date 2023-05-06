package com.example.a_profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class change_password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)


        //imageView3
        val btn4 = findViewById<ImageView>(R.id.imageView3)
        btn4.setOnClickListener {
            startActivity(Intent(this@change_password, notifi::class.java))
        }

        //back
        val btn5 = findViewById<Button>(R.id.back)
        btn5.setOnClickListener {
            startActivity(Intent(this@change_password, admin_profile::class.java))
        }

        }
    }
