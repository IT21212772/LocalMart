package com.example.sellerui.Seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sellerui.databinding.ActivityNotificationBinding

class Notification : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBtn3.setOnClickListener {
            startActivity(Intent(this, WelcomePage::class.java))
        }
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }
    }
}