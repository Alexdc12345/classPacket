package com.example.classpacket.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.classpacket.databinding.ActivityMainBinding
import com.example.classpacket.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.fragmentContainerView)
        val navDrawer = binding.navigationView
        navDrawer.setupWithNavController(navController)
        val navBottom = binding.navbot
        navBottom.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.newsFragment,
            R.id.settingsFragment
        ),binding.drawerlayout)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.aboutFragment, R.id.faqFragment,
                R.id.addFragment, R.id.updateFragment,
                -> {
                    navBottom.visibility = View.GONE
                }
                else -> {
                    navBottom.visibility = View.VISIBLE
                    navBottom.animate().translationY(0F).duration = 300
                }
            }
        }

        navDrawer.menu.findItem(R.id.logoutNav).setOnMenuItemClickListener {
            logout()
            true
        }

        setupActionBarWithNavController(navController,appBarConfiguration)

    }


    private fun logout() {
        val intent = Intent(this,LoginActivity::class.java)
        val sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val userName = sharedPreferences?.getString("user",null).toString()
        sharedPreferences.edit().putBoolean("login",false).apply()
        Toast.makeText(this, "Goodbye ${userName}!", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navmenu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}

