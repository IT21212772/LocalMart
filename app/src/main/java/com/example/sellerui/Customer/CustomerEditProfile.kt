package com.example.sellerui.Customer

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.sellerui.R
import com.example.sellerui.databinding.ActivityCustomerEditProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class CustomerEditProfile : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityCustomerEditProfileBinding

    //firebase authentication
    private lateinit var firebaseAuth: FirebaseAuth

    //image uri
    private var imageUri: Uri?= null

    //progress dialog box
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup progress dialog box
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        //handle back button, go to previous page
        binding.bckArrow.setOnClickListener {
            onBackPressed() //go to previousv screen
        }

        //handle click, pick image from gallery
        binding.profilePic.setOnClickListener{
            showImageAttachMenu()
        }


        //handle click, begin update profile
        binding.btnEditProfile.setOnClickListener {
            validateData()
        }

        binding.profileBn3.setOnClickListener {
            startActivity(Intent(this, CustomerProfile::class.java))
        }

    }

    private var username = ""
    private var email = ""
    private var password = ""
    private var phone = ""
    private var postal = ""

    private fun validateData() {
        //get data
        username = binding.epUserN.text.toString().trim()
        email = binding.epEmail.text.toString().trim()
        password = binding.epPass.text.toString().trim()
        phone = binding.epMobile.text.toString().trim()
        postal = binding.epPostal.text.toString().trim()

        //validate data
        if (username.isEmpty()) {
            //username not entered
            Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show()
        }
        else {
            //username is entered
            if (imageUri == null) {
                //update without image
                updateProfile("")
            }
            else {
                //update with image
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading Profile Picture")
        progressDialog.show()

        val filePathAndName = "ProfileImages2/"+firebaseAuth.uid

        //storage reference
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot->

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadImageUrl = "${uriTask.result}"

                updateProfile(uploadImageUrl)

            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadImageUrl: String) {
        progressDialog.setMessage("Updating Profile..")

        //setup info to db
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["username"] = "$username"
        hashMap["email"] = "$email"
        hashMap["password"] = "$password"
        hashMap["phone"] = "$phone"
        hashMap["postal"] = "$postal"


        if(imageUri != null) {
            hashMap["profileImage2"] = uploadImageUrl
        }

        //update to db
        val reference = FirebaseDatabase.getInstance().getReference("Sellers")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {

                //profile updated
                progressDialog.dismiss()
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
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
                    val username = "${ snapshot.child("username").value }"

                    //setData
                    binding.epUserN.setText(username)
                    binding.usernameTop.setText(username)
                    binding.epPass.setText(password)
                    binding.epMobile.setText(phone)
                    binding.epEmail.setText(email)
                    binding.emailTop.setText(email)
                    binding.epPostal.setText(postal)

                    //set image
                    try {
                        Glide.with(this@CustomerEditProfile)
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

    private fun showImageAttachMenu() {
        //show pop up menu

        //setup popup menu
        val popupMenu = PopupMenu(this, binding.profilePic)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        popupMenu.show()

        //handle popup menu item click
        popupMenu.setOnMenuItemClickListener { item->

            //get id of clicked item
            val id = item.itemId
            if(id == 0){
                //camera click
                pickImageCamera()
            }
            else if(id == 1){
                //Gallery clicked
                pickImageGallery()
            }

            true

        }

    }

    private fun pickImageCamera() {

        //intent to pick image from camera
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }


    private fun pickImageGallery() {

        //intent to pick image from gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)

    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->

            //get uri of image
            if(result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                //imageUri = data!!.data

                //set to imageview
                binding.profilePic.setImageURI(imageUri)
            }
            else {
                //cancelled
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->

            //get uri of image
            if(result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data!!.data

                //set to imageview
                binding.profilePic.setImageURI(imageUri)
            }
            else {
                //cancelled
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )

}
