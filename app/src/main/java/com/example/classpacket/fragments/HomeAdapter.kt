package com.example.classpacket.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.classpacket.R
import com.example.classpacket.database.Phishing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeAdapter(private val lifecycleScope: LifecycleCoroutineScope) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>(){


    private var packetList = emptyList<Phishing>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = packetList[position]
        holder.itemView.findViewById<TextView>(R.id.packetName).text = currentItem.username
        holder.itemView.findViewById<TextView>(R.id.classification).text = "Link: ${currentItem.link}"
        holder.itemView.findViewById<TextView>(R.id.LinkClassification).text = "LinkClassification: ${currentItem.classification}"
        holder.itemView.findViewById<CardView>(R.id.cardRow).setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(packetList[position])
                holder.itemView.findNavController().navigate(action)
            }
        }
    }



    override fun getItemCount() = packetList.size

    fun setData(item: List<Phishing>){
        this.packetList = item
        notifyDataSetChanged()
    }

}


