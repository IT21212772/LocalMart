package com.example.sellerui.Customer

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.sellerui.FirstPage
import com.example.sellerui.Seller.WelcomePage
import com.example.sellerui.databinding.ActivityCustomerLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CustomerLogin : AppCompatActivity() {

    //view binding
    private lateinit var binding: ActivityCustomerLoginBinding

    //firebase authentication
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private  lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()

        //show while login user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, not have account? go to register page
        binding.textViewSignUp.setOnClickListener {
            startActivity(Intent(this, CustomerRegister::class.java))
        }

        //handle click, begin login
        binding.btnLogin.setOnClickListener{

            validateData()

        }

    }

    private var email = ""
    private var password = ""

    private fun validateData() {

        //Input data
        email = binding.inpUsername.text.toString().trim()
        password = binding.inpPass.text.toString().trim()

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email Pattern", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
        }
        else {
            loginUser()
        }
    }

    private fun loginUser() {

        //login - firebase auth


        //show progress
        progressDialog.setMessage("Login In..")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                checkUser()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun checkUser() {

        //check user Type
        progressDialog.setMessage("Checking User..")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Customers")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    //get user type - user or admin
                    val userType = snapshot.child("userType").value

                    if(userType == "user"){
                        startActivity(Intent(this@CustomerLogin, WelcomePage::class.java))
                        finish()
                    }
                    else if (userType == "admin") {
                        startActivity(Intent(this@CustomerLogin, FirstPage::class.java))
                        finish()
                    }
                }


                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}
