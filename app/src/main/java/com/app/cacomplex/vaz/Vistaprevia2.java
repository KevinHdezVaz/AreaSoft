package com.app.cacomplex.vaz;



import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;
import com.tfb.fbtoast.FBToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class Vistaprevia2 extends AppCompatActivity {
    Dialog epicDialog,epicDialog2;
    Map config = new HashMap();
    ImageButton buttonupload2;
    ImageView iv1;
    private Uri imagePath;
    ImageButton button;
LottieAnimationView animacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistaprevia2);
        //galeria

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Sube una imagen");
        animacion=findViewById(R.id.Animacion);
        iv1=(ImageView)findViewById(R.id.iv1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        buttonupload2 = findViewById(R.id.botonupload2);
        buttonupload2.setEnabled(false);

        button= findViewById(R.id.button2);

        button.setOnClickListener(view -> selectImage());
        buttonupload2.setOnClickListener(view -> {


            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                // Si hay conexión a Internet en este momento

                metodoenviarimagenCamara();
                mostrarInfo();

            } else {
                // No hay conexión a Internet en este momento
                FBToast.errorToast(Vistaprevia2.this,"Necesitas conexión a internet para continuar", FBToast.LENGTH_LONG);


            }

        });

        if(isFirstTime()){

            ShowcaseConfig config2 = new ShowcaseConfig();
            config2.setMaskColor(getResources().getColor(R.color.purple_500));
            config2.setRenderOverNavigationBar(true);
            config2.setDelay(500);

            final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(Vistaprevia2.this, "sacatepec22");
            sequence.setConfig(config2);
            button.post(new Runnable() {
                @Override
                public void run() {
                    sequence.addSequenceItem(button,"Para elegir una imagen, toca este icono y elige de la galería.","->CONTINUAR");

                    sequence.addSequenceItem(buttonupload2,"Ya elegida la fotografia, toca este icono para cargar la imagen y posteriormente procesarla. :)","->CONTINUAR");



                    sequence.start();
                }
            });



        }
    }

    public void metodoenviarimagenCamara()
    {

        Log.d("TAG", ": "+" button clicked");
        MediaManager.get().upload(imagePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.d("TAG", "onStart: "+"started");


            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.d("TAG", "onStart: "+"uploading");


            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                Log.d("TAG", "onStart: "+"usuccess");

                FBToast.successToast(Vistaprevia2.this,"Cargando...", FBToast.LENGTH_LONG);
                epicDialog.dismiss();
                Intent intent = new Intent(Vistaprevia2.this, web2.class);
                intent.putExtra("Clave", Objects.requireNonNull(resultData.get("url")).toString());
                startActivity(intent);

            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.d("TAG", "onStart: "+error);
                Toast.makeText(Vistaprevia2.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.d("TAG", "onStart: "+error);
            }
        }).dispatch();



    }
    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");// if you want to you can use pdf/gif/video
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
        buttonupload2.setEnabled(true);

animacion.setVisibility(View.GONE);

    }


    public void mostrarInfo(){

        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.about2);


        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();

        epicDialog.setCancelable(false);

    }

    private Bitmap rotateImageIfRequired(Bitmap image, String path) throws IOException {

        //String path= getRealPathFromUri(imageUri);
        ExifInterface ei= new ExifInterface(path);
        int orientation= ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(image,90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(image,180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(image,270);
            default:
                return image;
        }

    }
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore3", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore3", true);
            editor.commit();
        }
        return !ranBefore;
    }
    private static Bitmap rotateImage(Bitmap img, int degree){
        Matrix matrix= new Matrix();
        matrix.postRotate(degree);
        Bitmap imagenRotada= Bitmap.createBitmap(img,0,0,img.getWidth(),img.getHeight(),matrix,true);
        //img.recycle();
        return imagenRotada;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("¿Estas seguro que quieres salir?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAndRemoveTask();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imagePath=data.getData();
                        Picasso.get().load(imagePath).into(iv1);

                    }
                }
            });
}