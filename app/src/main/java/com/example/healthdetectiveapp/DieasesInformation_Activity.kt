package com.example.healthdetectiveapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.healthdetectiveapp.databinding.ActivityDieasesInformationBinding

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

        val index = intent.getIntExtra("urlIndex",5)
        webview = binding.webview
        var url = dieasesurl[index]
        webview.loadUrl("$url")
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = WebViewClient()

        /*when(request){
            "dieasesInformation" ->{
                val index = intent.getIntExtra("urlIndex",5)
                webview = binding.webview
                var url = dieasesurl[index]
                webview.loadUrl("$url")
                webview.settings.javaScriptEnabled = true
                webview.webViewClient = WebViewClient()
            }
            "bmiResultInformation" ->{
                var bmiResultUrl = intent.getStringExtra("bmiResultUrl")
                webview = binding.webview
                webview.loadUrl("$bmiResultUrl")
                webview.settings.javaScriptEnabled = true
                webview.webViewClient = WebViewClient()
            }
        }*/
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