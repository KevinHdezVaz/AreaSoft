package com.app.cacomplex.vaz;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivityPrincipal extends AppCompatActivity {
    EditText et1,et2,et3,et4;
    Button b1;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        et3=(EditText)findViewById(R.id.et3);
        et4=(EditText)findViewById(R.id.et4);
        b1=(Button)findViewById(R.id.b1);

        PreferenceManager.setDefaultValues(this, R.xml.preferencias, false);

        SharedPreferences sharedPreferences=getSharedPreferences("datos", Context.MODE_PRIVATE);
        et1.setText(sharedPreferences.getString("nombre",""));
        et2.setText(sharedPreferences.getString("apellido",""));
        et3.setText(sharedPreferences.getString("muestra",""));
        et4.setText(sharedPreferences.getString("area",""));

        if(!(sharedPreferences.getString("nombre","").equals("") &&
                        sharedPreferences.getString("nombre","").equals("") &&
                        sharedPreferences.getString("apellido","").equals("") &&
                        sharedPreferences.getString("muestra","").equals("") &&
                        sharedPreferences.getString("area","").equals("")) && !VistaPrevia.AgregarMuestra){
            lanzarVistaPrevia();
        }

        b1.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void lanzarVistaPrevia() {
        Intent intent=new Intent(this,VistaPrevia.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void ok (View v){

        String texto1=et1.getText().toString();
        String texto2=et2.getText().toString();
        String texto3=et3.getText().toString();
        String texto4=et4.getText().toString();

        if(texto1.equals("") || texto2.equals("") ||  texto3.equals("") || texto4.equals("") ) {
            b1.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"Debes completar todos los datos...",
                    Toast.LENGTH_SHORT).show();
        }
        else
            b1.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void siguiente(View v){

        String texto1=et1.getText().toString();
        String texto2=et2.getText().toString();
        String texto3=et3.getText().toString();
        String texto4=et4.getText().toString();

        if(texto1.equals("") || texto2.equals("") ||  texto3.equals("") ||  texto4.equals("")) {
            b1.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"Debes completar todos los datos...",
                    Toast.LENGTH_SHORT).show();
        }
        else {

            b1.setVisibility(View.VISIBLE);
            SharedPreferences sharedPreferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("nombre", et1.getText().toString());
            editor.putString("apellido", et2.getText().toString());
            editor.putString("muestra", et3.getText().toString());
            editor.putString("area", et4.getText().toString());
            editor.commit();
            VistaPrevia.AgregarMuestra=false;
            lanzarVistaPrevia();
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.salir:
                final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivityPrincipal.this);
                builder.setTitle("¿Estas seguro que quieres salir?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                builder.show();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
