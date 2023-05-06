package com.example.sellerui.Seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sellerui.FirstPage
import com.example.sellerui.databinding.ActivityWelcomePageBinding
import com.google.firebase.auth.FirebaseAuth

class WelcomePage : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityWelcomePageBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, FirstPage::class.java))
            finish()
        }

        //handle click, open profile
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfilePage::class.java))
        }

        binding.homeBtn.setOnClickListener {
            startActivity(Intent(this, WelcomePage::class.java))
        }
        binding.notifBtn3.setOnClickListener {
            startActivity(Intent(this, Notification::class.java))
        }

        binding.btnPost.setOnClickListener {
            startActivity(Intent(this, AddPhoto::class.java))
        }

    }

    private fun checkUser() {

        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){

            binding.postRules.text = "Not logged In"
        }
        else{
            //logged in, get and show user info
            val email = firebaseUser.email

            //set to textview of toolbar
            binding.postRules.text = email
        }

    }


}