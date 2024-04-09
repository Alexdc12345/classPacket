package com.example.classpacket.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.classpacket.R
import com.example.classpacket.activities.LoginActivity
import com.example.classpacket.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        val binding = FragmentSettingsBinding.bind(view)

        binding.logout.setOnClickListener {
            logout()
        }

        binding.faq.setOnClickListener {
            findNavController().navigate(R.id.faqFragment)
        }

        binding.about.setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }

        return binding.root
    }
    private fun logout(){
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
        val sharedPreferences = context?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val userName = sharedPreferences?.getString("user",null).toString()
        Toast.makeText(context, "Goodbye ${userName}!", Toast.LENGTH_SHORT).show()
        sharedPreferences?.edit()?.putBoolean("login",false)?.apply()
    }
}