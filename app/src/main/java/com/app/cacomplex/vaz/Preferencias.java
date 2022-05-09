package com.app.cacomplex.vaz;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Windows 10 on 10/05/2017.
 */

public class Preferencias extends AppCompatActivity{

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preferencias);
        getSupportActionBar().setTitle("Configuración");
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new PreferenciasFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
