package com.example.windows10.leerimagen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Filtros extends AppCompatActivity {

    Bitmap imagenFiltrada;

    int[] pixels;
    int progreso=50;

    ImageView iv1;
    LinearLayout linearLayout1;
    SeekBar seekbar;
    EditText editText;
    Button buttonOK;


    boolean brillo=false;
    private Mat imagenGris;
    private Mat matrizImagen;
    public  Mat matProcesada;
    private boolean seekBarDetenido=true;
    private HiloContraste hiloContraste;
    private Bitmap imagen;
    private HiloSaturacion hiloSaturacion;
    private boolean saturacionFlag=false;
    private HiloBrillo hiloBrillo;
    private boolean contrasteFlag=false;
    private Bitmap imagenFiltrada2;
    private Mat matrizImagen2;
    private Mat matProcesada2;
    private boolean invertido=false;
    private boolean escaladoEnGris=false;
    private HiloBinarizacion hiloBinarizacion;
    private boolean binarizacionFlag=false;
    private TextView textoLimite;
    private boolean binarizado=false;

    private ArrayList<Integer> orden=new ArrayList<Integer>();
    private static ArrayList<Integer> ordenS=new ArrayList<Integer>();

    private HiloNitidez hiloNitidez;
    private HiloTempCol hiloTempCol;
    private boolean nitidezFlag=false;
    private boolean tempColFlag=false;

    private Vector<Float> constanteBrillo=new Vector<Float>();
    private Vector<Float> constanteBinarizacion=new Vector<Float>();
    private Vector<Float> constanteSaturacion=new Vector<Float>();
    private Vector<Float> constanteContraste=new Vector<Float>();
    private Vector<Float> constanteNitidez=new Vector<Float>();
    private Vector<Float> constanteTempCol=new Vector<Float>();

    private static Vector<Float> constanteBrilloS=new Vector<Float>();
    private static Vector<Float> constanteBinarizacionS=new Vector<Float>();
    private static Vector<Float> constanteSaturacionS=new Vector<Float>();
    private static Vector<Float> constanteContrasteS=new Vector<Float>();
    private static Vector<Float> constanteNitidezS=new Vector<Float>();
    private static Vector<Float> constanteTempColS=new Vector<Float>();

    private int repeticionBrillo=-1;
    private int repeticionBinarizado=-1;
    private int repeticionSaturacion=-1;
    private int repeticionContraste=-1;
    private int repeticionNitidez=-1;
    private int repeticionTempCol=-1;

    private static int repeticionBrilloS=-1;
    private static int repeticionBinarizadoS=-1;
    private static int repeticionSaturacionS=-1;
    private static int repeticionContrasteS=-1;
    private static int repeticionNitidezS=-1;
    private static int repeticionTempColS=-1;

    private boolean pausa=false;

    public static final String TAG="MainActivityPrincipal";

    static{
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"Cargo exitosamente :)");
        }else
            Log.d(TAG,"No pudo cargar el paquete openCV :(");
    }

    private static boolean guardado=false;
    private Vector<Mat> canales=new Vector<Mat>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);
        setTitle("Métodos y filtros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv1=(ImageView)findViewById(R.id.iv1);
        linearLayout1=(LinearLayout)findViewById(R.id.linearLayout1);


//        iv1.setImageBitmap(VistaPrevia.imagen);

        if(VistaPrevia.currentFileName!=null) {
            BitmapFactory.Options options=new BitmapFactory.Options();
            BitmapFactory.decodeFile(VistaPrevia.currentFileName,options);

            options.inJustDecodeBounds=true;
            options.inSampleSize=(int)Math.max(Math.ceil(options.outWidth/600),Math.ceil(options.outHeight/600));
            options.inJustDecodeBounds=false;

            imagen = BitmapFactory.decodeFile(VistaPrevia.currentFileName,options);
            iv1.setImageBitmap(imagen);

            imagenFiltrada=Bitmap.createBitmap(imagen.getWidth(),
                    imagen.getHeight(), Bitmap.Config.RGB_565);
            matrizImagen=new Mat(imagen.getHeight(),imagen.getWidth(),CvType.CV_8UC3);
            Utils.bitmapToMat(imagen,matrizImagen);
            matProcesada=matrizImagen.clone();
        }

        seekbar=new SeekBar(this);
        seekbar.setProgress(50);
        seekbar.setMax(256);
        LinearLayout.LayoutParams layoutParamsSeekbar=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,0.13f);
        layoutParamsSeekbar.setMargins(0,(int)(10*getResources().getDisplayMetrics().density),0,0);
        seekbar.setLayoutParams(layoutParamsSeekbar);
        seekbar.setVisibility(View.INVISIBLE);


        textoLimite=new TextView(this);
        LinearLayout.LayoutParams layoutParamsLimite=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,0.02f);
        layoutParamsLimite.gravity=Gravity.CENTER;
        layoutParamsLimite.setMargins(0,(int)(10*getResources().getDisplayMetrics().density),0,0);
        textoLimite.setLayoutParams(layoutParamsLimite);
        textoLimite.setVisibility(View.INVISIBLE);

        linearLayout1.addView(textoLimite);
        linearLayout1.addView(seekbar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progreso = progress;
//                seekBar.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarDetenido=false;
                pausa=false;
                if(hiloBrillo!=null && brillo)
                    if(hiloBrillo.getState()== Thread.State.NEW)
                        hiloBrillo.start();
                    else
                        hiloBrillo.reanudar();
                else if(hiloSaturacion!=null && saturacionFlag)
                    if(hiloSaturacion.getState()== Thread.State.NEW)
                        hiloSaturacion.start();
                    else
                        hiloSaturacion.reanudar();
                else if(hiloContraste!=null && contrasteFlag)
                    if(hiloContraste.getState()== Thread.State.NEW)
                        hiloContraste.start();
                    else
                        hiloContraste.reanudar();
                else if(hiloBinarizacion!=null && binarizacionFlag)
                    if(hiloBinarizacion.getState()== Thread.State.NEW)
                        hiloBinarizacion.start();
                    else
                        hiloBinarizacion.reanudar();
                else if(nitidezFlag){
                        if(hiloNitidez.getState()== Thread.State.NEW)
                            hiloNitidez.start();
                        else
                            hiloNitidez.reanudar();
                }
                else if(tempColFlag){
                    if(hiloTempCol.getState()== Thread.State.NEW)
                        hiloTempCol.start();
                    else
                        hiloTempCol.reanudar();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                linearLayout1.removeView(seekBar);
                pausa=true;
                seekBarDetenido=true;


//                if(brillo){
//                    setBrillo();
//                }
            }
        });

//        editText=new EditText(this);
//        editText.setTextColor(Color.WHITE);
//        editText.setHintTextColor(
//                Color.WHITE);
//        editText.setText("125");
//        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//        editText.setHint("Ingrese valor de binarización: ");
//        editText.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//
//        buttonOK=new Button(this);
//        buttonOK.setText("OK");
//        buttonOK.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
//        buttonOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linearLayout1.removeView(editText);
//                linearLayout1.removeView(buttonOK);
//                binarizacion();
//            }
//        });
    }

    private void escalaGrices() {

        if(VistaPrevia.imagen!=null && !escaladoEnGris) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Imgproc.cvtColor(matrizImagen,matProcesada,Imgproc.COLOR_BGR2GRAY);

                    Utils.matToBitmap(matProcesada,imagenFiltrada);



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            iv1.setImageBitmap(imagenFiltrada);
//                            bitmap.recycle();

                        }
                    });
                    escaladoEnGris=true;
                }
            }).start();
///////////////////////////////Método Java/////////////////////////////////////////////////////
//            int w = imagen.getWidth();
//            int h = imagen.getHeight();
//
//            pixels=new int[w*h];
//            /*
//            r = new int[w][h];
//            g = new int[w][h];
//            b = new int[w][h];
//            a = new int[w][h];
//            gr = new int[w][h];
//            */
//
//            imagenFiltrada = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//
//            Color c = new Color();
//
//            /*
//            int[] colors=new int[w*h];
//            int k=0;
//            for (int i = 0; i < w; i++) {
//                for (int j = 0; j < h; j++) {
//                    imagen.getPixels(colors,0,w,0,0,w,h);
//                }
//            }
//            k=0;
//            imagenFiltrada = Bitmap.createBitmap(colors,w, h, Bitmap.Config.ARGB_8888);
//            imagen=imagenFiltrada.copy(Bitmap.Config.ARGB_8888,true);
//            */
//            /*
//            int k=0;
//            for (int i = 0; i < w; i++) {
//                for (int j = 0; j < h; j++) {
//                    */
//                    /*
//                    r[i][j] = c.red(imagen.getPixel(i, j));
//                    g[i][j] = c.green(imagen.getPixel(i, j));
//                    b[i][j] = c.blue(imagen.getPixel(i, j));
//                    a[i][j] = c.alpha(imagen.getPixel(i, j));
//
//
//
//                    gr[i][j] = (r[i][j] + g[i][j] + b[i][j]) / 3;
//                    imagenFiltrada.setPixel(i, j, c.argb(255, gr[i][j], gr[i][j], gr[i][j]));
//                    */
//
//                    /*
//                    r = Color.red(imagen.getPixel(i, j));
//                    g = Color.green(imagen.getPixel(i, j));
//                    b = Color.blue(imagen.getPixel(i, j));
//                    a = Color.alpha(imagen.getPixel(i, j));
//
//                    gr = (r + g + b) / 3;
//
//                    pixels[k]=Color.argb(255,gr,gr,gr);
//                    k++;
//
//                    imagenFiltrada.setPixel(i, j, Color.argb(255, gr, gr, gr));
//
//                }//end for
//            }//end for
//            */
//
//            imagen.getPixels(pixels,0,w,0,0,w,h);
//
//            int r,g,b,a,gray;
//
//            for(int i=0; i<pixels.length; i++){
//                r= Color.red(pixels[i]);
//                g= Color.green(pixels[i]);
//                b= Color.blue(pixels[i]);
//                a= Color.alpha(pixels[i]);
//                gray=(r+g+b)/3;
//                pixels[i]=Color.argb(a,gray,gray,gray);
//            }
//
//            imagenFiltrada.setPixels(pixels,0,w,0,0,w,h);
//            //imagen = imagenFiltrada;
//            iv1.setImageBitmap(imagenFiltrada);

        }else{
            Toast.makeText(this,"¡¡No existe la imagen previa o ya se ha aplicado el procesado!!",Toast.LENGTH_SHORT).show();
        }

    }

    private void binarizacion() {
        hiloBinarizacion=new HiloBinarizacion();
//
//            int w = VistaPrevia.imagen.getWidth();
//            int h = VistaPrevia.imagen.getHeight();
//
//            pixels=new int[w*h];
//
//            VistaPrevia.imagen.getPixels(pixels,0,w,0,0,w,h);
//
////            imagenFiltrada = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//
//            linearLayout1.addView(editText);
//            linearLayout1.addView(buttonOK);
//
//            int limite = Integer.parseInt(editText.getText().toString());
//            int r,g,b,a,gray;
//
//            for(int i=0; i<pixels.length; i++){
//                r= Color.red(pixels[i]);
//                g= Color.green(pixels[i]);
//                b= Color.blue(pixels[i]);
//                a= Color.alpha(pixels[i]);
//                gray=(r+g+b)/3;
//                if(gray >= limite) {
//                    gray=255;
//                    pixels[i] = Color.argb(a, gray, gray, gray);
//                }else{
//                    gray=0;
//                    pixels[i] = Color.argb(a, gray, gray, gray);
//                }
//            }
//
//            imagenFiltrada.setPixels(pixels,0,w,0,0,w,h);
//            //imagen = imagenFiltrada;
//            iv1.setImageBitmap(imagenFiltrada);
//
//
    }

    private class HiloBinarizacion extends Thread{
        public synchronized void reanudar(){
            pausa=false;
            notify();
        }
        @Override
        public void run() {
            super.run();

            Log.d("HiloBrillo",String.valueOf(seekBarDetenido));

            while(!seekBarDetenido) {
               procesoBinarizacion();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageBitmap(imagenFiltrada);
                        textoLimite.setText(String.valueOf(progreso));
                    }
                });
                constanteBinarizacion.set(repeticionBinarizado,(float)progreso);
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void procesoBinarizacion() {
        Imgproc.threshold(matrizImagen,matProcesada,(float)progreso,255,Imgproc.THRESH_BINARY);
        Utils.matToBitmap(matProcesada,imagenFiltrada);
        binarizado=true;

    }

    private void contraste() {
//        linearLayout1.addView(seekbar);
//
//        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        pDialog.setMessage("Procesando...");
//        pDialog.setCancelable(true);
//        pDialog.setCanceledOnTouchOutside(false);


//                        pDialog.show();
            hiloContraste=new HiloContraste();

//            hiloBrillo.start();

//                for(int i=0;i<matrizImagen.width();i++)
//                    for(int j=0;j<matrizImagen.height();j++)
//                File ubicacionFile=new File(getCacheDir().getAbsolutePath()+"/oscurecido.jpg");
//
//                Imgcodecs.imwrite(ubicacionFile.toString(),mat);
//                //                Bitmap bitmap=BitmapFactory.decodeFile(getCacheDir().getAbsolutePath()+"/oscurecido.jpg");
//                Drawable drawable= Drawable.createFromPath(ubicacionFile.toString());

//                            bitmap.recycle();


///////////////////////Método Java////////////////////////////////////
//        int w = imagen.getWidth();
//        int h = imagen.getHeight();
//
//        pixels=new int[w*h];
//
//        imagen.getPixels(pixels,0,w,0,0,w,h);
//
//        imagenFiltrada = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//
//
//        int r,g,b,a;
//
//        for(int i=0; i<pixels.length; i++){
//            r= Color.red(pixels[i]);
//            g= Color.green(pixels[i]);
//            b= Color.blue(pixels[i]);
//            a= Color.alpha(pixels[i]);
//
//            r=r - progreso;
//            g=g - progreso;
//            b=b - progreso;
//
//            if(r<0)
//                r=0;
//            if(g<0)
//                g=0;
//            if(b<0)
//                b=0;
//
//            pixels[i] = Color.argb(a, r, g, b);
//
//        }
//
//        imagenFiltrada.setPixels(pixels,0,w,0,0,w,h);
//        //imagen = imagenFiltrada;
//
//        iv1.setImageBitmap(imagenFiltrada);

    }

//    private void aclarar() {
//
//            linearLayout1.addView(seekbar);
//
//            int w = imagen.getWidth();
//            int h = imagen.getHeight();
//
//            pixels=new int[w*h];
//
//            imagen.getPixels(pixels,0,w,0,0,w,h);
//
//            imagenFiltrada = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//
//
//            int r,g,b,a;
//
//            for(int i=0; i<pixels.length; i++){
//                r= Color.red(pixels[i]);
//                g= Color.green(pixels[i]);
//                b= Color.blue(pixels[i]);
//                a= Color.alpha(pixels[i]);
//
//                r=r + progreso;
//                g=g + progreso;
//                b=b + progreso;
//
//                if(r>255)
//                    r=255;
//                if(g>255)
//                    g=255;
//                if(b>255)
//                    b=255;
//
//                pixels[i] = Color.argb(a, r, g, b);
//
//            }
//
//            imagenFiltrada.setPixels(pixels,0,w,0,0,w,h);
//            //imagen = imagenFiltrada;
//
//            iv1.setImageBitmap(imagenFiltrada);
//
//    }

    private void invertirColores() {

        Core.bitwise_not(matrizImagen,matProcesada);
        Utils.matToBitmap(matProcesada,imagenFiltrada);
        iv1.setImageBitmap(imagenFiltrada);
        invertido=true;
//        int w = VistaPrevia.imagen.getWidth();
//        int h = VistaPrevia.imagen.getHeight();
//
//        pixels=new int[w*h];
//
//        VistaPrevia.imagen.getPixels(pixels,0,w,0,0,w,h);
//
////        imagenFiltrada = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//
//
//        int r,g,b,a;
//
//        for(int i=0; i<pixels.length; i++){
//
//            r= 255 - Color.red(pixels[i]);
//            g= 255 - Color.green(pixels[i]);
//            b= 255 - Color.blue(pixels[i]);
//            a= Color.alpha(pixels[i]);
//
//            pixels[i] = Color.argb(a, r, g, b);
//
//        }
//
//        imagenFiltrada.setPixels(pixels,0,w,0,0,w,h);
//        //imagen = imagenFiltrada;
//
//        iv1.setImageBitmap(imagenFiltrada);
    }

    private class HiloContraste extends Thread{

        public synchronized void reanudar(){
            pausa=false;
            notify();
        }

        @Override
        public void run() {
            super.run();

            Log.d("HiloBrillo",String.valueOf(seekBarDetenido));

            while(!seekBarDetenido) {
                procesoContraste();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageBitmap(imagenFiltrada);
                        textoLimite.setText(String.valueOf(progreso));
                    }
                });
                constanteContraste.set(repeticionContraste,(float) progreso);
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    private void procesoContraste() {

        matrizImagen.convertTo(matProcesada, CvType.CV_8U, (float) progreso / 100,-(((float) progreso / 100)*150-150));

//                Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2BGR);

        Utils.matToBitmap(matProcesada, imagenFiltrada);

    }

    private  class HiloSaturacion extends Thread{

        Bitmap bitmap=Bitmap.createBitmap(imagenFiltrada.getWidth(),imagenFiltrada.getHeight(), Bitmap.Config.RGB_565);

        Canvas canvasResult = new Canvas(bitmap);

        Paint paint = new Paint();
        public synchronized void reanudar(){
            pausa=false;
            notify();
        }

        @Override
        public void run() {
            super.run();

            while(!seekBarDetenido) {

                procesoSaturacion(bitmap,canvasResult,paint);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageBitmap(bitmap);
                        textoLimite.setText(String.valueOf(progreso));
                    }
                });
                constanteSaturacion.set(repeticionSaturacion,(float)progreso/50);
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    private void procesoSaturacion(Bitmap bitmap,Canvas canvasResult,Paint paint) {

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation((float)progreso/50);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

        paint.setColorFilter(filter);

        canvasResult.drawBitmap(imagenFiltrada, 0, 0, paint);

        Utils.bitmapToMat(bitmap,matProcesada);
        matrizImagen=matProcesada.clone();
//      imagenFiltrada=bitmap.copy(Bitmap.Config.RGB_565,true);
    }

    private class HiloBrillo extends Thread{

        public synchronized void reanudar(){
            pausa=false;
            notify();
        }

        @Override
        public void run() {
            super.run();
            Log.d("HiloBrillo",String.valueOf(seekBarDetenido));
            while(!seekBarDetenido) {
                procesoBrillo();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageBitmap(imagenFiltrada);
                        textoLimite.setText(String.valueOf(progreso));
                    }
                });
                constanteBrillo.set(repeticionBrillo,(float)progreso-100);
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void procesoBrillo() {
            matrizImagen.convertTo(matProcesada, matrizImagen.type(), 1,(float)progreso-100);
//                Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2BGR);
            Utils.matToBitmap(matProcesada, imagenFiltrada);
    }

    public void saturacion(){
      hiloSaturacion=new HiloSaturacion();
//        Mat matHSV=new Mat(matrizImagen.cols(),matrizImagen.rows(),CvType.CV_8UC3);
//        Imgproc.cvtColor(matrizImagen,matHSV,Imgproc.COLOR_RGB2HSV);
//        int size=(int)matHSV.total()*matHSV.channels();
//
//        double []a;
//
////        a=matHSV.get(20,20);
//
//        for(int i=0;i<matHSV.rows();i++)
//            for(int j=0;j<matHSV.cols();j++) {
//                a=matHSV.get(i,j);
//                a[1]=150;
//                matHSV.put(i,j,a);
//            }
//        Imgproc.cvtColor(matHSV,matHSV,Imgproc.COLOR_HSV2RGB);
//        Utils.matToBitmap(matHSV,imagenFiltrada);
//        iv1.setImageBitmap(imagenFiltrada);
    }

    public void setBrillo(){
        hiloBrillo=new HiloBrillo();
    }

    public void nitidez(){

       hiloNitidez =new HiloNitidez();

    }

    private class  HiloNitidez extends Thread{
        public synchronized void reanudar(){
            pausa=false;
            notify();
        }
        @Override
        public void run() {
            super.run();
            while(!seekBarDetenido) {
                procesoNitidez();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageBitmap(imagenFiltrada);
                        textoLimite.setText(String.valueOf(progreso));
                    }
                });
                constanteNitidez.set(repeticionNitidez,(float)progreso);
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void procesoNitidez() {
        ////////////////////////Aumentando alpha y beta pero manteniendo la relacion, aumenta la nitidez.
        Imgproc.GaussianBlur(matrizImagen,matProcesada,new Size(0,0),3,3);
        Core.addWeighted(matrizImagen, 1+(float)progreso/3, matProcesada, - (float)progreso/3, 0, matProcesada);
        Utils.matToBitmap(matProcesada,imagenFiltrada);
    }

    public void ecualizacion(){

        if(matrizImagen.channels()>=3) {
            Vector<Mat> canales = new Vector<Mat>();

            Imgproc.cvtColor(matrizImagen, matProcesada, Imgproc.COLOR_RGB2HSV);

            Core.split(matProcesada, canales);

            Mat matEq=new Mat();
            matEq=canales.get(2).clone();

            Imgproc.equalizeHist(canales.get(2), matEq);
            canales.set(2,matEq);

            Core.merge(canales, matProcesada);

            Imgproc.cvtColor(matProcesada, matProcesada, Imgproc.COLOR_HSV2RGB);
            Utils.matToBitmap(matProcesada, imagenFiltrada);
        }else{
            Imgproc.equalizeHist(matrizImagen,matProcesada);
            Utils.matToBitmap(matProcesada, imagenFiltrada);
        }
        iv1.setImageBitmap(imagenFiltrada);
    }

    private class  HiloTempCol extends Thread{

        public synchronized void reanudar(){
            pausa=false;
            notify();
        }
        @Override
        public void run() {
            super.run();


            while(!seekBarDetenido) {
                procesoTemperaturaColor();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv1.setImageBitmap(imagenFiltrada);
                        textoLimite.setText(String.valueOf(progreso));
                    }
                });
                constanteTempCol.set(repeticionTempCol,(float)progreso);
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void temperaturaColor(){

            hiloTempCol=new HiloTempCol();

    }

    public void procesoTemperaturaColor(){

//        matrizImagen.convertTo(matProcesada, CvType.CV_8UC3);
        matProcesada=matrizImagen.clone();
        Core.split(matProcesada,canales);

//        int size = (int) (canales.get(0).total());
//
//        float[] floatR=new float[size];
//        float[] floatB=new float[size];
//        ////////////////////Esta alreves el mapa de colores, es decir BGR///////////
//        canales.get(0).get(0, 0, floatR);
//        canales.get(2).get(0, 0, floatB);
//
//        float rojo;
//
//        for (int i = 0; i < size; i++) {
//
//            floatR[i] =  (floatR[i]/(progreso/50f));
////            if(floatR[i]>255){
////                floatR[i]=255;
////            }
//            floatB[i] =  (floatB[i]*(progreso/50f));
////            if(floatB[i]>255){
////                floatB[i]= 255;
////            }
//
//        }
//
//        canales.get(0).put(0, 0, floatR);
//        canales.get(2).put(0, 0, floatB);

        Mat mRojo=new Mat();
        mRojo=canales.get(0).clone();
        Mat mAzul=new Mat();
        mAzul=canales.get(2).clone();

        if(progreso==0)
            progreso=1;

        Core.divide(mRojo, new Scalar(progreso / 50f), mRojo);
        Core.multiply(mAzul, new Scalar(progreso / 50f), mAzul);

        canales.set(0,mRojo);
        canales.set(2,mAzul);

        Core.merge(canales,matProcesada);
//        matProcesada.convertTo(matProcesada, CvType.CV_8UC3);
        Utils.matToBitmap(matProcesada,imagenFiltrada);

    }

//    private void rotarImagen(int grados) {
//            if(VistaPrevia.imagen!=null) {
//                Matrix matrix = new Matrix();
//                matrix.postRotate(grados);
//                int w = VistaPrevia.imagen.getWidth();
//                int h = VistaPrevia.imagen.getHeight();
//                Bitmap bitmap = Bitmap.createBitmap(VistaPrevia.imagen, 0, 0, w, h, matrix, false);
//                VistaPrevia.imagen = bitmap;
//                iv1.setImageBitmap(VistaPrevia.imagen);
//                guardarEnCache();
//            }else{
//                Toast.makeText(this,"No existe imagen previa",Toast.LENGTH_SHORT).show();
//            }
//
//    }

//    private void guardarImagen(Object o) {
//
//            FileOutputStream outputStream = null;
//            File file =createFilePath();
//            VistaPrevia.currentFileName=file.getAbsolutePath();
//            try {
//                outputStream = new FileOutputStream(file);
//                VistaPrevia.imagen.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show();
//                outputStream.flush();
//                outputStream.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //galleryAddPic();
//
//    }

    private File createFilePath() {

        File file=getAlbumStorageDir(VistaPrevia.nameFileDirectory);
        if(!file.exists()&&!file.mkdirs()){
            Toast.makeText(this,"No se pudo crear el directorio para grabar la imagen",Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyyhhmmss");
        String date=simpleDateFormat.format(new Date());
        String name="img"+date+".jpg";
        String file_name=file.getAbsolutePath()+"/"+name;
        File new_file=new File(file_name);
        VistaPrevia.currentFileName=file_name;
        return new_file;
    }

    private File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
        file.mkdirs();
        if (!file.mkdirs()) {
            Log.e("directorio", "Directory not created");
        }
        return file;
    }

    private void guardarEnCache() {

        if(imagenFiltrada!=null) {
        VistaPrevia.imagen = imagenFiltrada;
    }

    File file = new File(getExternalCacheDir(), "imagenCache"+"/"+ VistaPrevia.nameFileDirectory);
        file.mkdirs();
        if (!file.mkdirs()) {
        Log.e("directorio", "Directory not created");
    }
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyyhhmmss");
    String date=simpleDateFormat.format(new Date());
    String name="img"+date+".jpg";
    String file_name=file.getAbsolutePath()+"/"+name;
    File new_file=new File(file_name);

        VistaPrevia.currentFileName=new_file.getAbsolutePath();
        VistaPrevia.selectedImage= Uri.fromFile(new_file);

        Imgcodecs.imwrite(VistaPrevia.currentFileName,matrizImagen);
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_filtros,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invertirColores:
                orden.add(1);
                matrizImagen=matProcesada.clone();
                brillo=false;
                saturacionFlag=false;
                contrasteFlag=false;
                binarizacionFlag=false;
                nitidezFlag=false;
                tempColFlag=false;
                invertirColores();
                textoLimite.setVisibility(View.INVISIBLE);
                seekbar.setVisibility(View.INVISIBLE);
                break;
            case R.id.aplicar:
                aplicarProcesamiento();
//                if(imagenFiltrada!=null) {
//                    guardarEnCache();
//                    Toast.makeText(this, "Filtro aplicado", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(this, "No se aplicó ningun filtro...", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.descartar:
                descartarImagenProcesada();
                break;
            case R.id.guardarFiltros:
                guardarFiltros();
                break;
            case R.id.aplicarFiltros:
                if(guardado)
                    aplicarFiltros();
                else
                    Toast.makeText(this, "No se han guardado los filtros aplicados...", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.vistaPrevia:
//                guardarEnCache();
//                Intent intentVista=new Intent(this,VistaPrevia.class);
//                startActivity(intentVista);
//                break;
//            case R.id.editar:
//                if(VistaPrevia.currentFileName!=null){
//                    guardarEnCache();
//                    Intent intent = new Intent(this, Edicion.class);
//                    startActivity(intent);
//                }else{
//                    Toast.makeText(this,"No existe imagen previa",Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.grises:
                orden.add(2);
                matrizImagen=matProcesada.clone();
                brillo=false;
                saturacionFlag=false;
                contrasteFlag=false;
                binarizacionFlag=false;
                nitidezFlag=false;
                tempColFlag=false;
//                linearLayout1.removeView(seekbar);
                textoLimite.setVisibility(View.INVISIBLE);
                seekbar.setVisibility(View.INVISIBLE);
                escalaGrices();
                break;
            case R.id.binarizacion:
                repeticionBinarizado++;
                constanteBinarizacion.add(0f);
                orden.add(3);
                matrizImagen=matProcesada.clone();
                binarizado=true;
                brillo=false;
                saturacionFlag=false;
                contrasteFlag=false;
                binarizacionFlag=true;
                nitidezFlag=false;
                tempColFlag=false;
                textoLimite.setVisibility(View.VISIBLE);
                seekbar.setVisibility(View.VISIBLE);
                seekbar.setProgress(0);
                seekbar.setMax(255);
                textoLimite.setText("0");
                binarizacion();
                break;
            case R.id.brillo:
                repeticionBrillo+=1;
                constanteBrillo.add(1f);
                orden.add(4);
                matrizImagen=matProcesada.clone();
                brillo=true;
                saturacionFlag=false;
                contrasteFlag=false;
                binarizacionFlag=false;
                nitidezFlag=false;
                tempColFlag=false;
//                linearLayout1.removeView(seekbar);
                textoLimite.setVisibility(View.VISIBLE);
                seekbar.setVisibility(View.VISIBLE);
                seekbar.setProgress(100);
                seekbar.setMax(200);
                textoLimite.setText("100");
                setBrillo();
                break;
            case R.id.saturacion:
                repeticionSaturacion+=1;
                constanteSaturacion.add(1f);
                orden.add(5);
                matrizImagen=matProcesada.clone();
                Utils.matToBitmap(matrizImagen,imagenFiltrada);
                brillo=false;
                saturacionFlag=true;
                contrasteFlag=false;
                binarizacionFlag=false;
                nitidezFlag=false;
                tempColFlag=false;
//                linearLayout1.removeView(seekbar);
                textoLimite.setVisibility(View.VISIBLE);
                seekbar.setVisibility(View.VISIBLE);
                seekbar.setProgress(50);
                seekbar.setMax(256);
                textoLimite.setText("50");
                saturacion();
                break;
            case R.id.contraste:
                repeticionContraste+=1;
                constanteContraste.add(1f);
                orden.add(6);
                matrizImagen=matProcesada.clone();
                brillo=false;
                saturacionFlag=false;
                contrasteFlag=true;
                binarizacionFlag=false;
                nitidezFlag=false;
                tempColFlag=false;
//                linearLayout1.removeView(seekbar);
                textoLimite.setVisibility(View.VISIBLE);
                seekbar.setVisibility(View.VISIBLE);
                seekbar.setProgress(100);
                seekbar.setMax(200);
                textoLimite.setText("100");
                contraste();
                break;
            case R.id.nitidez:
                repeticionNitidez+=1;
                constanteNitidez.add(0f);
                orden.add(7);
                matrizImagen=matProcesada.clone();
                brillo=false;
                saturacionFlag=false;
                contrasteFlag=false;
                binarizacionFlag=false;
                nitidezFlag=true;
                tempColFlag=false;
//                linearLayout1.removeView(seekbar);
                textoLimite.setVisibility(View.VISIBLE);
                seekbar.setVisibility(View.VISIBLE);
                seekbar.setProgress(0);
                seekbar.setMax(10);
                textoLimite.setText("0");
                nitidez();
                break;
            case R.id.ecualizacion:
                orden.add(8);
                matrizImagen=matProcesada.clone();
                brillo=false;
                saturacionFlag=false;
                contrasteFlag=false;
                binarizacionFlag=false;
                nitidezFlag=false;
                tempColFlag=false;
//                linearLayout1.removeView(seekbar);
                textoLimite.setVisibility(View.INVISIBLE);
                seekbar.setVisibility(View.INVISIBLE);
                ecualizacion();
                break;
            case R.id.temperaturaColor:
                matrizImagen=matProcesada.clone();
                if(matrizImagen.channels()>2) {
                    repeticionTempCol += 1;
                    constanteTempCol.add(0f);
                    brillo = false;
                    saturacionFlag = false;
                    contrasteFlag = false;
                    binarizacionFlag = false;
                    nitidezFlag = false;
                    tempColFlag = true;
                    orden.add(9);
//                linearLayout1.removeView(seekbar);
                    textoLimite.setVisibility(View.VISIBLE);
                    seekbar.setVisibility(View.VISIBLE);
                    seekbar.setProgress(50);
                    seekbar.setMax(100);
                    textoLimite.setText("50");
                    temperaturaColor();
                }else
                    Toast.makeText(this, "La imagen está en escala de grises", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.guardarImagen:
//                if(imagenFiltrada!=null) {
//                guardarImagen(null);
//                }else{
//                    Toast.makeText(this, "No se aplicó ningun filtro...", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.rotarDerecha:
//                rotarImagen(90);
//                break;
//            case R.id.rotarIzquierda:
//                rotarImagen(-90);
//                break;
//            case R.id.config:
//                break;
//            case R.id.acercaDe:
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void aplicarFiltros() {
        constanteBrillo        =  (Vector<Float>) constanteBrilloS.clone();
        constanteBinarizacion  =  (Vector<Float>)constanteBinarizacionS.clone();
        constanteSaturacion    =  (Vector<Float>) constanteSaturacionS.clone();
        constanteContraste     =  (Vector<Float>) constanteContrasteS.clone();
        constanteNitidez       =  (Vector<Float>) constanteNitidezS.clone();
        constanteTempCol       =  (Vector<Float>) constanteTempColS.clone();

        orden = (ArrayList<Integer>) ordenS.clone();

        repeticionBrillo = repeticionBrilloS;
        repeticionBinarizado = repeticionBinarizadoS;
        repeticionSaturacion = repeticionSaturacionS;
        repeticionContraste = repeticionContrasteS;
        repeticionNitidez = repeticionNitidezS;
        repeticionTempCol = repeticionTempColS;

        aplicarProcesamiento();
    }

    private void guardarFiltros() {
        constanteBrilloS        =  (Vector<Float>) constanteBrillo.clone();
        constanteBinarizacionS  =  (Vector<Float>)constanteBinarizacion.clone();
        constanteSaturacionS    =  (Vector<Float>) constanteSaturacion.clone();
        constanteContrasteS     =  (Vector<Float>) constanteContraste.clone();
        constanteNitidezS       =  (Vector<Float>) constanteNitidez.clone();
        constanteTempColS       =  (Vector<Float>) constanteTempCol.clone();

        ordenS = (ArrayList<Integer>) orden.clone();

        repeticionBrilloS = repeticionBrillo;
        repeticionBinarizadoS = repeticionBinarizado;
        repeticionSaturacionS = repeticionSaturacion;
        repeticionContrasteS = repeticionContraste;
        repeticionNitidezS = repeticionNitidez;
        repeticionTempColS = repeticionTempCol;

        guardado=true;

        Toast.makeText(this, "Filtros guardados con exito...", Toast.LENGTH_SHORT).show();
    }

    private void descartarImagenProcesada() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Descartando...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       progressDialog.show();
                    }
                });
                SystemClock.sleep(200);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recreate();
                        Toast.makeText(Filtros.this,"Procesamiento descartado",Toast.LENGTH_SHORT).show();
                    }
                });
                progressDialog.dismiss();
            }
        }).start();

    }

    private void aplicarProcesamiento() {

        imagenFiltrada= VistaPrevia.imagen.copy(Bitmap.Config.RGB_565,true);
        matrizImagen=new Mat(VistaPrevia.imagen.getHeight(), VistaPrevia.imagen.getWidth(),CvType.CV_8UC3);
        Utils.bitmapToMat(VistaPrevia.imagen,matrizImagen);
        matProcesada=matrizImagen.clone();

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);

        repeticionBrillo=0;
        repeticionBinarizado=0;
        repeticionSaturacion=0;
        repeticionContraste=0;
        repeticionNitidez=0;
        repeticionTempCol=0;

        new Thread(new Runnable() {
            Bitmap bitmap=Bitmap.createBitmap(imagenFiltrada.getWidth(),imagenFiltrada.getHeight(),
                    Bitmap.Config.RGB_565);

            Canvas canvasResult = new Canvas(bitmap);

            Paint paint = new Paint();


            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       pDialog.show();
                    }
                });

                Log.d("aplicarF",String.valueOf(constanteContraste));
                for(int i=0;i<orden.size();i++) {
                    switch (orden.get(i)){
                        case 1:
//                            if (invertido) {
                                Core.bitwise_not(matrizImagen, matProcesada);
                                Utils.matToBitmap(matProcesada, imagenFiltrada);
                                Utils.bitmapToMat(imagenFiltrada, matrizImagen);
//                            }
                            break;
                        case 2:
//                            if (escaladoEnGris) {
                                Imgproc.cvtColor(matrizImagen, matProcesada, Imgproc.COLOR_BGR2GRAY);
                                Utils.matToBitmap(matProcesada, imagenFiltrada);
                                Utils.bitmapToMat(imagenFiltrada, matrizImagen);
//                            }
                            break;
                        case 3:
//                            if (binarizado) {
                                Imgproc.threshold(matrizImagen, matProcesada,
                                        constanteBinarizacion.get(repeticionBinarizado), 255, Imgproc.THRESH_BINARY);
                                Utils.matToBitmap(matProcesada, imagenFiltrada);
                                Utils.bitmapToMat(imagenFiltrada, matrizImagen);
                                repeticionBinarizado++;
//                            }
                            break;
                        case 4:
                            matrizImagen.convertTo(matProcesada, matrizImagen.type(), 1, constanteBrillo.get(repeticionBrillo));
                            Utils.matToBitmap(matProcesada, imagenFiltrada);
                            matrizImagen = matProcesada.clone();
                            repeticionBrillo++;
                            break;
                        case 5:
                            ColorMatrix colorMatrix = new ColorMatrix();
                            colorMatrix.setSaturation(constanteSaturacion.get(repeticionSaturacion));
                            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                            paint.setColorFilter(filter);
                            canvasResult.drawBitmap(imagenFiltrada, 0, 0, paint);
                            imagenFiltrada = bitmap.copy(Bitmap.Config.RGB_565, true);
                            Utils.bitmapToMat(imagenFiltrada, matrizImagen);
                            repeticionSaturacion++;
                            break;
                        case 6:
                            matrizImagen.convertTo(matProcesada, matrizImagen.type(),
                                    constanteContraste.get(repeticionContraste)/100,
                                    -((constanteContraste.get(repeticionContraste)/100)*150-150));
                            Utils.matToBitmap(matProcesada, imagenFiltrada);
                            matrizImagen = matProcesada.clone();
                            repeticionContraste++;
                            break;
                        case 7:
                            Imgproc.GaussianBlur(matrizImagen,matProcesada,new Size(0,0),3,3);
                            Core.addWeighted(matrizImagen, 1+constanteNitidez.get(repeticionNitidez)/3, matProcesada,
                                    - constanteNitidez.get(repeticionNitidez)/3, 0, matProcesada);
                            Utils.matToBitmap(matProcesada,imagenFiltrada);
                            matrizImagen = matProcesada.clone();
                            repeticionNitidez++;
                            break;
                        case 8:
                            if(matrizImagen.channels()>=3) {
                                Vector<Mat> canales = new Vector<Mat>();

                                Imgproc.cvtColor(matrizImagen, matProcesada, Imgproc.COLOR_RGB2HSV);
                                Core.split(matProcesada, canales);

                                Mat matEq=new Mat();
                                matEq=canales.get(2).clone();

                                Imgproc.equalizeHist(canales.get(2), matEq);
                                canales.set(2,matEq);

                                Core.merge(canales, matProcesada);

                                Imgproc.cvtColor(matProcesada, matProcesada, Imgproc.COLOR_HSV2RGB);
                                Utils.matToBitmap(matProcesada, imagenFiltrada);
                            }else{
                                Imgproc.equalizeHist(matrizImagen,matProcesada);
                                Utils.matToBitmap(matProcesada, imagenFiltrada);
                            }
                            matrizImagen = matProcesada.clone();
                            break;
                        case 9:
                            Vector<Mat> canales = new Vector<Mat>();

                            matrizImagen.convertTo(matrizImagen, CvType.CV_32FC3);
                            matProcesada=matrizImagen.clone();

                            Core.split(matrizImagen,canales);

                            Mat mRojo=new Mat();
                            mRojo=canales.get(0).clone();
                            Mat mAzul=new Mat();
                            mAzul=canales.get(2).clone();

                            Core.divide(canales.get(0),new Scalar(constanteTempCol.get(repeticionTempCol)/50f),mRojo);
                            Core.multiply(canales.get(2),new Scalar(constanteTempCol.get(repeticionTempCol)/50f),mAzul);

                            canales.set(0,mRojo);
                            canales.set(2,mAzul);

//                            int size = (int) (canales.get(0).total());
//
//                            float[] floatR=new float[size];
//                            float[] floatB=new float[size];
//                            ////////////////////Esta alreves el mapa de colores, es decir BGR///////////
//                            canales.get(0).get(0, 0, floatR);
//                            canales.get(2).get(0, 0, floatB);
//
//                            for (int j = 0; j < size; j++) {
//
//                                floatR[j] =  (floatR[j]/(constanteTempCol.get(repeticionTempCol)/50f));
////            if(floatR[i]>255){
////                floatR[i]=255;
////            }
//                                floatB[j] =  (floatB[j]*(constanteTempCol.get(repeticionTempCol)/50f));
////            if(floatB[i]>255){
////                floatB[i]= 255;
////            }
//                            }
//
//                            canales.get(0).put(0, 0, floatR);
//                            canales.get(2).put(0, 0, floatB);

                            Core.merge(canales,matProcesada);
                            matProcesada.convertTo(matProcesada, CvType.CV_8UC3);

                            Utils.matToBitmap(matProcesada, imagenFiltrada);
                            Utils.bitmapToMat(imagenFiltrada, matrizImagen);

                            repeticionTempCol++;

                            break;
                    }
//                    ///////////////////////////////////////////////////////////////////////////
//                    matrizImagen.convertTo(matProcesada, matrizImagen.type(), constanteContrasteAlpha,
//                            constanteContrasteBeta);
//                    Utils.matToBitmap(matProcesada, imagenFiltrada);
//                    matrizImagen = matProcesada.clone();
//                    //////////////////////////////////////////////////////////////////////////
//                    matrizImagen.convertTo(matProcesada, matrizImagen.type(), 1, constanteBrillo);
//                    Utils.matToBitmap(matProcesada, imagenFiltrada);
//                    matrizImagen = matProcesada.clone();
//                    /////////////////////////////////////////////////////////////////////////
//                    ColorMatrix colorMatrix = new ColorMatrix();
//                    colorMatrix.setSaturation(constanteSaturacion);
//                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
//                    paint.setColorFilter(filter);
//                    canvasResult.drawBitmap(imagenFiltrada, 0, 0, paint);
//                    imagenFiltrada = bitmap.copy(Bitmap.Config.RGB_565, true);
//                    Utils.bitmapToMat(imagenFiltrada, matrizImagen);
//                    //////////////////////////////////////////////////////////////////////////
//                    if (invertido) {
//                        Core.bitwise_not(matrizImagen, matProcesada);
//                        Utils.matToBitmap(matProcesada, imagenFiltrada);
//                        Utils.bitmapToMat(imagenFiltrada, matrizImagen);
//                    }
//                    //////////////////////////////////////////////////////////////////////////
//                    if (escaladoEnGris) {
//                        Imgproc.cvtColor(matrizImagen, matProcesada, Imgproc.COLOR_BGR2GRAY);
//                        Utils.matToBitmap(matProcesada, imagenFiltrada);
//                        Utils.bitmapToMat(imagenFiltrada, matrizImagen);
//                    }
//                    /////////////////////////////////////////////////////////////////////////////
//                    if (binarizado) {
//                        Imgproc.threshold(matrizImagen, matProcesada, limiteBinarizacion, 255, Imgproc.THRESH_BINARY);
//                        Utils.matToBitmap(matProcesada, imagenFiltrada);
//                        Utils.bitmapToMat(imagenFiltrada, matrizImagen);
//                    }
//                    /////////////////////////////////////////////////////////////////////////////
                }
//                VistaPrevia.imagen=bitmap.copy(Bitmap.Config.RGB_565,true);
//                Utils.matToBitmap(matProcesada2,VistaPrevia.imagen);
//                Mat imagenMatriz=new Mat();
//                Mat imagenMatrizProcesada;
//                Mat kernel = new Mat(3,3, CvType.CV_32F){
//                    {
//                        put(0,0,-1);
//                        put(0,1,-1);
//                        put(0,2,-1);
//
//                        put(1,0,-1);
//                        put(1,1,9);
//                        put(1,2,-1);
//
//                        put(2,0,-1);
//                        put(2,1,-1);
//                        put(2,2,-1);
//                    }
//                };
//                Imgproc.filter2D(matrizImagen,matProcesada,-1,kernel);
//                matrizImagen = matProcesada.clone();

                Imgproc.cvtColor(matrizImagen,matrizImagen,Imgproc.COLOR_BGR2RGB);

//                Imgcodecs.imwrite(VistaPrevia.currentFileName,matrizImagen);

                guardarEnCache();

                pDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Filtros.this,"Hecho",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Filtros.this,VistaPrevia.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(imagen!=null)
//            imagen.recycle();
//        if(imagenFiltrada!=null)
//            imagenFiltrada.recycle();
//        Intent intent=new Intent(this,VistaPrevia.class);
//        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
