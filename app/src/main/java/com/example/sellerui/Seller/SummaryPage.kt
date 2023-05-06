package com.example.sellerui.Seller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.sellerui.R
import com.example.sellerui.databinding.ActivitySummaryPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SummaryPage : AppCompatActivity() {

    private lateinit var ivFirebase: ImageView
    private lateinit var databaseReference: DatabaseReference

    private lateinit var btnAdd: Button
    private lateinit var title: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvTitle: TextView

    //view binding
    private lateinit var binding: ActivitySummaryPageBinding

    //firebase authentication
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ivFirebase = findViewById(R.id.disPic)

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(uid)
        databaseReference.get()
            .addOnSuccessListener {
                val url = it.child("url").value.toString()

                Glide.with(this).load(url).into(ivFirebase)
            }

        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        val btn1 = findViewById<ImageView>(R.id.imageView6)
        btn1.setOnClickListener {
            startActivity(Intent(this@SummaryPage, AddDetails::class.java))
        }



    }



}