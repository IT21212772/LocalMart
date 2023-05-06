package com.example.sellerui.Seller

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.sellerui.R
import com.example.sellerui.databinding.ActivityAddDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddDetails : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityAddDetailsBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private  lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        //show while login user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        /*//handle back button, go to previous page
        binding.bckArrow2.setOnClickListener {
            onBackPressed() //go to previousv screen
        }*/

        //handle click, begin uploading product location
        binding.btnDetail.setOnClickListener{
            validateData()

        }

        val btn3 = findViewById<ImageView>(R.id.imageView5)
        btn3.setOnClickListener {
            startActivity(Intent(this@AddDetails, AddPhoto::class.java))
        }

    }

    private var title = ""
    private var description = ""
    private var price = ""
    private var contact = ""

    private fun validateData() {
        //validate data

        //get data
        title = binding.inpTitle.text.toString().trim()
        description = binding.inpDescription.text.toString().trim()
        price = binding.inpPrice.text.toString().trim()
        contact = binding.inpContact.text.toString().trim()

        //validate data
        if (title.isEmpty()) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show()
        }
        else {
            addLocationFirebase()
        }
    }

    private fun addLocationFirebase() {
        //show progress
        progressDialog.setMessage("Adding Details..")
        progressDialog.show()

        //get timestamp
        val timestamp = System.currentTimeMillis()

        //setup data to add in firebase
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["title"] = title
        hashMap["description"] = description
        hashMap["price"] = price
        hashMap["contact"] = contact
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "$firebaseAuth.uid"

        //add to firebase db
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                //added successfully
                progressDialog.dismiss()
                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddDetails, SummaryPage::class.java))
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Add Product", Toast.LENGTH_SHORT).show()
            }
    }
}
