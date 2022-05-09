package com.app.cacomplex.vaz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class camaraGalle extends AppCompatActivity {

    Button camara, galeria;
    Map config = new HashMap();
    final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_galle);
        camara=findViewById(R.id.btnCamara);
        galeria =findViewById(R.id.btnGaleria);

        CardView carta= findViewById(R.id.carta);
            config.put("cloud_name", "hevaz");
            config.put("api_key","716775653773615");
            config.put("api_secret","8DT7Rl0o0Ts3cWE0U1vTDfJq1po");
            config.put("secure", true);
            MediaManager.init(this, config);

        camara.setOnClickListener(view -> startActivity(new Intent(camaraGalle.this,VistaPrevia.class)));
        galeria.setOnClickListener(view -> startActivity(new Intent(camaraGalle.this,Vistaprevia2.class)));

        if(isFirstTime()){

            ShowcaseConfig config2 = new ShowcaseConfig();
            config2.setMaskColor(getResources().getColor(R.color.purple_500));
            config2.setRenderOverNavigationBar(true);
            config2.setDelay(500);

            final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(camaraGalle.this, "quickMathOnBoarding");
            sequence.setConfig(config2);
            camara.post(new Runnable() {
                @Override
                public void run() {
                    sequence.addSequenceItem(carta,"","SIGUIENTE");

                    sequence.addSequenceItem(camara,"Puedes tomar foto desde tu dispositivo movil facilmente.","->CONTINUAR");

                    sequence.addSequenceItem(galeria,"Tambien puedes implementarlas directamente desde galería.","->CONTINUAR");


                    sequence.start();
                }
            });



        }

    }


    public void onRequestPermissionsResult(int requestCode,
                                                String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    } private boolean isFirstTime()
    {
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
}