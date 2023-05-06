package com.example.sellerui.Customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.sellerui.FirstPage
import com.example.sellerui.R
import com.example.sellerui.Seller.WelcomePage
import com.example.sellerui.databinding.ActivityCustomerProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class CustomerProfile : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityCustomerProfileBinding

    //firebase authentication
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        //handle back button, go to previous page
        binding.bckArrow2.setOnClickListener {
            onBackPressed() //go to previous screen
        }

        binding.btnEditProfile22.setOnClickListener {
            startActivity(Intent(this, CustomerEditProfile::class.java))
        }

        binding.deleteBtn22.setOnClickListener {
            val user = Firebase.auth.currentUser
            user?.delete()?.addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(this, "Account Successfully Deleted", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, FirstPage::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    startActivity(Intent(this, WelcomePage::class.java))
                }
            }
        }

    }


    private fun loadUserInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Customers")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    //get user info
                    val email = "${ snapshot.child("email").value }"
                    val password = "${ snapshot.child("password").value }"
                    val phone = "${ snapshot.child("phone").value }"
                    val postal = "${ snapshot.child("postal").value }"
                    val profileImage = "${ snapshot.child("profileImage").value }"
                    val timestamp = "${ snapshot.child("timestamp").value }"
                    val uid = "${ snapshot.child("uid").value }"
                    val userType = "${ snapshot.child("userType").value }"
                    val username = "${ snapshot.child("username").value }"

                    //setData
                    binding.puserN22.text = username
                    binding.usernameTop.text = username
                    binding.pPass22.text = password
                    binding.pMobile22.text = phone
                    binding.pEmail22.text = email
                    binding.emailTop.text = email
                    binding.pPostal22.text = postal

                    //set image
                    try {
                        Glide.with(this@CustomerProfile)
                            .load(profileImage)
                            .placeholder(R.drawable.profile2)
                            .into(binding.profilePic)
                    }
                    catch (e: Exception){

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}