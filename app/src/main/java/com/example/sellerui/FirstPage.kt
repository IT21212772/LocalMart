package com.example.sellerui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.sellerui.Admin.AdminReg
import com.example.sellerui.Customer.CustomerLogin
import com.example.sellerui.Seller.LoginActivity

class FirstPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        val btn1 = findViewById<TextView>(R.id.btnLogSeller)
        btn1.setOnClickListener {
            startActivity(Intent(this@FirstPage, LoginActivity::class.java))
        }

        val btn2 = findViewById<TextView>(R.id.btnLogBuyer)
        btn2.setOnClickListener {
            startActivity(Intent(this@FirstPage, CustomerLogin::class.java))
        }

        val btn3 = findViewById<TextView>(R.id.adminBtn)
        btn3.setOnClickListener {
            startActivity(Intent(this@FirstPage, AdminReg::class.java))
        }
    }
}