package com.example.windows10.leerimagen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class CalibracionesRealizadasActivity extends AppCompatActivity {
    ArrayList<String> lista=new ArrayList<String>();
    private SharedPreferences sharedPreferences;
    private String micro,objetivo,patron,escala;
    private ArrayList<TextoCalibracionesPrevias> calibracionesPrevias=new ArrayList<TextoCalibracionesPrevias>();
    private ListView listView;
    private AdaptadorCalibraciones adaptadorCalibraciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibraciones_realizadas);
        listView=(ListView)findViewById(R.id.LstOpciones);
        getSupportActionBar().setTitle("Configuraciones previas");

//        try
//        {
//            File ruta_sd = getExternalFilesDir(null);
//            File f = new File(ruta_sd, "configuraciones.txt");
//            BufferedReader fin =
//                    new BufferedReader(
//                            new InputStreamReader(
//                                    new FileInputStream(f)));
//            String linea;
//            String configuraciones="";
//            linea = fin.readLine();
//            configuraciones+=linea + "\n";
//            linea = fin.readLine();
//            configuraciones+=linea;
//            lista.add(configuraciones);
//            configuraciones="";
//            linea=fin.readLine();
//            while(linea != null) {
//                configuraciones+=linea + "\n";
//                linea = fin.readLine();
//                configuraciones+=linea;
//                lista.add(configuraciones);
//                configuraciones="";
//                linea=fin.readLine();
//            }
//
//            fin.close();
//
//        }
//        catch (Exception ex)
//        {
//            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
//        }

//        for(String configuraciones:MedicionesCuantitativas.nombresConfiguraciones){
//            sharedPreferences=getSharedPreferences(configuraciones,MODE_PRIVATE);
//            lista.add(String.valueOf(sharedPreferences.getInt("aumento",-1)) +" "+
//            String.valueOf(sharedPreferences.getFloat("patron",-1)) +" "+
//                    String.valueOf(sharedPreferences.getFloat("escala",-1)));
//
//        }
        try
        {
            File ruta_sd = getExternalFilesDir(null);
            File f = new File(ruta_sd, "configuraciones.txt");
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));
            String linea;
            linea = fin.readLine();
            micro=linea;
            linea = fin.readLine();
            objetivo=linea;
            linea = fin.readLine();
            patron=linea;
            linea = fin.readLine();
            escala=linea;

            if(!(micro == null || objetivo == null || patron == null || escala == null)) {
                calibracionesPrevias.add(new TextoCalibracionesPrevias(micro, objetivo, patron, escala));
                linea = fin.readLine();
                while (linea != null) {
                    micro = linea;
                    linea = fin.readLine();
                    objetivo = linea;
                    linea = fin.readLine();
                    patron = linea;
                    linea = fin.readLine();
                    escala = linea;
                    calibracionesPrevias.add(new TextoCalibracionesPrevias(micro, objetivo, patron, escala));
                    linea = fin.readLine();
                }
            }
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
        }

        adaptadorCalibraciones=new AdaptadorCalibraciones(this,calibracionesPrevias);

        listView.setAdapter(adaptadorCalibraciones);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextoCalibracionesPrevias calibracionCargada=(TextoCalibracionesPrevias)parent.getItemAtPosition(position);

                SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(CalibracionesRealizadasActivity.this);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                editor.putString("microscopio",calibracionCargada.getMicro());
                editor.putString("aumento",calibracionCargada.getObjetivo());
                editor.putString("patron",calibracionCargada.getPatron());
                editor.putString("escala",calibracionCargada.getEscala());
                editor.apply();

                Intent intent=new Intent(CalibracionesRealizadasActivity.this,MedicionesCuantitativas.class);
                startActivity(intent);
                finish();
            }
        });

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ctx_lista,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.borrarLista:
                borrarLista(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void borrarLista(int posicion) {

        int posicionLinea=(posicion)*4;
        int contadorLinea=0;

        calibracionesPrevias.remove(posicion);
        adaptadorCalibraciones.notifyDataSetChanged();
        listView.invalidate();

        try {
            File ruta_sd = getExternalFilesDir(null);
            File f = new File(ruta_sd, "configuraciones.txt");

            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));

            String configuraciones = "";
            String linea=fin.readLine();

            if(contadorLinea==posicionLinea){
                contadorLinea+=4;
                for(int i=0;i<4;i++){
                    linea=fin.readLine();
                }
            }
            if(linea!=null) {
                for (int i = 0; i < 4; i++) {
                    configuraciones += linea + "\n";
                    linea=fin.readLine();
                    contadorLinea++;
                }
            }
            while(linea!=null){
                if(contadorLinea==posicionLinea){
                    contadorLinea+=4;
                    for(int i=0;i<4;i++){
                        linea=fin.readLine();
                    }
                }
                if(linea!=null) {
                    for (int i = 0; i < 4; i++) {
                        configuraciones += linea + "\n";
                        linea=fin.readLine();
                        contadorLinea++;
                    }
                }
            }
            fin.close();
            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            new FileOutputStream(f,false));
            fout.write(configuraciones);
            fout.flush();
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }
    }

    public class AdaptadorCalibraciones extends ArrayAdapter<TextoCalibracionesPrevias> {
//        private TextoCalibracionesPrevias[] datos;

        public
        AdaptadorCalibraciones(Context context, ArrayList<TextoCalibracionesPrevias> datos) {
            super(context, R.layout.list_item_calibraciones_previas, datos);
//            this.datos=datos;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.list_item_calibraciones_previas, null);

            if(calibracionesPrevias.size()>0) {

                TextView micro = (TextView) item.findViewById(R.id.micro);
                micro.append(calibracionesPrevias.get(position).getMicro());

                TextView objetivo = (TextView) item.findViewById(R.id.objetivo);
                objetivo.append(calibracionesPrevias.get(position).getObjetivo());

                TextView patron = (TextView) item.findViewById(R.id.patron);
                patron.append(calibracionesPrevias.get(position).getPatron());

                TextView escala = (TextView) item.findViewById(R.id.escala);
                escala.append(calibracionesPrevias.get(position).getEscala());
            }
            return(item);
        }
    }

}
