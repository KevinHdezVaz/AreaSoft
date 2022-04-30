package com.example.windows10.leerimagen;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class web2 extends AppCompatActivity {
    private ValueCallback mUM;
    private ValueCallback<Uri[]> mUMA;
    private String mCM;
    Dialog epicDialog,epicDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

 

        Intent intent = getIntent();
        String valor = intent.getStringExtra("Clave");


        final Activity activity = this;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        WebView webView = (WebView) findViewById(R.id.webView);





        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressDialog.show();
                progressDialog.setProgress(0);

                activity.setProgress(progress * 1000);
                progressDialog.incrementProgressBy(progress);

                if(progress == 100 && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

        mostrarInfo();
        webView.loadUrl("https://ij.imjoy.io/?open="+valor);
        webView.getSettings().setJavaScriptEnabled(true);


    }  public void mostrarInfo(){

        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.about2);


        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();

        epicDialog.setCancelable(false);

    }
}

