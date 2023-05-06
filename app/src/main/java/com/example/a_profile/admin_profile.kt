package com.example.a_profile

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class admin_profile : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)

        val btn1 = findViewById<TextView>(R.id.changepassword)
        btn1.setOnClickListener {
            startActivity(Intent(this@admin_profile, change_password::class.java))
        }

        //customerdetails
        val btn2 = findViewById<TextView>(R.id.customerdetails)
        btn2.setOnClickListener {
            startActivity(Intent(this@admin_profile, Customer_details::class.java))
        }

        //sellerdetails
        val btn3 = findViewById<TextView>(R.id.sellerdetails)
        btn3.setOnClickListener {
            startActivity(Intent(this@admin_profile, Seller_details::class.java))
        }

        //confirmpage
        val btn4 = findViewById<TextView>(R.id.confirmpage)
        btn4.setOnClickListener {
            startActivity(Intent(this@admin_profile, order_confirmation::class.java))
        }

        //notifybtn
        val btn5 = findViewById<ImageView>(
            R.id.notifybtn)
        btn5.setOnClickListener {
            startActivity(Intent(this@admin_profile, notifi::class.java))

        }
    }
}