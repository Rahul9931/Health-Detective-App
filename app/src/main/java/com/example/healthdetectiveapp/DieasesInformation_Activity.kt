package com.example.healthdetectiveapp

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.healthdetectiveapp.databinding.ActivityDieasesInformationBinding
import java.net.URLEncoder

class DieasesInformation_Activity : AppCompatActivity() {
    private val binding:ActivityDieasesInformationBinding by lazy {
        ActivityDieasesInformationBinding.inflate(layoutInflater)
    }
    private lateinit var webview:WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val fileName = "DieasesInfoUrl.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        var dieasesurl = inputString.split("\n")

        val request = intent.getStringExtra("requestCode")
        webview = binding.webview
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = WebViewClient()

        when(request){
            "dieasesInformation" ->{
                val index = intent.getIntExtra("urlIndex",5)
                var url = dieasesurl[index]
                webview.loadUrl("$url")
            }
            "bmiResultInformation" ->{
                var bmiResultUrl = intent.getStringExtra("bmiResultUrl")
                webview.loadUrl("$bmiResultUrl")
            }
            "opendocument" ->{
                val fileurl = intent.getStringExtra("fileurl")
                val filename = intent.getStringExtra("filename")
                val filemime = intent.getStringExtra("mimetype")
                if (filemime!!.startsWith("image")){
                    webview.visibility = View.GONE
                    binding.webimg.visibility = View.VISIBLE
                    Glide.with(this).load(Uri.parse(fileurl)).into(binding.webimg)
                }
                else{
                    binding.webimg.visibility = View.GONE
                    webview.visibility = View.VISIBLE
                    val url = URLEncoder.encode(fileurl,"UTF-8")
                    webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (webview.canGoBack()){
            webview.goBack()
        }
        else{
            super.onBackPressed()
        }

    }
}