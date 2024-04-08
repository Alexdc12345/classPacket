package com.example.classpacket.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.classpacket.database.ViewModel
import com.example.classpacket.database.User
import com.example.classpacket.databinding.ActivitySignupBinding

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

                if (pass == confirmPass){
                    val user = packetViewModel.findUser(username, pass)

                    if (user != null){
                        Toast.makeText(this, "User Already Exist!", Toast.LENGTH_SHORT).show()
                    } else {
                        val use = User()
                        use.fullName = fullname
                        use.email = email
                        use.username = username
                        use.password = pass
                        packetViewModel.addUser(use)
                        Toast.makeText(this, "Signup Successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } else {
                    Toast.makeText(this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please Fill All Fields!", Toast.LENGTH_SHORT).show()
            }}
        binding.returnLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.gotoLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}