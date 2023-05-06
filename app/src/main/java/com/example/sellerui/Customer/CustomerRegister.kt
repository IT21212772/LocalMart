package com.example.sellerui.Customer

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.sellerui.databinding.ActivityCustomerRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CustomerRegister : AppCompatActivity() {

    //view binding
    private lateinit var binding:ActivityCustomerRegisterBinding

    //firebase authentication
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private  lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        //show while creating account
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, begin register
        binding.btnReg.setOnClickListener {
            validateData()
        }

        binding.already.setOnClickListener {
            startActivity(Intent(this, CustomerLogin::class.java))
        }

    }

    private var username = ""
    private var email = ""
    private var password = ""
    private var phone = ""
    private var postal = ""

    private fun validateData() {

        //input data
        username = binding.inpUsername.text.toString().trim()
        email = binding.InpEmail.text.toString().trim()
        password = binding.inpPass.text.toString().trim()
        phone = binding.inpPhone.text.toString().trim()
        postal = binding.inpPostal.text.toString().trim()
        //val cPassword = binding.cpass.text.toString().trim()

        //validate data
        if(username.isEmpty()) {
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email Pattern", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
        }
        else {
            createUserAccount()
        }
    }

    //create user account
    private fun createUserAccount() {

        //show progress
        progressDialog.setMessage("Creating Account..")
        progressDialog.show()

        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateUserInfo()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed Creating Account due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    //save user info in firebase
    private fun updateUserInfo() {

        progressDialog.setMessage("Saving Your info..")

        val timestamp = System.currentTimeMillis()

        val uid = firebaseAuth.uid

        //setup data to add in db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["username"] = username
        hashMap["email"] = email
        hashMap["password"] = password
        hashMap["phone"] = phone
        hashMap["postal"] = postal
        hashMap["profileImage"] = ""
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        //set data to db
        val ref = FirebaseDatabase.getInstance().getReference("Customers")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {

                //user info saved, redirecting to login page
                progressDialog.dismiss()
                Toast.makeText(this, "Account Created!!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@CustomerRegister, CustomerLogin::class.java))
                finish()
            }
            .addOnFailureListener { e->

                //failed adding to data to db
                progressDialog.dismiss()
                Toast.makeText(this, "Failed Saving User Info due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}
