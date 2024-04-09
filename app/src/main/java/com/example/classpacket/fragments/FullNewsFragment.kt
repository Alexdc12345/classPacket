package com.example.classpacket.fragments


import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.classpacket.R
import com.example.classpacket.databinding.FragmentFullNewsBinding

class FullNewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_full_news, container, false)
        val binding = FragmentFullNewsBinding.bind(view)

        val loadingIcon = binding.loadingIcon
        val webView = binding.web

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                loadingIcon.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadingIcon.visibility = View.INVISIBLE
            }
        }


        return binding.root
    }
}