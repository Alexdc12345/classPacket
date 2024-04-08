package com.example.classpacket.activities


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.classpacket.database.ViewModel
import com.example.classpacket.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var packetViewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        packetViewModel = ViewModelProvider(this)[ViewModel::class.java]
        val sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("login", false).apply()
        if (sharedPref.getBoolean("login",false)){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Please Fill All Fields!", Toast.LENGTH_SHORT).show()
            } else {
                // Start a coroutine on the IO dispatcher to perform database operation
                lifecycleScope.launch {
                    // Switch to the IO dispatcher for database operations
                    val user = withContext(Dispatchers.IO) {
                        packetViewModel.findUser(username, password)
                    }

                    // Check if user is not null before accessing its methods
                    if (user != null) {
                        // User found, continue with login process
                        withContext(Dispatchers.Main) {
                            sharedPref.edit().putString("user", user.username).apply()
                            sharedPref.edit().putString("pass", user.password).apply()
                            sharedPref.edit().putBoolean("login", true).apply()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            Toast.makeText(this@LoginActivity, "Hello ${user.username}!", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // User not found, show error message
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@LoginActivity, "Invalid username or password!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        binding.gotoSignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
}
