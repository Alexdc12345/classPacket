package com.example.classpacket.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.classpacket.R
import com.example.classpacket.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        val binding = FragmentNewsBinding.bind(view)

        recyclerView = binding.newsRecycler
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)


        return binding.root
    }
}