package com.app.cacomplex.vaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.cacomplex.vaz.R;


import java.io.File;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ImageView cerrar ;
    Button cerrar2,prueba,prueba2,botonvamo2;
    Dialog epicDialog;
    ImageButton boton1,boton2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    Uri imageUri;
    public static final int GET_FROM_GALLERY = 3;
    private ContentValues values;
    private static final int PICTURE_RESULT = 122 ;
    private Bitmap thumbnail;
    ImageView imagen;
    CardView cardview;
     TextView texto;
    String imageurl;

    private final int PICK_IMAGE = 1;
    public static final int REQUEST_CODE = 1;
    public static final String POINTS = "points";
    Button btnDrawPolygon;
     File photoFile;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        prueba2 = findViewById(R.id.ButtonIMAGENCV);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        texto = findViewById(R.id.texto);
        cardview = findViewById(R.id.carta);

        botonvamo2 = findViewById(R.id.btnDrawPolygon);

        //al dar click boton "como funciona" se ejecutara este metodo, que abre una ventana de ayuda
        botonvamo2.setOnClickListener(v -> {
            mostrarInfo();
        });
        prueba2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(MainActivity.this, VistaPrevia.class);
                startActivity(intent);
            }
        });

    }
    public void mostrarInfo(){

        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.ayuda);
        cerrar2 = (Button) epicDialog.findViewById(R.id.botonvamo);

        cerrar2.setOnClickListener(view -> epicDialog.dismiss());

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }








/*
    private boolean isFirstTime()
    {
|
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;


    }

 */

}