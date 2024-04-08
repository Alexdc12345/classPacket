package com.example.classpacket.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.classpacket.database.ViewModel
import com.example.classpacket.database.User
import com.example.classpacket.databinding.ActivitySignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var packetViewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        packetViewModel = ViewModelProvider(this)[ViewModel::class.java]

        binding.signupBtn.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val pass = binding.signupPassword.text.toString()
            val confirmPass = binding.signupPasswordConf.text.toString()
            val fullname = binding.signupFullname.text.toString()
            val username = binding.signupUsername.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && username.isNotEmpty() &&
                confirmPass.isNotEmpty() && fullname.isNotEmpty()) {

                if (pass == confirmPass) {
                    // Start a coroutine to perform database operation
                    lifecycleScope.launch {
                        // Switch to the IO dispatcher for database operations
                        withContext(Dispatchers.IO) {
                            val user = packetViewModel.findUser(username, pass)

                            // Check if user is not null before accessing its methods
                            if (user != null) {
                                // User already exists, show error message
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@SignupActivity, "User Already Exist!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // User does not exist, add user to database
                                val newUser = User().apply {
                                    fullName = fullname
                                    this.email = email
                                    this.username = username
                                    this.password = pass
                                }
                                packetViewModel.addUser(newUser)

                                // Display signup successful message
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@SignupActivity, "Signup Successful!", Toast.LENGTH_SHORT).show()

                                    // Navigate to login activity
                                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Fill All Fields!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.returnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.gotoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
