package com.example.sellerui.Seller

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.sellerui.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPassword : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityForgetPasswordBinding

    //firebase authentication
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private  lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        //show while login user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle back button, go to previous page
        binding.bckArrow33.setOnClickListener {
            onBackPressed() //go to previousv screen
        }

        //handle click, begin password recovery
        binding.forgetPassSubmitBtn.setOnClickListener{
            validateData()

        }

    }

    private var email = ""

    private fun validateData() {
        //Input data
        email = binding.inpForgetEmail.text.toString().trim()

        //validate data
        if (email.isEmpty()) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
        }
        else{
            recoverPassword()
        }

    }

    private fun recoverPassword() {

        //show progress
        progressDialog.setMessage("Sending Password Reset Instructions to $email")
        progressDialog.show()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Instructions Sent to ${email}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to send due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}