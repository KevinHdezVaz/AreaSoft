package com.example.windows10.leerimagen;


import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class vistaPick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_pick);


        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://www.geogebra.org/material/iframe/id/2626471/width/775/height/734/border/888888/rc/false/ai/false/sdz/false/smb/false/stb/true/stbh/true/ld/false/sri/true/at/auto");



         WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setBuiltInZoomControls(true);
    }
}