package com.example.classpacket.fragments


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classpacket.R
import com.example.classpacket.database.ViewModel
import com.example.classpacket.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var packetViewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val binding = FragmentHomeBinding.bind(view)

        binding.addPacketBtn.setOnClickListener {
            findNavController().navigate(R.id.addFragment)
        }

        val sharedPreferences = context?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        //recycler
        val homeAdapter = HomeAdapter(viewLifecycleOwner.lifecycleScope)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = homeAdapter
        recyclerView.layoutManager = GridLayoutManager(context,1)
        binding.addPacketBtn.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        packetViewModel = ViewModelProvider(this)[ViewModel::class.java]
        packetViewModel.readAllDataUser(sharedPreferences?.getString("user",null).toString()).observe(viewLifecycleOwner,
            Observer{ packet ->
                homeAdapter.setData(packet)
                if (homeAdapter.itemCount != 0){
                    binding.noItemsTxt.visibility = View.GONE
                }
                else{
                    binding.noItemsTxt.visibility = View.VISIBLE
                }
            })

        binding.delAllBtn.setOnClickListener {
            deleteAll()
        }



        return binding.root

    }
    private fun deleteAll() {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes"){_,_ ->
            val sharedPreferences = context?.getSharedPreferences("sharedPref",Context.MODE_PRIVATE)
            val user = sharedPreferences?.getString("user",null).toString()
            packetViewModel.deleteAll(user)
            Toast.makeText(context,"All Packets Deleted!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.setTitle("Delete All Packets")
        builder.setMessage("Do you want to delete all packets?")
        builder.create().show()
    }
}