package com.example.sellerui.Seller

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sellerui.R
import com.example.sellerui.databinding.ActivityAddPhotoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class AddPhoto : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var btnBrowse: Button
    private lateinit var btnUpload: Button

    private var storageRef = Firebase.storage

    private lateinit var uri: Uri

    //view binding
    private lateinit var binding: ActivityAddPhotoBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private  lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        storageRef = FirebaseStorage.getInstance()


        //upload image begin
        image = findViewById(R.id.addimageProd)
        btnBrowse = findViewById(R.id.browsBtn)
        btnUpload = findViewById(R.id.btnPhoto2)

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                image.setImageURI(it)
                if (it != null) {
                    uri = it
                }
            }
        )
        btnBrowse.setOnClickListener {
            galleryImage.launch("image/*")
        }

        btnUpload.setOnClickListener {

            storageRef.getReference("images").child(System.currentTimeMillis().toString())
                .putFile(uri)
                .addOnSuccessListener { task->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            val uid = FirebaseAuth.getInstance().currentUser!!.uid

                            val mapImage = mapOf(
                                "url" to it.toString()
                            )

                            val databaseReference = FirebaseDatabase.getInstance().getReference("Products")
                            databaseReference.child(uid).setValue(mapImage)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {error->
                                    Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        }



        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        //show while login user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, begin uploading product location
        binding.locationBtn.setOnClickListener{
            validateData()

        }

        val btn3 = findViewById<ImageView>(R.id.imageView9)
        btn3.setOnClickListener {
            startActivity(Intent(this@AddPhoto, WelcomePage::class.java))
        }

    }

    private var location = ""

    private fun validateData() {
        //validate data

        //get data
        location = binding.inpLocation.text.toString().trim()

        //validate data
        if (location.isEmpty()) {
            Toast.makeText(this, "Enter Location", Toast.LENGTH_SHORT).show()
        }
        else {
            addLocationFirebase()
        }
    }

    private fun addLocationFirebase() {
        //show progress
        progressDialog.setMessage("Adding location..")
        progressDialog.show()

        //get timestamp
        val timestamp = System.currentTimeMillis()

        //setup data to add in firebase
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id"] = "$timestamp"
        hashMap["location"] = location
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "$firebaseAuth.uid"

        //hashMap["profileImage"] = ""

        //add to firebase db
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                //added successfully
                progressDialog.dismiss()
                Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddPhoto, AddDetails::class.java))
                finish()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Add Product", Toast.LENGTH_SHORT).show()
            }


    }
}
