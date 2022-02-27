package com.example.windows10.leerimagen;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Ki67ObtenerColorActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    LinearLayout linearLayout2;
    LinearLayout linearLayoutControles;
    FrameLayout frameLayout;
    ImageView zoomBoton;
    Vista vista;
    Bitmap bitmap;
    Bitmap bitmapModificado;
    Bitmap bitmapBuffer;
    Bitmap bitmapRecortado;
    Path pathRecuperado;
    Matrix matrix;
    Matrix matrixBuffer;

    String currentFileName2;
    Bundle recuperado;

    boolean destruido=false;
    boolean lapiz=false;
    boolean flecha=false;
    boolean rotarFlecha=false;
    boolean rotarFlechaLeft=false;
    boolean nuevaFlecha=true;
    boolean medir=false;




    //Bitmap bitmapRecuperado;
    //ImageView imageView;
    //ObjectOutputStream fileOut;
    //Path[] path=new Path[3];
    //Paint[] paint=new Paint[3];

    ArrayList<Path> paths=new ArrayList<Path>();
    ArrayList<Paint> paints=new ArrayList<Paint>();
    ArrayList<Paint> paintsV=new ArrayList<Paint>();
    ArrayList<Bitmap> bitmapFiguras=new ArrayList<Bitmap>();

    ArrayList<Integer> xf=new ArrayList<Integer>();
    ArrayList<Integer> yf=new ArrayList<Integer>();
    ArrayList<Integer> xfV=new ArrayList<Integer>();
    ArrayList<Integer> yfV=new ArrayList<Integer>();
    ArrayList<Integer> xi=new ArrayList<Integer>();
    ArrayList<Integer> yi=new ArrayList<Integer>();

//    ArrayList<Integer> rMinLP=new ArrayList<Integer>();
//    ArrayList<Integer> rMaxLP=new ArrayList<Integer>();
//    ArrayList<Integer> gMinLP=new ArrayList<Integer>();
//    ArrayList<Integer> gMaxLP=new ArrayList<Integer>();
//    ArrayList<Integer> bMinLP=new ArrayList<Integer>();
//    ArrayList<Integer> bMaxLP=new ArrayList<Integer>();

    ArrayList<Integer> pRojoLP=new ArrayList<Integer>();
    ArrayList<Integer> pVerdeLP=new ArrayList<Integer>();
    ArrayList<Integer> pAzulLP=new ArrayList<Integer>();

//    ArrayList<Integer> rMinLNP=new ArrayList<Integer>();
//    ArrayList<Integer> rMaxLNP=new ArrayList<Integer>();
//    ArrayList<Integer> gMinLNP=new ArrayList<Integer>();
//    ArrayList<Integer> gMaxLNP=new ArrayList<Integer>();
//    ArrayList<Integer> bMinLNP=new ArrayList<Integer>();
//    ArrayList<Integer> bMaxLNP=new ArrayList<Integer>();

    ArrayList<Integer> pRojoLNP=new ArrayList<Integer>();
    ArrayList<Integer> pVerdeLNP=new ArrayList<Integer>();
    ArrayList<Integer> pAzulLNP=new ArrayList<Integer>();

    ArrayList<Integer> pRojoEE=new ArrayList<Integer>();
    ArrayList<Integer> pVerdeEE=new ArrayList<Integer>();
    ArrayList<Integer> pAzulEE=new ArrayList<Integer>();


    int numeroFlecha=-1;
    int color=0;
    int indice=-1;
    int currentBitmap=-1;
    int wBitmap,hBitmap,a,r,g,b;
    int numeroRotacion=0;
    int ancho=0;
    int alto=0;
    int altoInicial=0;
    int altoFinal=0;
    int anchoInicial=0;
    int anchoFinal=0;
    float R1,R2;

    View decorView;
    private GestureDetectorCompat mDetector;
    private boolean oculto;
    ////////////////////////////////////////////////////////
    private ScaleGestureDetector mScaleDetector;
    boolean zoom=true;
    private int indiceDoubleTap=0;
    private float mScaleFactor=1.0f;
    float origScale;
    float saveScale = 1.f;
    float mContentViewX;
    float mContentViewY;
    float minTranslate;
    float maxTranslate;
    float xFocusView;
    float yFocusView;
    float xFocusContent;
    float yFocusContent;
    float xTransMin;
    float xTransMax;
    float yTransMin;
    float yTransMax;
    float focusX;
    float focusY;
    float focusPrevioX=-1;
    float focusPrevioY=-1;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX;
    private float mPosY;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    float minScale = 1.f;
    float maxScale = 8.f;
    int viewWidth, viewHeight;
    boolean touchDentroVista=true;
    boolean touchFueraVista=false;
    private  float densidad;
    /////////////////////////////////////////////////
    private ImageButton lapizBoton;
    private Button nuevoBoton;
    boolean activo=false;
    private ImageButton flechaBoton;
    private ImageButton cancelarBoton;
    private ImageButton okBoton;
    private Edicion.VistaColor colorImagen;
    private RelativeLayout RelativeLayoutControles;
    private SeekBar seekbar;
    private ImageButton grosorLapiz;
    private float tamañoStroke=1*densidad;
    ArrayList<Float> tamañoStrokeArray=new ArrayList<Float>();
    ArrayList<Float> radio=new ArrayList<Float>();
    ArrayList<Float> radioV=new ArrayList<Float>();
    private Drawable flechaDrawable;
    Paint paintSofware;
    private ArrayList<ShapeDrawable> flechaShapeDrawable=new ArrayList<ShapeDrawable>();
    private ArrayList<Shape> shapes=new ArrayList<Shape>();
    private boolean cGrosorLapiz=true;
    private boolean cGrosorFlecha=false;
    private ArrayList<Float> tamañoFlechaArrayW=new ArrayList<Float>();
    private Float guardarTamañoflecha;
    private Float guardarTamañoLapiz;
    private ArrayList<Paint> paintsFlechas=new ArrayList<Paint>();
    private ArrayList<Integer> colors=new ArrayList<Integer>();
    private int radioInval;
    private int cenX,cenY;
    private ArrayList<Integer> rotaciones=new ArrayList<Integer>();
    private ArrayList<Float> tamañoFlechaArrayH=new ArrayList<Float>();
    private EditText EditTextCanvas;
    private ImageButton botonTexto;
    private RelativeLayout relativeLayoutTexto;
    private ImageButton grosorTexto;
    private ImageButton botonOk;
    private final ArrayList<Integer> xTexts=new ArrayList<Integer>();
    private final ArrayList<Integer> yTexts=new ArrayList<Integer>();
    private int currentTexto=-1;
    private int numeroTexto=-1;
    private ArrayList<String> strings=new ArrayList<String>();
    private SeekBar seekBarTexto;
    private ArrayList<TextPaint> textsPaintCanvas = new ArrayList<TextPaint>();
    private ImageButton botonCancel;
    private Edicion.VistaColor vistaColorTexto;
    private boolean circulo;
    private int progresoBar;
    private int contadorM=0;
    private boolean circuloRojo=false,circuloVerde=false;
    private int indiceR=-1,indiceV=-1;
    private TextView rojo,verde,total,porcentaje;
    private boolean obtenerColorP=false,obtenerColorNP=false,obtenerColorEE=false;
    private int rMax,rMin,gMax,gMin,bMax,bMin;
    private Bitmap bitmapColor;
    private int xColor,yColor,xColori,yColori,xColorf,yColorf;
    private Path pathObtenerColor;
    private boolean okBotonB=false,nuevoBotonB=false;
    private boolean okBotonPresionado=false;
    private int totalMuestras=0;
    private int rPromedio=0,gPromedio=0,bPromedio=0;
    private Mat matrizColor;
    private Scalar scalarColor;
    private TextView textV,textV2,textV3,textV4;
    private ImageView imV1,imV2,imV3;

    //todo promediar min y max
    //todo aparezca nuevo y listo y desaparezcan
    //todo extraer para ambas celulas P y NP


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ki67_obtener_color);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbarKi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable fondo = getResources().getDrawable(R.drawable.fondo);
        getSupportActionBar().setBackgroundDrawable(fondo);

        linearLayout=(LinearLayout)findViewById(R.id.l1);
        linearLayout2=(LinearLayout) findViewById(R.id.l2);

        textV=(TextView)findViewById(R.id.tview1);
        textV2=(TextView)findViewById(R.id.tview2);
        textV3=(TextView)findViewById(R.id.tview3);
        textV4=(TextView)findViewById(R.id.tview4);

        imV1=(ImageView)findViewById(R.id.imV1);
        imV2=(ImageView)findViewById(R.id.imV2);
        imV3=(ImageView)findViewById(R.id.imV3);


        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        densidad = getResources().getDisplayMetrics().density;
        Log.i("densidad",String.valueOf(densidad));


        iniciarLayoutControles();
        onClickControles();

        okBoton.setVisibility(View.INVISIBLE);
        nuevoBoton.setVisibility(View.INVISIBLE);
        cancelarBoton.setVisibility(View.INVISIBLE);

        oculto=true;

        recuperado=savedInstanceState;

        if(savedInstanceState==null) {

//            colorImagen.setBackgroundColor(color);

            vista = new Vista(this);
            vista.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,0.85f));
            //linearLayout.addView(vista);
            linearLayout2.addView(vista);
            linearLayout2.addView(RelativeLayoutControles);

        }else {
            color=savedInstanceState.getInt("color",0);
            destruido=savedInstanceState.getBoolean("destruido",false);

            vista = new Vista(this);
            vista.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,0.85f));
            //linearLayout.addView(vista);
            linearLayout2.addView(vista);
            linearLayout2.addView(RelativeLayoutControles);
        }

    }

    private void onClickControles() {

        zoomBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zoom=true;
                circuloVerde=false;
                circuloRojo=false;
                touchFueraVista=true;
                touchDentroVista=false;

                zoomBoton.setActivated(true);

                if(obtenerColorEE||obtenerColorNP||obtenerColorP) {
                    nuevoBoton.setVisibility(View.VISIBLE);
                    okBoton.setVisibility(View.INVISIBLE);
                    cancelarBoton.setVisibility(View.INVISIBLE);
                }

                invalidateOptionsMenu();
            }
        });
        //llama al onClick de zoomBoton
        zoomBoton.performClick();

        cancelarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchFueraVista=false;
                touchDentroVista=true;
                okBotonB=false;
                nuevoBotonB=false;
                flecha=false;
                medir=false;
                zoom=false;
                okBotonPresionado=true;

                zoomBoton.setActivated(false);

                pathObtenerColor.reset();

                xColori=0;
                xColorf=0;
                yColori=0;
                yColorf=0;

                xColor=0;
                yColor=0;

                rPromedio=0;
                gPromedio=0;
                bPromedio=0;

                r=0;
                g=0;
                b=0;

                totalMuestras = 0;

                okBoton.setVisibility(View.INVISIBLE);
                nuevoBoton.setVisibility(View.VISIBLE);
                cancelarBoton.setVisibility(View.INVISIBLE);

                invalidateOptionsMenu();
                vista.invalidate();

            }
        });


        okBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                touchFueraVista=false;
                touchDentroVista=true;
                okBotonB=true;
                nuevoBotonB=false;
                flecha=false;
                medir=false;
                zoom=false;
                color = Color.argb(255,40,220,120);
                zoomBoton.setActivated(false);

                if(yColorf-yColori<0){
                    int auxf=yColorf;
                    int auxi=yColori;
                    yColori=auxf;
                    yColorf=auxi;
                }
                if(xColorf-xColori<0){
                    int auxf=xColorf;
                    int auxi=xColori;
                    xColori=auxf;
                    xColorf=auxi;
                }

                Mat matrizColorR=new Mat();
                matrizColorR=matrizColor.submat(yColori,yColorf,xColori,xColorf);
                scalarColor=Core.mean(matrizColorR);

                double[] valorColor=scalarColor.val;


                if(obtenerColorP){
//                    rMinLP.add(rMin);
//                    rMaxLP.add(rMax);
//                    gMinLP.add(gMin);
//                    gMaxLP.add(gMax);
//                    bMinLP.add(bMin);
//                    bMaxLP.add(bMax);
                    if(totalMuestras!=0) {
                        //                        pRojoLP.add(rPromedio / totalMuestras);
//                        pVerdeLP.add(gPromedio / totalMuestras);
//                        pAzulLP.add(bPromedio / totalMuestras);

                        pRojoLP.add((int)valorColor[0]);
                        pVerdeLP.add((int)valorColor[1]);
                        pAzulLP.add((int)valorColor[2]);

                        int pR=promedio(pRojoLP);
                        int pG=promedio(pVerdeLP);
                        int pB=promedio(pAzulLP);


                        r=(int)valorColor[0];
                        g=(int)valorColor[1];
                        b=(int)valorColor[2];

                        imV1.setBackgroundColor(Color.rgb(pR,pG,pB));

                        totalMuestras = (int)matrizColorR.total();

                    }

                }else if(obtenerColorNP){
//                    rMinLNP.add(rMin);
//                    rMaxLNP.add(rMax);
//                    gMinLNP.add(gMin);
//                    gMaxLNP.add(gMax);
//                    bMinLNP.add(bMin);
//                    bMaxLNP.add(bMax);
                    if(totalMuestras!=0) {
                        pRojoLNP.add((int)valorColor[0]);
                        pVerdeLNP.add((int)valorColor[1]);
                        pAzulLNP.add((int)valorColor[2]);

                        int pR=promedio(pRojoLNP);
                        int pG=promedio(pVerdeLNP);
                        int pB=promedio(pAzulLNP);

                        r=(int)valorColor[0];
                        g=(int)valorColor[1];
                        b=(int)valorColor[2];

                        imV2.setBackgroundColor(Color.rgb(pR,pG,pB));


                        totalMuestras = (int)matrizColorR.total();
                    }
                }else if(obtenerColorEE){

                    if(totalMuestras!=0) {
                        pRojoEE.add((int)valorColor[0]);
                        pVerdeEE.add((int)valorColor[1]);
                        pAzulEE.add((int)valorColor[2]);

                        int pR=promedio(pRojoEE);
                        int pG=promedio(pVerdeEE);
                        int pB=promedio(pAzulEE);

                        r=(int)valorColor[0];
                        g=(int)valorColor[1];
                        b=(int)valorColor[2];

                        imV3.setBackgroundColor(Color.rgb(pR,pG,pB));

                        totalMuestras = (int)matrizColorR.total();
                    }

                }

                Toast.makeText(Ki67ObtenerColorActivity.this, "Colores obtenidos...", Toast.LENGTH_SHORT).show();

                okBotonPresionado=true;

                okBoton.setVisibility(View.INVISIBLE);
                cancelarBoton.setVisibility(View.INVISIBLE);
                nuevoBoton.setVisibility(View.VISIBLE);

                invalidateOptionsMenu();
                vista.invalidate();
            }
        });
        nuevoBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                touchFueraVista=false;
                touchDentroVista=true;
                okBotonB=false;
                nuevoBotonB=true;
                flecha=false;
                medir=false;
                zoom=false;
                okBotonPresionado=false;

                color = Color.argb(255,200,40,40);
                zoomBoton.setActivated(false);

                pathObtenerColor.reset();

                xColor=0;
                yColor=0;

                xColori=0;
                xColorf=0;
                yColori=0;
                yColorf=0;

                rPromedio=0;
                gPromedio=0;
                bPromedio=0;

                r=0;
                g=0;
                b=0;

                totalMuestras = 0;

                okBoton.setVisibility(View.VISIBLE);
                nuevoBoton.setVisibility(View.INVISIBLE);
                cancelarBoton.setVisibility(View.VISIBLE);

                invalidateOptionsMenu();
                vista.invalidate();

            }
        });


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void iniciarLayoutControles() {
        LinearLayout.LayoutParams layoutParamsR=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,0.15f);
        layoutParamsR.setMargins(0,(int)(10*densidad),0,0);

        RelativeLayoutControles =new RelativeLayout(this);
        RelativeLayoutControles.setPadding((int)(5*densidad),(int)(5*densidad),(int)(5*densidad),(int)(5*densidad));
        RelativeLayoutControles.setLayoutParams(layoutParamsR);
        RelativeLayoutControles.setBackgroundColor(Color.DKGRAY);
        RelativeLayoutControles.setGravity(Gravity.CENTER_VERTICAL);

        Drawable drawable=getResources().getDrawable(R.drawable.boton_zoom);

        final RelativeLayout.LayoutParams layoutParamsSeekbar=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(40*densidad));
//        layoutParamsSeekbar.setMargins(0,0,0,(int)(10*densidad));


        final RelativeLayout.LayoutParams layoutParamsZoom=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsZoom.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsZoom.setMarginEnd((int)(10*densidad));
        layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);
        layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);

        final RelativeLayout.LayoutParams layoutParamsCirculo=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsCirculo.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsCirculo.setMarginEnd((int)(10*densidad));

        final RelativeLayout.LayoutParams layoutParamsColor=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsColor.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsColor.setMarginEnd((int)(10*densidad));

        final RelativeLayout.LayoutParams layoutParamsCancelar=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsCancelar.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsCancelar.setMarginEnd((int)(10*densidad));


        zoomBoton=new ImageButton(this);
        zoomBoton.setId(R.id.zoomBoton);
        zoomBoton.setBackground(drawable);
        zoomBoton.setImageResource(R.drawable.ic_zoom_in_white_36dp);
        zoomBoton.setLayoutParams(layoutParamsZoom);

        layoutParamsCirculo.addRule(RelativeLayout.RIGHT_OF,zoomBoton.getId());
        layoutParamsCirculo.addRule(RelativeLayout.END_OF,zoomBoton.getId());
        layoutParamsCirculo.addRule(RelativeLayout.ALIGN_BOTTOM,zoomBoton.getId());

        nuevoBoton=new Button(this);
        nuevoBoton.setId(View.generateViewId());
        nuevoBoton.setBackgroundResource(R.drawable.boton_lapiz);
        nuevoBoton.setText("Nuevo");
        nuevoBoton.setLayoutParams(layoutParamsCirculo);

        layoutParamsCancelar.addRule(RelativeLayout.RIGHT_OF,nuevoBoton.getId());
        layoutParamsCancelar.addRule(RelativeLayout.END_OF,nuevoBoton.getId());
        layoutParamsCancelar.addRule(RelativeLayout.ALIGN_BOTTOM,nuevoBoton.getId());

        cancelarBoton=new ImageButton(this);
        cancelarBoton.setId(View.generateViewId());
        cancelarBoton.setBackgroundResource(R.drawable.boton_lapiz);
        cancelarBoton.setImageResource(R.drawable.ic_action_text_close);
        cancelarBoton.setLayoutParams(layoutParamsCancelar);


        layoutParamsColor.addRule(RelativeLayout.RIGHT_OF,cancelarBoton.getId());
        layoutParamsColor.addRule(RelativeLayout.END_OF,cancelarBoton.getId());
        layoutParamsColor.addRule(RelativeLayout.ALIGN_BOTTOM,cancelarBoton.getId());


        okBoton=new ImageButton(this);
        okBoton.setId(ImageButton.generateViewId());
        okBoton.setBackgroundResource(R.drawable.boton_lapiz);
        okBoton.setImageResource(R.drawable.ic_ok);
        okBoton.setLayoutParams(layoutParamsColor);


        RelativeLayoutControles.addView(zoomBoton);
        RelativeLayoutControles.addView(nuevoBoton);
        RelativeLayoutControles.addView(cancelarBoton);
        RelativeLayoutControles.addView(okBoton);

    }


    private void cambiarTamaño(int progress) {
//        if(indice>-1)
//            tamañoStrokeArray.set(indice, progress * densidad);
        progresoBar=progress;
        vista.invalidate();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.i("onWindow","entro");
        /*if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }*/
    }

    public void guardar(View view){

        boolean valoresObtenidos=false;
        int pRojoCP=promedio(pRojoLP);
        int pVerdeCP=promedio(pVerdeLP);
        int pAzulCP=promedio(pAzulLP);

        int pRojoCNP=promedio(pRojoLNP);
        int pVerdeCNP=promedio(pVerdeLNP);
        int pAzulCNP=promedio(pAzulLNP);

        int pRojoEEx=promedio(pRojoEE);
        int pVerdeEEx=promedio(pVerdeEE);
        int pAzulEEx=promedio(pAzulEE);

        Intent intent=new Intent(this,RecuentoKi67Activity.class);

        if(pRojoCP!=0||pVerdeCP!=0||pAzulCP!=0) {
            intent.putExtra("rojoCP", pRojoCP);
            intent.putExtra("verdeCP", pVerdeCP);
            intent.putExtra("azulCP", pAzulCP);
            valoresObtenidos=true;
        }
        if(pRojoCNP!=0||pVerdeCNP!=0||pAzulCNP!=0) {
            intent.putExtra("rojoCNP", pRojoCNP);
            intent.putExtra("verdeCNP", pVerdeCNP);
            intent.putExtra("azulCNP", pAzulCNP);
            valoresObtenidos=true;
        }
        if(pRojoEEx!=0||pVerdeEEx!=0||pAzulEEx!=0) {
            intent.putExtra("rojoEE", pRojoEEx);
            intent.putExtra("verdeEE", pVerdeEEx);
            intent.putExtra("azulEE", pAzulEEx);
            valoresObtenidos=true;
        }
        if(valoresObtenidos) {
            setResult(RESULT_OK, intent);
            Toast.makeText(this, "Datos guardados...", Toast.LENGTH_LONG).show();
            finish();
        }else {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    private int promedio(ArrayList<Integer> minMax) {
        int suma=0;
        int promedio=0;
        if(!minMax.isEmpty()) {
            for (int i = 0; i < minMax.size(); i++) {
                suma += minMax.get(i);
            }
            promedio = suma / minMax.size();
            return promedio;
        }else
            return 0;
    }

    public void guardarImagen(View v){

        if(bitmap!=null) {
            FileOutputStream outputStream = null;
            File file =createFilePath();
            VistaPrevia.currentFileName=file.getAbsolutePath();
            VistaPrevia.selectedImage= Uri.fromFile(file);
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show();
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            galleryAddPic();
        }else{
            Toast.makeText(this,"No existe imagen previa",Toast.LENGTH_SHORT).show();
        }

    }

    private void guardarEnCache() {

        vista.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        vista.buildDrawingCache();
        bitmap=vista.getDrawingCache();

        if (ancho != 0 || alto != 0) {
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                bitmapRecortado = Bitmap.createBitmap(bitmap, 0, 0, ancho, alto);

            } else {
                if ((R1 > R2) && ((R1-R2)>0.02)) {
                    bitmapRecortado = Bitmap.createBitmap(bitmap, anchoInicial, 0, ancho, alto);
                } else {
                    bitmapRecortado = Bitmap.createBitmap(bitmap, 0, altoInicial, ancho, alto);
                }
            }
            bitmap=bitmapRecortado;
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

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new_file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(VistaPrevia.currentFileName);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

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


    public class Vista extends View{

        String accion=null;
        Drawable image;
        Paint paintCordenada;
        Paint paintObtenerColor;
        Paint paintColorObtenido;


        int x;
        int y;
        int xMedirInicial;
        int yMedirInicial;
        int xMedirFinal;
        int yMedirFinal;
        //        int distancia;


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public Vista(Context context) {
            super(context);

            if (VistaPrevia.currentFileName!=null) {
                image = new BitmapDrawable(getResources(), VistaPrevia.currentFileName);
            }else
                image=getResources().getDrawable(R.drawable.ic_image_black_36dp);

            matrix=new Matrix();
            matrixBuffer=new Matrix();
            paintCordenada = new Paint();
            paintCordenada.setColor(Color.RED);
            paintCordenada.setTextSize(40);
            paintCordenada.setAntiAlias(true);
            paintCordenada.setStyle(Paint.Style.STROKE);

            paintObtenerColor=new Paint();
            paintObtenerColor.setColor(Color.MAGENTA);
            paintObtenerColor.setAntiAlias(true);
            paintObtenerColor.setStyle(Paint.Style.STROKE);
            paintObtenerColor.setStrokeWidth(2);

            paintColorObtenido=new Paint();
            paintColorObtenido.setAntiAlias(true);
            paintColorObtenido.setStyle(Paint.Style.FILL_AND_STROKE);
            paintColorObtenido.setStrokeWidth(1);

            pathObtenerColor=new Path();

            tamañoStrokeArray.add(5 * densidad);

            setBackground(image);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            contadorM++;
            int anchoDeseado = 600;
            int altoDeseado = 600;

            int ancho = obtenDimension(widthMeasureSpec, anchoDeseado);
            int alto = obtenDimension(heightMeasureSpec, altoDeseado);


            setMeasuredDimension(ancho, alto);


        }

        private int obtenDimension(int measureSpec, int deseado) {
            int dimension=MeasureSpec.getSize(measureSpec);
            int modo=MeasureSpec.getMode(measureSpec);

            if(modo==MeasureSpec.EXACTLY){
                return dimension;
            }else if(modo==MeasureSpec.AT_MOST){
                return dimension;
            }else{
                return deseado;
            }
        }


        @Override
        public void buildDrawingCache() {
            super.buildDrawingCache();
        }

        @Override
        public Bitmap getDrawingCache() {
            return super.getDrawingCache();
        }


        public void onDraw(Canvas canvas){

            float imagenX=image.getIntrinsicWidth();
            float imagenY=image.getIntrinsicHeight();
            R1=imagenY/imagenX;

            float w=getMeasuredWidth();
            float h=getMeasuredHeight();
            R2=h/w;

            ancho=0;
            alto=0;
            altoInicial=0;
            altoFinal=0;
            anchoInicial=0;
            anchoFinal=0;

            if ((R1 > R2) && ((R1-R2)>0.02)) {
                ancho = (int) (h / R1);
                alto = (int) h;


                anchoInicial = (int) (w / 2 - ancho/ 2);
                anchoFinal = (int) (w / 2 + ancho / 2);
                if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP)
                {
                    //image.setBounds(anchoInicial/2,0,ancho+anchoInicial/2,alto);
                    image.setBounds(0,0,ancho,alto);
                }else {
                    image.setBounds(anchoInicial,0,anchoFinal,alto);
                }

            }
            if ((R1 < R2) && ((R2-R1)>0.10)) {
                ancho = (int) w;
                alto = (int) (R1 * w);
                image.setBounds(0,0,ancho,alto);

                altoInicial = (int) (h / 2 - alto / 2);
                altoFinal = (int) (h / 2 + alto / 2);
                if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP)
                {
                    //image.setBounds(0,altoInicial/2,ancho,alto+altoInicial/2);
                    image.setBounds(0,0,ancho,alto);
                }else {
                    image.setBounds(0, altoInicial, ancho, altoFinal);
                }

            }

            Log.i("info","Paso por aqui");

            if((obtenerColorP||obtenerColorNP||obtenerColorEE)&&!okBotonPresionado){

                paintCordenada.setColor(Color.RED);

                float scaleX = vista.getScaleX();
                float scaleY = vista.getScaleY();

                //float wScaleImage=vista.getWidth()/scaleX;
                //float hScaleImage=vista.getHeight()/scaleY;
                float focusScaleX = focusPrevioX / scaleX;
                float focusScaleY = focusPrevioY / scaleY;
                float xPaint;
                float yPaint;
                if (scaleX > 1 && scaleY > 1) {
                    xPaint = (focusPrevioX - focusScaleX);
                    yPaint = (focusPrevioY - focusScaleY);
                    xPaint += (-mPosX / scaleX);
                    yPaint += (-mPosY / scaleY);
                } else {
                    xPaint = 0;
                    yPaint = 0;
                }
                float textSize = 50;
                paintCordenada.setTextSize(textSize / scaleX);
//
//                canvas.drawText("RM: " + String.valueOf(r), xPaint + 10/scaleX * densidad,yPaint + (25 * densidad )/scaleY, paintCordenada);
//                canvas.drawText("GM: " + String.valueOf(g), xPaint + 10/scaleX * densidad,yPaint + (45 * densidad )/scaleY, paintCordenada);
//                canvas.drawText("BM: " + String.valueOf(b), xPaint + 10/scaleX * densidad,yPaint + (65 * densidad )/scaleY, paintCordenada);
//                canvas.drawText("Muestra: " + String.valueOf(totalMuestras), xPaint + 10/scaleX * densidad,yPaint + (85 * densidad )/scaleY, paintCordenada);
//
//                paintColorObtenido.setARGB(255,r,g,b);
//
//                canvas.drawCircle(xPaint + 40/scaleX * densidad,yPaint +
//                                (canvas.getHeight()-(40*densidad))/scaleY,15*densidad/scaleX,
//                        paintColorObtenido);
//
//
//                canvas.drawPath(pathObtenerColor,paintObtenerColor);

                canvas.drawRect(xColori,yColori,xColorf,yColorf,paintObtenerColor);

                paintCordenada.setTextSize(45);
            }
            if(okBotonPresionado){
                paintCordenada.setColor(Color.RED);

                float scaleX = vista.getScaleX();
                float scaleY = vista.getScaleY();

                //float wScaleImage=vista.getWidth()/scaleX;
                //float hScaleImage=vista.getHeight()/scaleY;
                float focusScaleX = focusPrevioX / scaleX;
                float focusScaleY = focusPrevioY / scaleY;
                float xPaint;
                float yPaint;
                if (scaleX > 1 && scaleY > 1) {
                    xPaint = (focusPrevioX - focusScaleX);
                    yPaint = (focusPrevioY - focusScaleY);
                    xPaint += (-mPosX / scaleX);
                    yPaint += (-mPosY / scaleY);
                } else {
                    xPaint = 0;
                    yPaint = 0;
                }
                float textSize = 50;
                paintCordenada.setTextSize(textSize / scaleX);

                canvas.drawText("RM: " + String.valueOf(r), xPaint + 10/scaleX * densidad,yPaint + (25 * densidad )/scaleY, paintCordenada);
                canvas.drawText("GM: " + String.valueOf(g), xPaint + 10/scaleX * densidad,yPaint + (45 * densidad )/scaleY, paintCordenada);
                canvas.drawText("BM: " + String.valueOf(b), xPaint + 10/scaleX * densidad,yPaint + (65 * densidad )/scaleY, paintCordenada);
                canvas.drawText("Muestra: " + String.valueOf(totalMuestras), xPaint + 10/scaleX * densidad,yPaint + (85 * densidad )/scaleY, paintCordenada);
            }

            /////////////////////////////////////////////////////////////////////////////////
        }

        public boolean onTouchEvent(MotionEvent e){
//            RecuentoKi67ManualActivity.this.mDetector.onTouchEvent(e);
            x=(int)e.getX();
            y=(int)e.getY();
            int sumaFlechas=0;

            if(e.getAction()==MotionEvent.ACTION_DOWN){
                if((obtenerColorP||obtenerColorNP|obtenerColorEE)&&!okBotonPresionado){

                    xColor=x;
                    yColor=y;

                    xColori=x;
                    yColori=y;

                    xColorf=x;
                    yColorf=y;
//                    pathObtenerColor=new Path();
                    pathObtenerColor.moveTo(xColor,yColor);

                    totalMuestras++;

                    r=Color.red(bitmapColor.getPixel((int)xColor,(int)yColor));
                    g=Color.green(bitmapColor.getPixel((int)xColor,(int)yColor));
                    b=Color.blue(bitmapColor.getPixel((int)xColor,(int)yColor));

                    rPromedio+=r;
                    gPromedio+=g;
                    bPromedio+=b;

                }
                if(circuloRojo) {
                    indiceR++;

                    xf.add(x);
                    yf.add(y);

                    radio.add(seekbar.getProgress() * densidad);
//                    tamañoStrokeArray.add(seekbar.getProgress()*densidad);

                    paints.add(new Paint());
                    paints.get(indiceR).setColor(color);
                    paints.get(indiceR).setAntiAlias(true);
                    paints.get(indiceR).setStyle(Paint.Style.FILL_AND_STROKE);
                    paints.get(indiceR).setStrokeWidth(3 * densidad);
                }else if(circuloVerde){
                    indiceV++;

                    xfV.add(x);
                    yfV.add(y);

                    radioV.add(seekbar.getProgress() * densidad);
//                    tamañoStrokeArray.add(seekbar.getProgress()*densidad);

                    paintsV.add(new Paint());
                    paintsV.get(indiceV).setColor(color);
                    paintsV.get(indiceV).setAntiAlias(true);
                    paintsV.get(indiceV).setStyle(Paint.Style.FILL_AND_STROKE);
                    paintsV.get(indiceV).setStrokeWidth(3 * densidad);
                }

                accion="down";
            }

            if(e.getAction()==MotionEvent.ACTION_MOVE) {
                if((obtenerColorP||obtenerColorNP||obtenerColorEE)&&!okBotonPresionado){

                    xColor=x;
                    yColor=y;

                    xColorf=x;
                    yColorf=y;

                    pathObtenerColor.lineTo(xColor,yColor);

                    totalMuestras++;

                    r=Color.red(bitmapColor.getPixel((int)xColor,(int)yColor));
                    g=Color.green(bitmapColor.getPixel((int)xColor,(int)yColor));
                    b=Color.blue(bitmapColor.getPixel((int)xColor,(int)yColor));

                    rPromedio+=r;
                    gPromedio+=g;
                    bPromedio+=b;


                }

                if(circuloRojo) {
                    xf.set(indiceR,x);
                    yf.set(indiceR,y);
                }else if(circuloVerde){
                    xfV.set(indiceV,x);
                    yfV.set(indiceV,y);
                }
                accion="move";
            }
            if(e.getAction()==MotionEvent.ACTION_UP) {
                if(circuloRojo) {
                    xf.set(indiceR,x);
                    yf.set(indiceR,y);
                }else if(circuloVerde){
                    xfV.set(indiceV,x);
                    yfV.set(indiceV,y);
                }

                accion="up";
            }
            invalidate();

            return touchDentroVista;
        }
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            /*
            zoom=true;
            medir = false;
            relacion = false;
            calibrado = false;
            touchDentroVista=false;
            touchFueraVista=true;
            */

            mode=ZOOM;
            Log.i("Escalado","onScaleBegin");

            return true;
        }
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.i("Escalado","onScale");
            mScaleFactor *= detector.getScaleFactor();

            origScale = saveScale;

            saveScale = mScaleFactor;

            if (saveScale > maxScale) {

                saveScale = maxScale;

                mScaleFactor = saveScale;

            } else if (saveScale < minScale) {

                saveScale = minScale;

                mScaleFactor = saveScale;

            }

            /*
            if (viewWidth * saveScale <= viewWidth || viewHeight * saveScale <= viewHeight) {
                vista.setPivotX(viewWidth / 2);
                vista.setPivotY(viewHeight / 2);
                vista.setScaleX(mScaleFactor);
                vista.setScaleY(mScaleFactor);

            }else {
                vista.setPivotX(detector.getFocusX());
                vista.setPivotY(detector.getFocusY());
                vista.setScaleX(mScaleFactor);
                vista.setScaleY(mScaleFactor);

            }
            */
            //xFocusView = detector.getFocusX();
            //yFocusView = detector.getFocusY();

            viewWidth  = vista.getWidth();
            viewHeight = vista.getHeight();
            mContentViewX = viewWidth*mScaleFactor;
            mContentViewY = viewHeight*mScaleFactor;
            /*
            focusX=detector.getFocusX();
            focusY=detector.getFocusY();
            focusX+=-mPosX;
            focusY+=-mPosY;
            */
            focusX=detector.getFocusX();
            focusY=detector.getFocusY();
            focusX+=-mPosX/mScaleFactor;
            focusY+=-mPosY/mScaleFactor;

            //Inicializa el focusPrevio
            if(mContentViewX==viewWidth && mContentViewY == viewHeight){
                focusPrevioX = focusX;
                focusPrevioY = focusY;
            }
            //focusPrevioX = saveFocusX;
            //focusPrevioY = saveFocusY;

            //saveFocusX=focusX;
            //saveFocusY=focusY;
            //if(Math.abs(mScaleFactor-origScale)>0) {


            // }

            Log.i("Pivot", "x: " + String.valueOf(focusPrevioX) + " " +  String.valueOf(focusX) +
                    " " + String.valueOf(vista.getPivotX()));

            //Zoom a vista
            vista.setScaleX(mScaleFactor);
            vista.setScaleY(mScaleFactor);

            //Calculo de la distancia que debe trasladarse la imagen cuando se realiza zoom.
            xFocusContent = focusPrevioX*mScaleFactor;
            yFocusContent = focusPrevioY*mScaleFactor;

            if (mContentViewX >= viewWidth) {
                if ((xFocusContent - focusPrevioX) == 0) {
                    xTransMin = 0;
                    xTransMax = mContentViewX - viewWidth;
                } else if ((xFocusContent - focusPrevioX) > 0) {
                    xTransMin = xFocusContent - focusPrevioX;
                    xTransMax = (mContentViewX - viewWidth) - xTransMin;
                    //xTransMax = (mContentViewX - viewWidth);
                }
            }
            if (mContentViewY >= viewHeight) {
                if ((yFocusContent - focusPrevioY) == 0) {
                    yTransMin = 0;
                    yTransMax = mContentViewY - viewHeight;
                } else if ((yFocusContent - focusPrevioY ) > 0) {
                    yTransMin = yFocusContent - focusPrevioY;
                    yTransMax = (mContentViewY - viewHeight) - yTransMin;
                    //yTransMax = (mContentViewY - viewHeight);
                }
            }
            //Cambio de Pivot y Permite suavizarlo.
            if ((Math.abs((focusX - focusPrevioX)/mScaleFactor)) > 0 && (Math.abs((focusY - focusPrevioY)/mScaleFactor)) > 0) {
                vista.setPivotX(focusPrevioX);
                vista.setPivotY(focusPrevioY);
                focusPrevioX += ((focusX - focusPrevioX)/mScaleFactor) * Math.abs(mScaleFactor-origScale)*0.5;
                focusPrevioY += ((focusY - focusPrevioY)/mScaleFactor) * Math.abs(mScaleFactor-origScale)*0.5;
            }
                /*
                if((xTransMax==0) && (yTransMax==0)){
                    vista.setTranslationX(0);
                    vista.setTranslationY(0);
                }
                */
            //Reacomoda la imagen cuando se realiza el zoom out
            if((origScale-mScaleFactor)>0) {
                if (mPosX > xTransMin) {
                    mPosX = xTransMin;
                    vista.setTranslationX(mPosX);
                }
                if (-mPosX > xTransMax) {
                    mPosX = -xTransMax;
                    vista.setTranslationX(mPosX);
                }
                if (mPosY > yTransMin) {
                    mPosY = yTransMin;
                    vista.setTranslationY(mPosY);
                }
                if (-mPosY > yTransMax) {
                    mPosY = -yTransMax;
                    vista.setTranslationY(mPosY);
                }
            }


            /*
                xTransMax=0;
                xTransMin=0;
                yTransMax=0;
                yTransMin=0;
                vista.setTranslationX(0);
                vista.setTranslationY(0);
                mPosX=0;
                mPosY=0;
            */

            Log.i("trans", "xTransMax: "+ String.valueOf(xTransMax)+ " xTransMin: " + String.valueOf(xTransMin)+
                    " yTransMax: "+ String.valueOf(yTransMax)+ " yTransMin: " + String.valueOf(yTransMin));
            Log.i("scale", "mPosX: " + String.valueOf(mPosX) + " mPosY: " + String.valueOf(mPosY));
            vista.invalidate();

            return true;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        this.mDetector.onTouchEvent(event);
        // Let the ScaleGestureDetector inspect all events.
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(event);
        final int action = MotionEventCompat.getActionMasked(event);

        if(zoom) {
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    if (!mScaleDetector.isInProgress()) {
                        Log.i("hola", "Down: " + String.valueOf(mScaleDetector.getCurrentSpan()));

                        final float x = event.getX();
                        final float y = event.getY();

                        indiceDoubleTap++;
                        Log.i("hola", "Down: " + String.valueOf(mScaleDetector.getCurrentSpan()));
                        //Tiene como funcion hacer zoom con doble tap
                        android.os.Handler handler=new android.os.Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(indiceDoubleTap==2) {
                                    if (vista.getScaleX() > 1 && vista.getScaleY() > 1) {
                                        vista.setScaleX(1);
                                        vista.setScaleY(1);

                                        xTransMax=0;
                                        xTransMin=0;
                                        yTransMax=0;
                                        yTransMin=0;

                                        mScaleFactor=1;

                                        mPosX=0;
                                        mPosY=0;

                                        vista.setTranslationX(mPosX);
                                        vista.setTranslationY(mPosY);

                                    } else {

                                        mScaleFactor*=3;
                                        saveScale=mScaleFactor;
                                        vista.setPivotX(x);
                                        vista.setPivotY(y);
                                        vista.setScaleX(mScaleFactor);
                                        vista.setScaleY(mScaleFactor);

                                        viewWidth  = vista.getWidth();
                                        viewHeight = vista.getHeight();

                                        mContentViewX = viewWidth*mScaleFactor;
                                        mContentViewY = viewHeight*mScaleFactor;

                                        focusPrevioX = x;
                                        focusPrevioY = y;
                                        xFocusContent = focusPrevioX*mScaleFactor;
                                        yFocusContent = focusPrevioY*mScaleFactor;

                                        if (mContentViewX >= viewWidth) {
                                            if ((xFocusContent - focusPrevioX) == 0) {
                                                xTransMin = 0;
                                                xTransMax = mContentViewX - viewWidth;
                                            } else if ((xFocusContent - focusPrevioX) > 0) {
                                                xTransMin = xFocusContent - focusPrevioX;
                                                xTransMax = (mContentViewX - viewWidth) - xTransMin;
                                                //xTransMax = (mContentViewX - viewWidth);
                                            }
                                        }
                                        if (mContentViewY >= viewHeight) {
                                            if ((yFocusContent - focusPrevioY) == 0) {
                                                yTransMin = 0;
                                                yTransMax = mContentViewY - viewHeight;
                                            } else if ((yFocusContent - focusPrevioY ) > 0) {
                                                yTransMin = yFocusContent - focusPrevioY;
                                                yTransMax = (mContentViewY - viewHeight) - yTransMin;
                                                //yTransMax = (mContentViewY - viewHeight);
                                            }
                                        }

                                    }
                                }
                                indiceDoubleTap=0;
                            }
                        },200);
                        // Remember where we started (for dragging)
                        mLastTouchX = x;
                        mLastTouchY = y;
                        // Save the ID of this pointer (for dragging)
                        //mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                        mode=DRAG;
                    }
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if(mode!=NONE) {
                        if (!mScaleDetector.isInProgress()) {
                            if (!(event.getPointerCount() > 1)) {
                                Log.i("hola", "Traslación: " + String.valueOf(mScaleDetector.getCurrentSpan()));

                                final float x = event.getX();
                                final float y = event.getY();

                                final float dx = x - mLastTouchX;
                                final float dy = y - mLastTouchY;

                                mPosX += dx;
                                mPosY += dy;

                                Log.i("posicion", "mPosX: " + String.valueOf(mPosX) + " mPosY: " + String.valueOf(mPosY));
                                Log.i("Focos", "xTransMax: " + String.valueOf(xTransMax) + " xTransMin: " + String.valueOf(xTransMin) +
                                        " yTransMax: " + String.valueOf(yTransMax) + " yTransMin: " + String.valueOf(yTransMin));

                                if (mPosX > 0 && mPosX <= xTransMin) {
                                    vista.setTranslationX(mPosX);
                                    focusX=mPosX;
                                }
                                if (mPosX < 0 && -mPosX <= xTransMax) {
                                    vista.setTranslationX(mPosX);
//                                    focusX=mPosX;
                                }
                                if (mPosY > 0 && mPosY <= yTransMin) {
                                    vista.setTranslationY(mPosY);
//                                    focusY=mPosY;
                                }
                                if (mPosY < 0 && -mPosY <= yTransMax) {
                                    vista.setTranslationY(mPosY);
//                                    focusY=mPosY;
                                }

                                if (mPosX > xTransMin)
                                    mPosX = xTransMin;
                                if (-mPosX > xTransMax)
                                    mPosX = -xTransMax;
                                if (mPosY > yTransMin)
                                    mPosY = yTransMin;
                                if (-mPosY > yTransMax)
                                    mPosY = -yTransMax;


                            /*
                            if(Math.abs(mPosX) < xTransMax)
                                vista.setTranslationX(mPosX);
                            if(Math.abs(mPosY) < yTransMax)
                                vista.setTranslationY(mPosY);
                                */
                                /*
                                if (mPosX > xTransMax)
                                    mPosX = xTransMax;
                                if (mPosX > xTransMin)
                                    mPosX = xTransMin;
                                if (mPosY > yTransMax)
                                    mPosY = yTransMax;
                                if (mPosY > yTransMin)
                                    mPosY = yTransMin;
                                 */

                                //vista.setTranslationX(mPosX);
                                //vista.setTranslationY(mPosY);

                                vista.invalidate();

                                mLastTouchX = x;
                                mLastTouchY = y;
                            }
                        } else {

                            final float x = event.getX();
                            final float y = event.getY();
                            vista.invalidate();

                            mLastTouchX = x;
                            mLastTouchY = y;

                            //final float gx = mScaleDetector.getFocusX();
                            //final float gy = mScaleDetector.getFocusY();

                            //final float gdx = gx - mLastGestureX;
                            //final float gdy = gy - mLastGestureY;

                            //final int pointerIndex = MotionEventCompat.getActionIndex(event);
                            //final float x = event.getX(pointerIndex);
                            //final float y = event.getY(pointerIndex);


                            //mPosX += gdx;
                            //mPosY += gdy;
                        /*
                        Log.i("hola", "Escalado: " + String.valueOf(mScaleDetector.getFocusX()) +
                                " " + String.valueOf(mScaleDetector.getFocusY()));
                        vista.setPivotX(mScaleDetector.getFocusX());
                        vista.setPivotY(mScaleDetector.getFocusY());
                        vista.setScaleX(mScaleFactor);
                        vista.setScaleY(mScaleFactor);
                        */

                            //mLastGestureX = gx;
                            //mLastGestureY = gy;

                        }
                    }
                    break;
                }

                case MotionEvent.ACTION_UP: {
                    mode=NONE;
                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    mode=NONE;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {
                    mode=NONE;
                    break;

                }
            }

            vista.invalidate();
        }


        vista.invalidate();
        return touchFueraVista;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_ki67_obtener_color,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.guardar3:
                guardar(null);
                break;
            case R.id.colorProliferantes:

                totalMuestras=0;

                r=0;
                g=0;
                b=0;


                okBotonPresionado=true;
                nuevoBoton.setVisibility(View.VISIBLE);
                okBoton.setVisibility(View.INVISIBLE);
                cancelarBoton.setVisibility(View.INVISIBLE);

                vista.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                vista.buildDrawingCache();
                vista.setDrawingCacheEnabled(true);
                bitmapColor=vista.getDrawingCache();

                matrizColor=new Mat();
                Utils.bitmapToMat(bitmapColor,matrizColor);

                obtenerColorP=true;
                obtenerColorNP=false;
                obtenerColorEE=false;

                textV.setText("NP");
                break;

            case R.id.colorNProliferantes:

                totalMuestras=0;

                r=0;
                g=0;
                b=0;

                okBotonPresionado=true;
                nuevoBoton.setVisibility(View.VISIBLE);
                okBoton.setVisibility(View.INVISIBLE);
                cancelarBoton.setVisibility(View.INVISIBLE);

                vista.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                vista.buildDrawingCache();
                vista.setDrawingCacheEnabled(true);
                bitmapColor=vista.getDrawingCache();

                matrizColor=new Mat();
                Utils.bitmapToMat(bitmapColor,matrizColor);

                bitmapColor=vista.getDrawingCache();
                obtenerColorNP=true;
                obtenerColorP=false;
                obtenerColorEE=false;

                textV.setText("NNP");
                break;

            case R.id.colorEE:

                totalMuestras=0;

                r=0;
                g=0;
                b=0;

                okBotonPresionado=true;
                nuevoBoton.setVisibility(View.VISIBLE);
                okBoton.setVisibility(View.INVISIBLE);
                cancelarBoton.setVisibility(View.INVISIBLE);
                vista.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                vista.buildDrawingCache();
                vista.setDrawingCacheEnabled(true);
                bitmapColor=vista.getDrawingCache();

                matrizColor=new Mat();
                Utils.bitmapToMat(bitmapColor,matrizColor);

                obtenerColorNP=false;
                obtenerColorP=false;
                obtenerColorEE=true;

                textV.setText("EE");
                break;
        }
        vista.invalidate();
        return super.onOptionsItemSelected(item);
    }

    //Cuando se presiona atrás se recrea la vista previa
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("destruido",true);

        outState.putInt("color",color);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}
