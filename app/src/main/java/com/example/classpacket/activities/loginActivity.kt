package com.example.classpacket.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.classpacket.database.ViewModel
import com.example.classpacket.databinding.ActivityLoginBinding
class LoginActivity : AppCompatActivity() {
    private lateinit var packetViewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        packetViewModel = ViewModelProvider(this)[ViewModel::class.java]

        val sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        val username = binding.loginUsername.text
        val password = binding.loginPassword.text

        if (sharedPref.getBoolean("login",false))
            binding.loginBtn.setOnClickListener{
                val user = packetViewModel.findUser(username.toString(), password.toString())
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(this, "Please Fill All Fields!", Toast.LENGTH_SHORT).show()
                } else {
                    sharedPref.edit().putString("user", user.username.toString()).apply()
                    sharedPref.edit().putString("pass", user.password.toString()).apply()
                    sharedPref.edit().putBoolean("login", true).apply()
                    Toast.makeText(this, "Hello ${user.username}!", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
            }
        setContentView(binding.root)
    }
}
