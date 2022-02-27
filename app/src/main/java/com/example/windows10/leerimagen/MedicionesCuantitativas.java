package com.example.windows10.leerimagen;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.SharedPreferencesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MedicionesCuantitativas extends AppCompatActivity {

    //TODO ver el tema del pivot colocarlo en down o en otro lado.(Solucionado).
    //TODO Ver como mostrar la distancia medida en la opcion "medir".(Solucionado).
    //TODO Probar extender la vista a un ImageView.(probado medianamente).
    //TODO Agregar Guardado.(Practicamente solucionado).
    //TODO Mejorar la configuracion, que se permitan numeros de punto flotante.(Practicamente solucionado).
    //TODO Lograr que muestre la recta luego del escalado automático y se seleccione el mayor.(practicamente solucionado).
    //TODO Mejorar el pivot cuando hay traslación.(parcialmente solucionado)
    //TODO Agrandar y achicar regla de medición.(Solucionado)
    //TODO Mostrar las configuraciones seteadas en la parte inferior.(Algo solucionado)
    //TODO ver focus previo que esta multiplicado(visto)
    //TODO Agregar varias mediciones.
    //TODO Ver de mejorar la precisión del escalado, utilizando las coordenadas "x" e "y" del tipo float.(Practicamente solucionado).
    //TODO Verificar que sucede con las actividades en segundo plano como el resuelto en la actividad CortarImagen.
    //TODO Agregar sonido al click.
    //TODO Agregar boton "Ok" para guardar la calibración(parcialmente realizado, solo para calibración manual).
    //TODO Agregar boton "Cancelar" para no guardar calibración(parcialmente realizado, solo para calibración manual).
    //TODO Ir mostrando los angulos en la medicion y calibración manual.(practicamente resuelto).

    CoordinatorLayout linearLayout;
    vista vista;
    Bitmap bitmapImage;
    Bitmap bitmapModificado;
    Bitmap bitmapBuffer;
    Bitmap bitmapRecortado;
    Path pathRecuperado;
    Matrix matrixBuffer;
    BigDecimal bd;

    ArrayList<Integer> pixeles;
    int pixelesMayores;

    boolean medir=false;
    boolean calibrado=false;
    boolean calibradoRealizado=false;
    boolean relacion=false;
    boolean touchDentroVista=false;
    boolean touchFueraVista=true;

    Paint paintMedir;

    int ancho=0;
    int alto=0;
    int altoInicial=0;
    int altoFinal=0;
    int anchoInicial=0;
    int anchoFinal=0;
    int aumentoPreferencias;
    float patronPreferencias;
    float distanciaReal=0.0f;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private GestureDetectorCompat mDetector;
    //int pixels=0;

    float escala=0;
    float R1,R2;
    boolean zoom=true;
    float xOriginalScale;
    float yOriginalScale;
    private int mActivePointerId;
    int INVALID_POINTER_ID=-1;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX;
    private float mPosY;
    private float mLastGestureX;
    private float mLastGestureY;

    ///////////////////////////////////////////
    Matrix matrix;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;

    int mode = NONE;

    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1.f;
    float maxScale = 15.f;
    float[] m;
    int viewWidth, viewHeight;

    static final int CLICK = 3;

    float saveScale = 1.f;

    protected float origWidth, origHeight;

    int oldMeasuredWidth, oldMeasuredHeight;
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
    float saveFocusX;
    float saveFocusY;
    int calibradoIncialX=-1;
    int calibradoInicialY=-1;
    int calibradoFinalX;
    int calibradoFinalY;
    boolean bandera=false;
    boolean banderaDos=false;
    int[] calibradoMaximo;
    int tamañoMax;
    int tamaño;
    int indiceDoubleTap=0;
    float origScale;
    float focusEscaladoX;
    float focusEscaladoY;
    ////////////////////////////////////////////

    private ProgressDialog pDialog;
    private MiTareaAsincronaDialog miTareaAsincronaDialog;
    TextView O,P,M;
    private boolean configuracion=false;
    private boolean restart=false;
    private boolean medicionRealizada=false;
    float xMedirInicial;
    float yMedirInicial;
    float xMedirFinal;
    float yMedirFinal;
    String accion=null;
    private boolean modoMedir=false;
    private boolean modoZoom=false;
    private boolean modoCalibradoManual=false;
    private boolean modoCalibradoAutomatico=false;
    private boolean reglaSelec=false;
    float densidad;
    private ImageButton addBoton;
    private FrameLayout frameLayout;
    private RelativeLayout linearLayout2;
    private ImageButton removeBoton;
    float tg;
    private ImageView rotateRight;
    private double angulo=0;
    private double moduloRecta;
    private double cop;
    private double cady;
    private ImageButton rotateLeft;
    private boolean obtenerColor=false;
    private float xColor;
    private float yColor;
    private Bitmap bitmapColor;
    private int rMax=0;
    private int rMin=0;
    private int gMax=0;
    private int gMin=0;
    private int bMax=0;
    private int bMin=0;
    private Button guardarColores;
    /////////////////////////////////////////////////////
    private ImageButton zoomBoton;
    private LinearLayout linearLayoutModo;
    private ImageButton medirBoton;
    private ImageButton calibrarBotonA;
    private ImageButton calibrarBotonM;
    private Button agregarBoton;
    private ImageButton obtenerColorBoton;
    private String configuraciones="";
    private ImageButton listoBoton;
    private ImageButton cancelarBoton;
    ////////////////////////////////////////
    private View decorView;
    public static ArrayList<String> nombresConfiguraciones=new ArrayList<String>();
    private String linea="";
    private int divisionImagen;

    public static final String TAG="MainActivityPrincipal";

    static{
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"Cargo exitosamente :)");
        }else
            Log.d(TAG,"No pudo cargar el paquete openCV :(");
    }

    private Mat imagenMatriz;
    private Mat imagenProcesada;
    private ArrayList<Mat> clusters;
    private int indice1,indice2,indice3,indice4;
    private float aumentoRecta;
    private float distancia;
    private float disminuyeRecta;
    private TextView cali;
    private TextView a;
    private LinearLayout LL;
    private LinearLayout linearLayoutCalibracion;
    private LinearLayout linearLayoutMedicion;
    private int aumentoMedicion;
    private float inc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediciones_cuantitativas);


        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        densidad = getResources().getDisplayMetrics().density;

        linearLayout = (CoordinatorLayout)findViewById(R.id.linearLayout1);
        LL = (LinearLayout)findViewById(R.id.LL);
        //linearLayout2=(LinearLayout)findViewById(R.id.linearLayout2);
        frameLayout  = (FrameLayout)findViewById(R.id.frame1);

//        O=(TextView)findViewById(R.id.o);
//        P=(TextView)findViewById(R.id.p);
//        cali=(TextView)findViewById(R.id.cali);
//        a=(TextView)findViewById(R.id.a);

        vista=new vista(this);
        vista.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));


        controlesModo();

        controlesMedicionCalibracion();

        controlesToolbar();

        aplicarZoom();

        frameLayout.addView(vista);
        frameLayout.addView(linearLayout2);
        frameLayout.addView(linearLayoutModo);

        linearLayout2.setVisibility(View.INVISIBLE);

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        mostrarEnToolbar();

//        SharedPreferences prefs =
//                getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
//        rMax=prefs.getInt("rMax",0);
//        rMin=prefs.getInt("rMin",0);
//        gMax=prefs.getInt("gMax",0);
//        gMin=prefs.getInt("gMin",0);
//        bMax=prefs.getInt("bMax",0);
//        bMin=prefs.getInt("bMin",0);

        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
        rMax=Integer.parseInt(preferences.getString("rMax","-1"));
        rMin=Integer.parseInt(preferences.getString("rMin","-1"));
        gMax=Integer.parseInt(preferences.getString("gMax","-1"));
        gMin=Integer.parseInt(preferences.getString("gMin","-1"));
        bMax=Integer.parseInt(preferences.getString("bMax","-1"));
        bMin=Integer.parseInt(preferences.getString("bMin","-1"));

//        SharedPreferences sharedPreferencesPrevia=getPreferences(MODE_PRIVATE);
//        Map map=sharedPreferencesPrevia.getAll();
//
//        for(String configuraciones:nombresConfiguraciones){
//            nombresConfiguraciones.add(String.valueOf(sharedPreferencesPrevia.getInt("aumento",-1)) +" "+
//                    String.valueOf(sharedPreferencesPrevia.getFloat("patron",-1)) +" "+
//                    String.valueOf(sharedPreferencesPrevia.getFloat("escala",-1)));
//
//        }


//Ver el tema de las preferencias que se cree los archivos!!
//        try
//        {
//            File ruta_sd = getExternalFilesDir(null);
//            File f = new File(ruta_sd, "configuraciones.txt");
//            BufferedReader fin =
//                    new BufferedReader(
//                            new InputStreamReader(
//                                    new FileInputStream(f)));
//            linea = fin.readLine();
//            while(linea!=null) {
//                configuraciones=configuraciones+linea+"\n";
//                linea = fin.readLine();
////                lista.add(linea);
//            }
//
//            fin.close();
//
//        }
//        catch (Exception ex)
//        {
//            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
//        }
    }

    private void controlesToolbar() {
        linearLayoutCalibracion = new LinearLayout(this);
        linearLayoutCalibracion.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayoutCalibracion.setPadding((int) (10*densidad),0,0,0);
        linearLayoutCalibracion.setOrientation(LinearLayout.HORIZONTAL);

        cali=new TextView(this);
        O=new TextView(this);
        P=new TextView(this);

        cali.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        O.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        O.setPadding((int)(10*densidad),0,0,0);
        P.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        P.setPadding((int)(10*densidad),0,0,0);

        linearLayoutCalibracion.addView(cali);
        linearLayoutCalibracion.addView(O);
        linearLayoutCalibracion.addView(P);

//        LL.addView(linearLayoutCalibracion);

        linearLayoutMedicion = new LinearLayout(this);
        linearLayoutMedicion .setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayoutMedicion.setPadding((int) (10*densidad),0,0,0);
        linearLayoutMedicion .setOrientation(LinearLayout.HORIZONTAL);

        a=new TextView(this);
        a.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        linearLayoutMedicion.addView(a);
    }

    private void controlesModo() {
        linearLayoutModo =new LinearLayout(this);
        linearLayoutModo.setPadding((int)(15*densidad),(int)(15*densidad),(int)(15*densidad),(int)(15*densidad));
        linearLayoutModo.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.TOP|Gravity.START));
        linearLayoutModo.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutModo.setBackgroundResource(R.drawable.degradado_bar_opcional_invertido);

        LinearLayout.LayoutParams layoutParamsZoom=new LinearLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsZoom.setMarginEnd((int)(15*densidad));

        zoomBoton=new ImageButton(this);
        zoomBoton.setId(R.id.zoomBoton);
//        zoomBoton.setPadding((int)(5*densidad),(int)(5*densidad),(int)(5*densidad),(int)(5*densidad));
        zoomBoton.setBackgroundResource(R.drawable.boton_botones_modo);
        zoomBoton.setImageResource(R.drawable.ic_zoom_in_white_36dp);
        zoomBoton.setLayoutParams(layoutParamsZoom);

        medirBoton=new ImageButton(this);
        medirBoton.setPadding((int)(5*densidad),(int)(5*densidad),(int)(5*densidad),(int)(5*densidad));
        medirBoton.setId(View.generateViewId());
        medirBoton.setBackgroundResource(R.drawable.boton_botones_modo);
        medirBoton.setImageResource(R.drawable.ic_action_medir);
        medirBoton.setLayoutParams(layoutParamsZoom);

        calibrarBotonA=new ImageButton(this);
        calibrarBotonA.setId(View.generateViewId());
        calibrarBotonA.setBackgroundResource(R.drawable.boton_botones_modo);
        calibrarBotonA.setImageResource(R.drawable.ic_calibracion_automatica);
        calibrarBotonA.setLayoutParams(layoutParamsZoom);

        calibrarBotonM=new ImageButton(this);
        calibrarBotonM.setId(View.generateViewId());
        calibrarBotonM.setBackgroundResource(R.drawable.boton_botones_modo);
        calibrarBotonM.setImageResource(R.drawable.ic_calibracion_manual);
        calibrarBotonM.setLayoutParams(layoutParamsZoom);

//        obtenerColorBoton=new ImageButton(this);
//        obtenerColorBoton.setId(View.generateViewId());
//        obtenerColorBoton.setBackgroundResource(R.drawable.boton_botones_modo);
//        obtenerColorBoton.setImageResource(R.drawable.ic_action_obtener_color);
//        obtenerColorBoton.setLayoutParams(layoutParamsZoom);

        linearLayoutModo.addView(zoomBoton);
        linearLayoutModo.addView(medirBoton);
//        linearLayoutModo.addView(calibrarBotonA);
        linearLayoutModo.addView(calibrarBotonM);
//        linearLayoutModo.addView(obtenerColorBoton);

        zoomBoton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
               aplicarZoom();
            }
        });
        medirBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!medirBoton.isActivated()) {
                    if (LL.getChildCount() != 0)
                        LL.removeView(linearLayoutCalibracion);
                    LL.addView(linearLayoutMedicion);
                }
                medicion();
            }
        });
//        calibrarBotonA.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calibracionAutomatica();
//            }
//        });
        calibrarBotonM.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if(!calibrarBotonM.isActivated()) {
                    if (LL.getChildCount() != 0)
                        LL.removeView(linearLayoutMedicion);
                    LL.addView(linearLayoutCalibracion);
                }
                calibracionManual();
//                calibrarBotonM.setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
            }
        });
//        obtenerColorBoton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    extraerColor();
//            }
//        });
    }

    private void controlesMedicionCalibracion() {

        linearLayout2=new RelativeLayout(this);
        linearLayout2.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.BOTTOM));
        linearLayout2.setPadding((int)(10*densidad),(int)(10*densidad),(int)(10*densidad),(int)(10*densidad));
        //linearLayout2.setGravity(Gravity.BOTTOM);
        linearLayout2.setBackgroundResource(R.drawable.degradado_bar_options);

        Drawable drawable=getResources().getDrawable(R.drawable.boton_mas);
        Drawable drawable2=getResources().getDrawable(R.drawable.boton_mas);
        Drawable drawable3=getResources().getDrawable(R.drawable.boton_mas);
        Drawable drawable4=getResources().getDrawable(R.drawable.boton_mas);
        Drawable drawable5=getResources().getDrawable(R.drawable.boton_mas);
        //final FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(80,80, Gravity.START|Gravity.BOTTOM);
        RelativeLayout.LayoutParams layoutParamsAdd=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
//        layoutParamsAdd.setMargins((int)(10*densidad),(int)(20*densidad),(int)(10*densidad),(int)(20*densidad));
        layoutParamsAdd.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        layoutParamsAdd.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);
        layoutParamsAdd.setMarginEnd((int)(15*densidad));



//        final LinearLayoutCompat.LayoutParams layoutParamsButton=new LinearLayoutCompat.LayoutParams((int)(50*densidad),(int)(50*densidad));
//        layoutParamsButton.setMargins((int)(30*densidad),(int)(20*densidad),(int)(10*densidad),(int)(20*densidad));
        Log.i("densidad","densidad: " + String.valueOf(densidad));


        addBoton=new ImageButton(this);
        addBoton.setId(View.generateViewId());
        //addBoton.setPadding(100,100,100,100);
        addBoton.setBackground(drawable);
        addBoton.setImageResource(R.drawable.ic_add);
        addBoton.setLayoutParams(layoutParamsAdd);

        RelativeLayout.LayoutParams layoutParamsBotones1=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsBotones1.addRule(RelativeLayout.ALIGN_BOTTOM,addBoton.getId());
        layoutParamsBotones1.addRule(RelativeLayout.END_OF,addBoton.getId());
//        layoutParamsBotones1.addRule(RelativeLayout.ALIGN_END,addBoton.getId());
        layoutParamsBotones1.setMarginEnd((int)(15*densidad));

        removeBoton=new ImageButton(this);
        removeBoton.setId(View.generateViewId());
        //addBoton.setPadding(100,100,100,100);
        removeBoton.setBackground(drawable2);
        removeBoton.setImageResource(R.drawable.ic_remove);
        removeBoton.setLayoutParams(layoutParamsBotones1);

        RelativeLayout.LayoutParams layoutParamsBotones2=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsBotones2.addRule(RelativeLayout.ALIGN_BOTTOM,removeBoton.getId());
        layoutParamsBotones2.addRule(RelativeLayout.END_OF,removeBoton.getId());
//        layoutParamsBotones2.addRule(RelativeLayout.ALIGN_END,removeBoton.getId());
        layoutParamsBotones2.setMarginEnd((int)(15*densidad));

        rotateRight=new ImageButton(this);
        rotateRight.setId(View.generateViewId());
        rotateRight.setBackground(drawable3);
        rotateRight.setImageResource(R.drawable.ic_rotate_right_white_24dp);
        rotateRight.setLayoutParams(layoutParamsBotones2);
//        rotateRight.setSoundEffectsEnabled(true);
        Log.i("efecto", String.valueOf(rotateRight.isSoundEffectsEnabled()));

        RelativeLayout.LayoutParams layoutParamsBotones3=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsBotones3.addRule(RelativeLayout.ALIGN_BOTTOM,rotateRight.getId());
        layoutParamsBotones3.addRule(RelativeLayout.END_OF,rotateRight.getId());
//        layoutParamsBotones3.addRule(RelativeLayout.ALIGN_END,rotateRight.getId());
        layoutParamsBotones3.setMarginEnd((int)(15*densidad));

        rotateLeft=new ImageButton(this);
        rotateLeft.setId(View.generateViewId());
        rotateLeft.setBackground(drawable4);
        rotateLeft.setImageResource(R.drawable.ic_rotate_left_white_24dp);
        rotateLeft.setLayoutParams(layoutParamsBotones3);

        obtenerColorBoton=new ImageButton(this);
        obtenerColorBoton.setId(View.generateViewId());
        obtenerColorBoton.setBackgroundResource(R.drawable.boton_mas);
        obtenerColorBoton.setImageResource(R.drawable.ic_action_obtener_color);
        obtenerColorBoton.setLayoutParams(layoutParamsBotones3);

        RelativeLayout.LayoutParams layoutParamsNuevo=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsNuevo.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
        layoutParamsNuevo.addRule(RelativeLayout.ALIGN_PARENT_END,RelativeLayout.TRUE);
//        layoutParamsNuevo.addRule(RelativeLayout.ALIGN_BOTTOM,rotateLeft.getId());

        agregarBoton=new Button(this);
        agregarBoton.setId(View.generateViewId());
        agregarBoton.setBackgroundResource(R.drawable.boton_mas);
        agregarBoton.setText("Nuevo");
        agregarBoton.setTextSize(3.5f*densidad);
        agregarBoton.setLayoutParams(layoutParamsNuevo);

        listoBoton=new ImageButton(this);
        listoBoton.setBackgroundResource(R.drawable.boton_mas);
        listoBoton.setImageResource(R.drawable.ic_ok);
        listoBoton.setLayoutParams(layoutParamsNuevo);

        RelativeLayout.LayoutParams layoutParamsCancelarBoton=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsCancelarBoton.addRule(RelativeLayout.ALIGN_BOTTOM,agregarBoton.getId());
        layoutParamsCancelarBoton.addRule(RelativeLayout.START_OF,agregarBoton.getId());
//        layoutParamsBotones3.addRule(RelativeLayout.ALIGN_END,rotateRight.getId());
        layoutParamsCancelarBoton.setMarginEnd((int)(15*densidad));

        cancelarBoton=new ImageButton(this);
        cancelarBoton.setBackgroundResource(R.drawable.boton_mas);
        cancelarBoton.setImageResource(R.drawable.ic_action_text_close);
        cancelarBoton.setLayoutParams(layoutParamsCancelarBoton);


//        rotateLeft.playSoundEffect(SoundEffectConstants.CLICK);
//        rotateLeft.setSoundEffectsEnabled(true);

//        guardarColores=new Button(this);
//        guardarColores.setLayoutParams(layoutParamsButton);
//        guardarColores.setBackground(drawable5);
//        guardarColores.setTextColor(Color.WHITE);
//        guardarColores.setTextSize(10);
//        guardarColores.setText("Obtener");

//        linearLayout2.setBackgroundResource(R.drawable.degradado_bar_options);

        linearLayout2.addView(agregarBoton);
        linearLayout2.addView(addBoton);
        linearLayout2.addView(removeBoton);
        linearLayout2.addView(rotateRight);
        linearLayout2.addView(rotateLeft);
        linearLayout2.addView(obtenerColorBoton);
        linearLayout2.addView(listoBoton);
        linearLayout2.addView(cancelarBoton);

        listoBoton.setVisibility(View.INVISIBLE);
        cancelarBoton.setVisibility(View.INVISIBLE);
//        linearLayout2.addView(agregarBoton);
//        linearLayout2.addView(guardarColores);
//        linearLayout2.addView(agregarBoton);
//        linearLayout2.addView(guardarColores);


        addBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int cop=Math.abs(yMedirFinal-yMedirInicial);
                //int cad=Math.abs(xMedirFinal-xMedirInicial);


                //Log.i("boton",String.valueOf(cop)+ " " + String.valueOf(cad));
                if(!calibrarBotonM.isActivated())
                    aumentoRecta=(distanciaReal+inc)/escala;
                else
                    aumentoRecta=distancia+1f;


                if(xMedirFinal-xMedirInicial > 0) {
                    if(Math.abs(yMedirFinal-yMedirInicial) > Math.abs(xMedirFinal-xMedirInicial)){
                        if((yMedirFinal-yMedirInicial)>0) {
//                            yMedirFinal = yMedirFinal + 2;
//                            xMedirFinal = xMedirFinal + (2 / tg);
                            yMedirFinal = yMedirInicial + (float) (aumentoRecta*Math.sin(angulo));
                            xMedirFinal = xMedirInicial + (float) (aumentoRecta*Math.cos(angulo));
                        }else{
//                            yMedirFinal = yMedirFinal - 2;
//                            xMedirFinal = xMedirFinal - (2 / tg);
                            yMedirFinal = yMedirInicial + (float) (aumentoRecta*Math.sin(angulo));
                            xMedirFinal = xMedirInicial + (float) (aumentoRecta*Math.cos(angulo));
                        }
                    }else{
                        Log.i("boton>0", String.format("%.8f", tg));
//                        xMedirFinal = xMedirFinal + 2;
//                        yMedirFinal = yMedirFinal + (2 * tg);
                        yMedirFinal = yMedirInicial + (float) (aumentoRecta*Math.sin(angulo));
                        xMedirFinal = xMedirInicial + (float) (aumentoRecta*Math.cos(angulo));
                    }
                }else if(xMedirFinal-xMedirInicial < 0){
                    if(Math.abs(yMedirFinal-yMedirInicial) > Math.abs(xMedirFinal-xMedirInicial)){
                        if((yMedirFinal-yMedirInicial)>0) {
//                            yMedirFinal = yMedirFinal + 2;
//                            xMedirFinal = xMedirFinal + (2 / tg);
                            yMedirFinal = yMedirInicial - (float) (aumentoRecta*Math.sin(angulo));
                            xMedirFinal = xMedirInicial - (float) (aumentoRecta*Math.cos(angulo));
                        }else{
//                            yMedirFinal = yMedirFinal - 2;
//                            xMedirFinal = xMedirFinal - (2 / tg);
                            yMedirFinal = yMedirInicial - (float) (aumentoRecta*Math.sin(angulo));
                            xMedirFinal = xMedirInicial - (float) (aumentoRecta*Math.cos(angulo));
                        }
                    }else{
                        Log.i("boton<0", String.format("%.8f", tg));
//                        xMedirFinal = xMedirFinal - 2;
//                        yMedirFinal = yMedirFinal - (2 * tg);
                        yMedirFinal = yMedirInicial - (float) (aumentoRecta*Math.sin(angulo));
                        xMedirFinal = xMedirInicial - (float) (aumentoRecta*Math.cos(angulo));
                    }
                }
                moduloRecta=Math.sqrt((yMedirFinal-yMedirInicial)*(yMedirFinal-yMedirInicial)
                        +(xMedirFinal-xMedirInicial)*(xMedirFinal-xMedirInicial));
                Log.i("boton",String.valueOf(yMedirFinal)+ " " + String.valueOf(xMedirFinal));
                vista.invalidate();
            }
        });

        removeBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!calibrarBotonM.isActivated())
                    disminuyeRecta=(distanciaReal-inc)/escala;
                else
                    disminuyeRecta=distancia-1f;

                if(xMedirFinal-xMedirInicial > 0) {
                    if(Math.abs(yMedirFinal-yMedirInicial) > Math.abs(xMedirFinal-xMedirInicial)){
                        if((yMedirFinal-yMedirInicial)>0) {
//                            yMedirFinal = yMedirFinal - 2;
//                            xMedirFinal = xMedirFinal - (2 / tg);
                            yMedirFinal = yMedirInicial + (float) (disminuyeRecta*Math.sin(angulo));
                            xMedirFinal = xMedirInicial + (float) (disminuyeRecta*Math.cos(angulo));
                        }else{
//                            yMedirFinal = yMedirFinal + 2;
//                            xMedirFinal = xMedirFinal + (2 / tg);
                            yMedirFinal = yMedirInicial + (float) (disminuyeRecta*Math.sin(angulo));
                            xMedirFinal = xMedirInicial + (float) (disminuyeRecta*Math.cos(angulo));
                        }
                    }else{
                        Log.i("boton>0", String.format("%.8f", tg));
//                        xMedirFinal = xMedirFinal - 2;
//                        yMedirFinal = yMedirFinal - (2 * tg);
                        yMedirFinal = yMedirInicial + (float) (disminuyeRecta*Math.sin(angulo));
                        xMedirFinal = xMedirInicial + (float) (disminuyeRecta*Math.cos(angulo));
                    }
                }else if(xMedirFinal-xMedirInicial < 0){
                    if(Math.abs(yMedirFinal-yMedirInicial) > Math.abs(xMedirFinal-xMedirInicial)){
                        if((yMedirFinal-yMedirInicial)>0) {
//                            yMedirFinal = yMedirFinal - 2;
//                            xMedirFinal = xMedirFinal - (2 / tg);
                            yMedirFinal = yMedirInicial - (float) (disminuyeRecta*Math.sin(angulo));
                            xMedirFinal = xMedirInicial - (float) (disminuyeRecta*Math.cos(angulo));
                        }else{
//                            yMedirFinal = yMedirFinal + 2;
//                            xMedirFinal = xMedirFinal + (2 / tg);
                            yMedirFinal = yMedirInicial - (float) (disminuyeRecta*Math.sin(angulo));
                            xMedirFinal = xMedirInicial - (float) (disminuyeRecta*Math.cos(angulo));
                        }
                    }else{
                        Log.i("boton<0", String.format("%.8f", tg));
//                        xMedirFinal = xMedirFinal + 2;
//                        yMedirFinal = yMedirFinal + (2 * tg);
                        yMedirFinal = yMedirInicial - (float) (disminuyeRecta*Math.sin(angulo));
                        xMedirFinal = xMedirInicial - (float) (disminuyeRecta*Math.cos(angulo));
                    }
                }
                moduloRecta=Math.sqrt((yMedirFinal-yMedirInicial)*(yMedirFinal-yMedirInicial)
                        +(xMedirFinal-xMedirInicial)*(xMedirFinal-xMedirInicial));
                Log.i("boton",String.valueOf(yMedirFinal-yMedirInicial)+ " " + String.valueOf(xMedirFinal-xMedirInicial));
                vista.invalidate();
            }
        });

        rotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rotateRight.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
                angulo=angulo+Math.PI/1800;
                Log.i("AnguloBotonDerechoA", String.valueOf(Math.abs(angulo)));

                if(Math.abs(angulo*(180/Math.PI))>0 && Math.abs(angulo*(180/Math.PI))<90) {
                    rotateLeft.setClickable(true);
                    Log.i("AnguloBotonDerechoA", String.valueOf(Math.abs(angulo)));
                    Log.i("AnguloBotonDerecho", String.valueOf(angulo * (180 / Math.PI)));
                    tg = (float) Math.tan(angulo);
                    Log.i("mBotonR", String.valueOf((yMedirFinal - yMedirInicial)) + " " + String.valueOf((xMedirFinal - xMedirInicial)));
                    if ((yMedirFinal - yMedirInicial) < 0 && (xMedirFinal - xMedirInicial) > 0) {
                        cop = moduloRecta * Math.sin(angulo);
                        cady = moduloRecta * Math.cos(angulo);
                    } else if ((yMedirFinal - yMedirInicial) > 0 && (xMedirFinal - xMedirInicial) < 0) {
                        cop = -moduloRecta * Math.sin(angulo);
                        cady = -moduloRecta * Math.cos(angulo);
                    } else if ((yMedirFinal - yMedirInicial) > 0 && (xMedirFinal - xMedirInicial) > 0) {
                        cop = moduloRecta * Math.sin(angulo);
                        cady = moduloRecta * Math.cos(angulo);
                    } else if ((yMedirFinal - yMedirInicial) < 0 && (xMedirFinal - xMedirInicial) < 0) {
                        Log.i("mDerecha", "Entro derecho");
                        cop = -moduloRecta * Math.sin(angulo);
                        cady = -moduloRecta * Math.cos(angulo);
                    }
                    yMedirFinal = (float) cop + yMedirInicial;
                    xMedirFinal = (float) cady + xMedirInicial;
                }else{
                    rotateRight.setClickable(false);
                }
                Log.i("botonRotateR",String.valueOf(angulo) + " " + String.valueOf(cady) + " " + String.valueOf(cop));
                vista.invalidate();
            }
        });

        rotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                angulo=angulo-Math.PI/1800;
                Log.i("AnguloBotonIzquierdoA", String.valueOf(Math.abs(angulo)));
                if(Math.abs(angulo*(180/Math.PI))>0 && Math.abs(angulo*(180/Math.PI))<90) {
                    rotateRight.setClickable(true);
                    Log.i("AnguloBotonIzquierdoA", String.valueOf(Math.abs(angulo)));
                    Log.i("AnguloBotonIzquierdo", String.valueOf(angulo * (180 / Math.PI)));
                    tg = (float) Math.tan(angulo);
                    Log.i("mBotonL", String.valueOf((yMedirFinal - yMedirInicial)) + " " + String.valueOf((xMedirFinal - xMedirInicial)));
                    if (yMedirFinal - yMedirInicial < 0 && (xMedirFinal - xMedirInicial) > 0) {
                        cop = moduloRecta * Math.sin(angulo);
                        cady = moduloRecta * Math.cos(angulo);
                    } else if ((yMedirFinal - yMedirInicial) > 0 && (xMedirFinal - xMedirInicial) < 0) {
                        cop = -moduloRecta * Math.sin(angulo);
                        cady = -moduloRecta * Math.cos(angulo);
                    } else if ((yMedirFinal - yMedirInicial) > 0 && (xMedirFinal - xMedirInicial) > 0) {
                        cop = moduloRecta * Math.sin(angulo);
                        cady = moduloRecta * Math.cos(angulo);
                    } else if ((yMedirFinal - yMedirInicial) < 0 && (xMedirFinal - xMedirInicial) < 0) {
                        Log.i("mIzquierda", "Entro izquierda");
                        cop = -moduloRecta * Math.sin(angulo);
                        cady = -moduloRecta * Math.cos(angulo);
                    }

                    yMedirFinal = (float) cop + yMedirInicial;
                    xMedirFinal = (float) cady + xMedirInicial;
                }else{
                    rotateLeft.setClickable(false);
                }
                Log.i("botonRotateL",String.valueOf(angulo) + " " + String.valueOf(cady) + " " + String.valueOf(cop));
                vista.invalidate();
            }
        });
        agregarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText entrada=new EditText(MedicionesCuantitativas.this);
                final SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(MedicionesCuantitativas.this);
                final SharedPreferences.Editor editor=sharedPreferences.edit();

                if(relacion) {

                    if(zoomBoton.isActivated())
                        calibracionManual();


                        final AlertDialog.Builder builder=new AlertDialog.Builder(MedicionesCuantitativas.this);
                        builder.setView(entrada);
                        builder.setTitle("¿Quieres agregar el nombre del microscopio?");
                        builder.setMessage("Se recomienda para cada calibración");

                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putString("microscopio",entrada.getText().toString());
                                editor.apply();
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    medicionRealizada = false;

                    addBoton.setVisibility(View.VISIBLE);
                    removeBoton.setVisibility(View.VISIBLE);
                    rotateRight.setVisibility(View.VISIBLE);
                    rotateLeft.setVisibility(View.VISIBLE);
                    obtenerColorBoton.setVisibility(View.INVISIBLE);
                    listoBoton.setVisibility(View.VISIBLE);
                    cancelarBoton.setVisibility(View.VISIBLE);
                    agregarBoton.setVisibility(View.INVISIBLE);

                    xMedirInicial=0;
                    xMedirFinal=0;
                    yMedirInicial=0;
                    yMedirFinal=0;

                    vista.postInvalidate();

                }else if(medir){
                    if(escala!=0 && !Double.isInfinite(escala)) {
                        if(zoomBoton.isActivated())
                            medicion();
                        medicionRealizada=false;
                        xMedirInicial=0;
                        xMedirFinal=0;
                        yMedirInicial=0;
                        yMedirFinal=0;
                        angulo=0;

                    }else{
                        Toast.makeText(MedicionesCuantitativas.this,
                                "Primero obtenga la escala o vuelva a realizarlo...",Toast.LENGTH_SHORT).show();
                    }
                    vista.postInvalidate();
                }else if(obtenerColorBoton.isActivated()){
                    obtenerColor=true;
                }else if(calibrarBotonA.isActivated()){
                    realizarCalibracionAutomatica();
                }
            }
        });
//        obtenerColorBoton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                extraerColor();
//            }
//        });
        listoBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relacion){

                    final SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(MedicionesCuantitativas.this);
                    final SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("aumentoMedicion",String.valueOf(aumentoPreferencias));
                    editor.apply();

                    a.setText("Aumento: "+String.valueOf(aumentoPreferencias)+"X");

                    listoBoton.setSelected(true);
                    addBoton.setVisibility(View.INVISIBLE);
                    removeBoton.setVisibility(View.INVISIBLE);
                    rotateRight.setVisibility(View.INVISIBLE);
                    rotateLeft.setVisibility(View.INVISIBLE);
                    obtenerColorBoton.setVisibility(View.INVISIBLE);
                    listoBoton.setVisibility(View.INVISIBLE);
                    cancelarBoton.setVisibility(View.INVISIBLE);
                    agregarBoton.setVisibility(View.VISIBLE);
                }
                vista.invalidate();
            }
        });
        cancelarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relacion){
                    calibracionManual();
                    accion=null;
                    xMedirInicial=0;
                    xMedirFinal=0;
                    yMedirInicial=0;
                    yMedirFinal=0;
                }
            }
        });
        vista.invalidate();
    }

    private void mostrarEnToolbar() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        aumentoPreferencias           = Integer.parseInt(preferences.getString("aumento",""));
        patronPreferencias     = Float.parseFloat(preferences.getString("patron",""));
        inc = Float.parseFloat(preferences.getString("incremento",""));

        if(escala!=0 && !Float.isInfinite(escala)){
            cali.setText("Calibrado");
        }else
            cali.setText("No calibrado");

        O.setText("O: " + String.valueOf(aumentoPreferencias)+"X");
        P.setText("P: " + String.valueOf(patronPreferencias)+"um");
        a.setText("Aumento: " + String.valueOf(aumentoMedicion)+"X");

    }

    public void guardar(View view){

        linearLayout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        linearLayout.buildDrawingCache();
        bitmapBuffer=linearLayout.getDrawingCache();

//        if(vista.getScaleX() == 1 && vista.getScaleY() == 1) {
//            if (ancho != 0 || alto != 0) {
//                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
//                    bitmapRecortado = Bitmap.createBitmap(bitmapBuffer, 0, 0, ancho, alto);
//
//                } else {
//                    if ((R1 > R2) && ((R1 - R2) > 0.02)) {
//                        bitmapRecortado = Bitmap.createBitmap(bitmapBuffer, anchoInicial, 0, ancho, alto);
//                    } else {
//                        bitmapRecortado = Bitmap.createBitmap(bitmapBuffer, 0, altoInicial, ancho, alto);
//                    }
//                }
//                bitmapBuffer = bitmapRecortado;
//
//            }
//        }

        guardarImagen(null);
        galleryAddPic();

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(VistaPrevia.currentFileName);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void guardarImagen(View v){

        if(bitmapBuffer!=null) {
            FileOutputStream outputStream = null;
            File file =createFilePath();
            VistaPrevia.currentFileName=file.getAbsolutePath();
            try {
                outputStream = new FileOutputStream(file);
                bitmapBuffer.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show();
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            if(vista.getScaleX() == 1 && vista.getScaleY() == 1) {
//                bitmapRecortado.recycle();
//            }
            //galleryAddPic();
        }else{
            Toast.makeText(this,"No existe imagen previa",Toast.LENGTH_SHORT).show();
        }

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

    public class vista extends View {

        Path  pathObtenerColor;
        float pixelsX = 0;
        float pixelsY = 0;
        float pixelsXi = 0;
        float pixelsYi = 0;
        Drawable image;

        //float densidad;
        //Bitmap bitmap;

        Paint paintCordenada;
        Paint paintObtenerColor;
        Paint paintColorObtenido;

        float x;
        float y;



        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public vista(Context context) {
            super(context);

            if (VistaPrevia.currentFileName!=null) {
//                image= new BitmapDrawable(getResources(),VistaPrevia.currentFileName);
                image=new BitmapDrawable(getResources(), VistaPrevia.imagen);
            }
            else
                image=getResources().getDrawable(R.drawable.ic_image_black_36dp);

            //bitmap=BitmapFactory.decodeFile(VistaPrevia.currentFileName);

            //densidad = getResources().getDisplayMetrics().density;

            paintCordenada = new Paint();
            paintCordenada.setColor(Color.RED);
            paintCordenada.setTextSize(45);
            paintCordenada.setAntiAlias(true);
            paintCordenada.setStyle(Paint.Style.FILL_AND_STROKE);
            paintCordenada.setTypeface(Typeface.DEFAULT_BOLD);

            paintMedir=new Paint();
            paintMedir.setStyle(Paint.Style.STROKE);
            paintMedir.setColor(Color.RED);
            paintMedir.setStrokeWidth(5*getResources().getDisplayMetrics().density);
            paintMedir.setAntiAlias(true);

            paintObtenerColor=new Paint();
            paintObtenerColor.setColor(getResources().getColor(R.color.colorAccent));
            paintObtenerColor.setAntiAlias(true);
            paintObtenerColor.setStyle(Paint.Style.STROKE);
            paintObtenerColor.setStrokeWidth(1);

            paintColorObtenido=new Paint();
            paintColorObtenido.setAntiAlias(true);
            paintColorObtenido.setStyle(Paint.Style.FILL_AND_STROKE);
            paintColorObtenido.setStrokeWidth(1);

            pathObtenerColor=new Path();

            ///////////////////////////////////
            setBackground(image);
            /////////////////////////////////////

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            /*
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(VistaPrevia.currentFileName,options);
            float photoW = options.outWidth;
            float photoH = options.outHeight;

            // Determine how much to scale down the image
            float scaleFactor = Math.max(photoW / w, photoH / h);
            float scaleFactorCortado= scaleFactor;
            if(scaleFactor>(int)scaleFactorCortado)
                scaleFactor+=1;
            // Decode the image file into a Bitmap sized to fill the View
            options.inJustDecodeBounds = false;
            options.inSampleSize = (int)scaleFactor;
            options.inPurgeable = true;

            bitmap = BitmapFactory.decodeFile(VistaPrevia.currentFileName, options);
            */

        }

        @Override
        public void buildDrawingCache() {
            super.buildDrawingCache();
        }

        @Override
        public Bitmap getDrawingCache() {
            return super.getDrawingCache();
        }

        public void onDraw(Canvas canvas) {


            //onSizeChanged(getWidth(),getHeight(),0,0);
            //float ancho=bitmap.getWidth();
            //float alto=bitmap.getHeight();


            //canvas.drawBitmap(bitmap,(w/2) - bitmap.getWidth()/2,
            //        (h/2) - bitmap.getHeight()/2,null);
            //image.draw(canvas);


            float imagenX=image.getIntrinsicWidth();
            float imagenY=image.getIntrinsicHeight();

            R1=imagenY/imagenX;

            float w=canvas.getWidth();
            float h=canvas.getHeight();
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
            if ((R1 < R2) && ((R2-R1)>0.05)) {
                ancho = (int) w;
                alto = (int) (R1 * w);
                image.setBounds(0,0,ancho,alto);

                altoInicial = (int) (h / 2 - alto / 2);
                altoFinal = (int) (h / 2 + alto / 2);
                if(Build.VERSION.SDK_INT==Build.VERSION_CODES.LOLLIPOP)
                {  //image.setBounds(0,altoInicial/2,ancho,alto+altoInicial/2);
                    image.setBounds(0,0,ancho,alto);
                }else {
                    image.setBounds(0, altoInicial, ancho, altoFinal);
                }

            }

            if (medir) {
                int color = Color.parseColor(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("colorMedicion","#2962FF"));
                paintMedir.setColor(color);

                    distancia = medir(xMedirInicial, yMedirInicial, xMedirFinal, yMedirFinal);
                    distanciaReal = distancia * escala;

                    bd = new BigDecimal(distanciaReal);
                    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                    distanciaReal = bd.floatValue();

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
                    paintCordenada.setColor(color);

                    paintMedir.setStrokeWidth(5*getResources().getDisplayMetrics().density/scaleX);

                    canvas.drawLine(xMedirInicial, yMedirInicial, xMedirFinal, yMedirFinal, paintMedir);


                    Log.i("medir", "x: " + String.valueOf(xPaint) + " y: " + String.valueOf(yPaint) +
                            " ws: " + String.valueOf(scaleX) + " hs: " + String.valueOf(scaleY));
                    canvas.drawText("Distancia: " + String.valueOf(distanciaReal) + " um", xPaint + 10/scaleX*densidad,
                            yPaint + (25*densidad + linearLayout2.getHeight())/scaleY, paintCordenada);

                    canvas.drawText("Angulo: " + String.format("%.1f",Math.toDegrees((Double.isNaN(angulo)) ? 0 : angulo)) + " °", xPaint + 10/scaleX*densidad,
                            yPaint + (45*densidad + linearLayout2.getHeight())/scaleY, paintCordenada);
                    paintCordenada.setTextSize(45);

            }
            paintCordenada.setTextSize(45);
            if (relacion) {
                paintMedir.setColor(Color.parseColor("#F50057"));
                distancia = medir(xMedirInicial, yMedirInicial, xMedirFinal, yMedirFinal);
                if (accion == "move" || accion == "up" || accion == "down"){
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

                    paintMedir.setStrokeWidth(5*getResources().getDisplayMetrics().density/scaleX);

                    paintCordenada.setTextSize(textSize / scaleX);
                    paintCordenada.setColor(Color.parseColor("#F50057"));

                    canvas.drawLine(xMedirInicial, yMedirInicial, xMedirFinal, yMedirFinal, paintMedir);


                    if(agregarBoton.getVisibility()==VISIBLE)
                        canvas.drawText("Escala: " + String.valueOf((Double.isInfinite(escala)) ? "Ninguna" : escala), xPaint + 10/scaleX*densidad,
                            yPaint + (25*densidad + linearLayout2.getHeight())/scaleY, paintCordenada);

                        canvas.drawText("Angulo: " + String.format("%.1f",Math.toDegrees((Double.isNaN(angulo)) ? 0 : angulo)) + " °", xPaint + 10/scaleX*densidad,
                            yPaint + (45*densidad + linearLayout2.getHeight())/scaleY, paintCordenada);

                    paintCordenada.setTextSize(45);
                }

                if (listoBoton.isSelected()) {
                    distancia = medir(xMedirInicial, yMedirInicial, xMedirFinal, yMedirFinal);
                    escala = obtenerEscala(distancia);

                    if(escala!=0 && !Float.isInfinite(escala)) {
                        guardarConfiguracionCalibracion();
                        Toast.makeText(MedicionesCuantitativas.this, "Se ha obtenido la escala con exito...", Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(MedicionesCuantitativas.this, "No se ha podido obtener la escala...", Toast.LENGTH_LONG).show();

                    listoBoton.setSelected(false);
                    listoBoton.setVisibility(INVISIBLE);
                    agregarBoton.setVisibility(VISIBLE);
                }
            }
            //Coordenadas rojas!!
//            if (medir || relacion) {
//                if (accion == "down" || accion == "move") {
//                    paintCordenada.setColor(Color.RED);
//
//                    float scaleX = vista.getScaleX();
//                    float scaleY = vista.getScaleY();
//
//                    //float wScaleImage=vista.getWidth()/scaleX;
//                    //float hScaleImage=vista.getHeight()/scaleY;
//                    float focusScaleX = focusPrevioX / scaleX;
//                    float focusScaleY = focusPrevioY / scaleY;
//                    float xPaint;
//                    float yPaint;
//                    if (scaleX > 1 && scaleY > 1) {
//                        xPaint = (focusPrevioX - focusScaleX);
//                        yPaint = (focusPrevioY - focusScaleY);
//                        xPaint += (-mPosX / scaleX);
//                        yPaint += (-mPosY / scaleY);
//                    } else {
//                        xPaint = 0;
//                        yPaint = 0;
//                    }
//                    float textSize = 50;
//
//                    paintCordenada.setTextSize(textSize / scaleX);
//
//                    // canvas.drawText("X: " + String.valueOf((int)x)+" Y: "+String.valueOf((int)y),50,50,paint[0]);
//                    canvas.drawText("X: " + String.valueOf((int) x) + " Y: " + String.valueOf((int) y),
//                            xPaint + 10/scaleX * densidad, yPaint + (25* densidad + linearLayout2.getHeight())/scaleY, paintCordenada);
//                    paintCordenada.setTextSize(45);
//                }
//            }
                if (calibrado && calibradoRealizado) {
                    paintCordenada.setColor(Color.YELLOW);
                    paintMedir.setColor(Color.YELLOW);
                    escala = obtenerEscala(tamañoMax);
                    // canvas.drawText("X: " + String.valueOf((int)x)+" Y: "+String.valueOf((int)y),50,50,paint[0]);
                    canvas.drawText("Pixeles: " + String.valueOf(tamañoMax) + " Escala: " +
                            String.valueOf(escala), 50, (25*densidad + linearLayout2.getHeight()), paintCordenada);
                    canvas.drawLine(calibradoMaximo[0], calibradoMaximo[2], calibradoMaximo[1], calibradoMaximo[3], paintMedir);
                }
                if(obtenerColor || obtenerColorBoton.isActivated()){

                    paintCordenada.setColor(Color.RED);

                    int r,g,b;

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
//                    vista.buildDrawingCache();
//                    Bitmap bitmapColor=vista.getDrawingCache();
                    r=Color.red(bitmapColor.getPixel((int)xColor,(int)yColor));
                    g=Color.green(bitmapColor.getPixel((int)xColor,(int)yColor));
                    b=Color.blue(bitmapColor.getPixel((int)xColor,(int)yColor));

                    if(obtenerColor) {
                        if (accion == "down") {
                            rMax = r;
                            rMin = r;
                            gMax = g;
                            gMin = g;
                            bMax = b;
                            bMin = b;
                        }

                        if (accion == "move") {
                            if (rMax < r) {
                                rMax = r;
                            }
                            if (rMin > r) {
                                rMin = r;
                            }
                            if (gMax < g) {
                                gMax = g;
                            }
                            if (gMin > g) {
                                gMin = g;
                            }
                            if (bMax < b) {
                                bMax = b;
                            }
                            if (bMin > b) {
                                bMin = b;
                            }
                        }
                        if (accion == "up") {
                            if (rMax < r) {
                                rMax = r;
                            }
                            if (rMin > r) {
                                rMin = r;
                            }
                            if (gMax < g) {
                                gMax = g;
                            }
                            if (gMin > g) {
                                gMin = g;
                            }
                            if (bMax < b) {
                                bMax = b;
                            }
                            if (bMin > b) {
                                bMin = b;
                            }
                        }
                    }

                    canvas.drawText("RM: " + String.valueOf(rMax)+ "  Rm: " + String.valueOf(rMin)
                            , xPaint + 10/scaleX * densidad,yPaint + (25 * densidad + linearLayout2.getHeight())/scaleY, paintCordenada);
                    canvas.drawText("GM: " + String.valueOf(gMax) + "  Gm: " + String.valueOf(gMin)
                            , xPaint + 10/scaleX * densidad,yPaint + (45 * densidad + linearLayout2.getHeight())/scaleY, paintCordenada);
                    canvas.drawText("BM: " + String.valueOf(bMax) + "  Bm: " + String.valueOf(bMin)
                            , xPaint + 10/scaleX * densidad,yPaint + (65 * densidad + linearLayout2.getHeight())/scaleY, paintCordenada);

                    paintColorObtenido.setARGB(255,rMax,gMax,bMax);

                    canvas.drawCircle(xPaint + 40/scaleX * densidad,yPaint +
                            (canvas.getHeight()-(40*densidad))/scaleY,15*densidad/scaleX,
                            paintColorObtenido);

                    paintColorObtenido.setARGB(255,rMin,gMin,bMin);

                    canvas.drawCircle(xPaint + 100/scaleX * densidad,yPaint +
                            (canvas.getHeight()-(40*densidad))/scaleY,15*densidad/scaleX,paintColorObtenido);

                    canvas.drawPath(pathObtenerColor,paintObtenerColor);

                    paintCordenada.setTextSize(45);
                }


        }

        public boolean onTouchEvent(MotionEvent e){

            x=e.getX();
            y=e.getY();
            xOriginalScale=getScaleX();
            yOriginalScale=getScaleY();

            //mScaleDetector.onTouchEvent(e);
            if(e.getAction()==MotionEvent.ACTION_DOWN){
                if(obtenerColor){
                    pathObtenerColor.reset();
                    xColor=x;
                    yColor=y;
//                    pathObtenerColor=new Path();
                    pathObtenerColor.moveTo(xColor,yColor);
                }

                if(relacion && !medicionRealizada) {
                    xMedirInicial = x;
                    yMedirInicial = y;
                    xMedirFinal=x;
                    yMedirFinal=y;
                    reglaSelec=false;
                }

                if (medir && !medicionRealizada) {
                    xMedirInicial = x;
                    yMedirInicial = y;
                    xMedirFinal=x;
                    yMedirFinal=y;
                    reglaSelec=false;
                }

                if((medir || relacion) && medicionRealizada){

                    float xCentral=xMedirInicial+(xMedirFinal-xMedirInicial)/2;
                    float yCentral =yMedirInicial+(int)((yMedirFinal-yMedirInicial)+20*densidad)/2;
                    float cenX = x - xCentral;
                    float cenY = y - yCentral;
                    int distancia=(int)Math.sqrt(cenX*cenX+cenY*cenY);
                    Log.i("regla",String.valueOf(Math.abs(xMedirFinal-xMedirInicial)) + " " + String.valueOf(Math.abs(((yMedirFinal-yMedirInicial)+20*densidad))));
                    Log.i("regla",String.valueOf(distancia));
                    if(distancia <= Math.abs(xMedirFinal-xMedirInicial) || distancia <= Math.abs(((yMedirFinal-yMedirInicial)+20*densidad))){
                        reglaSelec=true;
                    }else{
                        reglaSelec=false;
                    }
                    pixelsX=0;
                    pixelsY=0;
                    pixelsXi = x;
                    pixelsYi = y;
                    Log.i("pixelMove", String.valueOf(pixelsX) + " " + String.valueOf(pixelsY));

                }
                /*
                if(zoom){
                    setPivotX(x);
                    setPivotY(y);
                }*/
                Log.i("regla",String.valueOf(reglaSelec));
                accion="down";
            }

            if(e.getAction()==MotionEvent.ACTION_MOVE) {
                if(obtenerColor){
                    xColor=x;
                    yColor=y;
                    pathObtenerColor.lineTo(xColor,yColor);
                }

                if(relacion && !medicionRealizada) {
                    xMedirFinal = x;
                    yMedirFinal = y;
                }
                if(medir && !medicionRealizada) {
                    xMedirFinal = x;
                    yMedirFinal = y;
                }
                if(reglaSelec){
                    pixelsX=pixelsXi-x;
                    pixelsY=pixelsYi-y;
                    Log.i("regla",String.valueOf(reglaSelec));
                    Log.i("pixel",String.valueOf(pixelsX)+ " " +String.valueOf(pixelsY));
                    xMedirInicial = xMedirInicial - pixelsX;
                    yMedirInicial = yMedirInicial - pixelsY;
                    xMedirFinal  = xMedirFinal - pixelsX;
                    yMedirFinal  = yMedirFinal - pixelsY;
                    pixelsXi=x;
                    pixelsYi=y;
                }

                invalidate();
                accion="move";
            }
            if(e.getAction()==MotionEvent.ACTION_UP) {
                        //////////////////////////////////Obteener color/////////////////
                if(obtenerColor){

                    xColor = x;
                    yColor = y;
//                    SharedPreferences prefs =
//                            getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
//
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putInt("rMax", rMax);
//                    editor.putInt("rMin", rMin);
//                    editor.putInt("gMax", gMax);
//                    editor.putInt("gMin", gMin);
//                    editor.putInt("bMax", bMax);
//                    editor.putInt("bMin", bMin);
//                    editor.commit();
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(MedicionesCuantitativas.this);
                    SharedPreferences.Editor editor=preferences.edit();

                    editor.putString("rMax", String.valueOf(rMax));
                    editor.putString("rMin", String.valueOf(rMin));
                    editor.putString("gMax", String.valueOf(gMax));
                    editor.putString("gMin", String.valueOf(gMin));
                    editor.putString("bMax", String.valueOf(bMax));
                    editor.putString("bMin", String.valueOf(bMin));
                    editor.commit();

                    obtenerColor=false;

                    Toast.makeText(MedicionesCuantitativas.this, "Se han obtenido los colores con exito...", Toast.LENGTH_LONG).show();
                }
                    //////////////////////////////////////Calibracion manual///////////////////
                if(relacion && !medicionRealizada) {
                    xMedirFinal = x;
                    yMedirFinal = y;

                    medicionRealizada=true;
                    invalidateOptionsMenu();
//                    distancia = medir(xMedirInicial, yMedirInicial, xMedirFinal, yMedirFinal);
//                    escala = obtenerEscala(distancia);
//                    guardarConfiguracionCalibracion();
                }
                /////////////////////////////////////////Medición////////////////////////////////
                if(medir && !medicionRealizada) {
                    xMedirFinal = x;
                    yMedirFinal = y;
                    medicionRealizada=true;
                }
//                medicionRealizada=true;
                ///////////////////////////////////////////////////////////////////////////////
//                tg = (yMedirFinal-yMedirInicial)/(xMedirFinal-xMedirInicial);
//                angulo = Math.atan(tg);
//                moduloRecta=Math.sqrt((yMedirFinal-yMedirInicial)*(yMedirFinal-yMedirInicial)
//                                     +(xMedirFinal-xMedirInicial)*(xMedirFinal-xMedirInicial));
                ///////////////////////////////////////////////////////////////////////////////
                Log.i("up","entro");
                Log.i("up",String.valueOf(angulo));

                addBoton.setClickable(true);
                removeBoton.setClickable(true);
                accion="up";
            }
            tg = (yMedirFinal-yMedirInicial)/(xMedirFinal-xMedirInicial);
            angulo = Math.atan(tg);
            moduloRecta=Math.sqrt((yMedirFinal-yMedirInicial)*(yMedirFinal-yMedirInicial)
                    +(xMedirFinal-xMedirInicial)*(xMedirFinal-xMedirInicial));
            invalidate();

            return touchDentroVista;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }
        //        /*@Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//            viewWidth = MeasureSpec.getSize(widthMeasureSpec);
//
//            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
//
//
//            //
//            // Rescales image on rotation
//            //
//
//            if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
//
//                    || viewWidth == 0 || viewHeight == 0)
//
//                return;
//
//            oldMeasuredHeight = viewHeight;
//
//            oldMeasuredWidth = viewWidth;
//
//
//
//
//            if (saveScale == 1) {
//
//                //Fit to screen.
//
//                float scale;
//
//                Drawable drawable = getDrawable();
//
//                if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
//
//                    return;
//
//                int bmWidth = drawable.getIntrinsicWidth();
//
//                int bmHeight = drawable.getIntrinsicHeight();
//
//                Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);
//
//                float scaleX = (float) viewWidth / (float) bmWidth;
//
//                float scaleY = (float) viewHeight / (float) bmHeight;
//
//                scale = Math.min(scaleX, scaleY);
//
//                matrix.setScale(scale, scale);
//
//                // Center the image
//
//                float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
//
//                float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
//
//                redundantYSpace /= (float) 2;
//
//                redundantXSpace /= (float) 2;
//
//                matrix.postTranslate(redundantXSpace, redundantYSpace);
//
//                origWidth = viewWidth - 2 * redundantXSpace;
//
//                origHeight = viewHeight - 2 * redundantYSpace;
//
//                setImageMatrix(matrix);
//
//            }
//
//
//
//        }*/
    }

    private float medir(float xi, float yi, float xf, float yf) {
        float catetoY = yf-yi;
        float catetoX = xf-xi;
        float distancia = (float)Math.sqrt(catetoY*catetoY+catetoX*catetoX);
        return distancia;
    }

    private float obtenerEscala(float distancia) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        aumentoPreferencias           = Integer.parseInt(preferences.getString("aumento",""));
        patronPreferencias     = Float.parseFloat(preferences.getString("patron",""));


        escala = (patronPreferencias)/distancia;
        return escala;
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

        // Let the ScaleGestureDetector inspect all events.
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(event);
        final int action = MotionEventCompat.getActionMasked(event);

        if(zoom) {
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    accion="down";
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
                    accion="move";
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
//                                    focusX=mPosX;
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
                    accion="up";
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
        getMenuInflater().inflate(R.menu.menu_mediciones_cuantitativas,menu);
//        if(!(calibradoRealizado || medicionRealizada)){
//            menu.findItem(R.id.medir).setVisible(false);
//            menu.findItem(R.id.modo).setVisible(false);
//        }else{
//            menu.findItem(R.id.medir).setVisible(true);
//            menu.findItem(R.id.modo).setVisible(true);
//        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.calibracionAutomatica:
//               calibracionAutomatica();
//                break;
            case R.id.calibracionManual:
                calibracionManual();
                break;
//            case R.id.obtenerColor:
//                extraerColor();
//                break;
//            case R.id.modoEscala:
//                if(escala!=0 && !Double.isInfinite(escala)) {
//                    modoCalibradoManual = true;
//                    relacion = true;
//                    medicionRealizada = true;
//                    modoCalibradoAutomatico = false;
//                    modoZoom = false;
//                    modoMedir = false;
//                    touchDentroVista = true;
//                    touchFueraVista = false;
//                    medir = false;
//                    calibrado = false;
//                    zoom = false;
//                    obtenerColor=false;
//                    linearLayout2.setVisibility(View.VISIBLE);
////                    addBoton.setVisibility(View.VISIBLE);
////                    removeBoton.setVisibility(View.VISIBLE);
////                    rotateRight.setVisibility(View.VISIBLE);
////                    rotateLeft.setVisibility(View.VISIBLE);
////                    guardarColores.setVisibility(View.INVISIBLE);
//                    vista.postInvalidate();
//                    modo();
//                }else{
//                    Toast.makeText(this,"Primero obtenga la escala o vuelva a realizarlo...",Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.Modomedir:
//                if(escala!=0 && !Double.isInfinite(escala)) {
//
//                    modoMedir=true;
//                    medir = true;
//                    modoZoom =false;
//                    modoCalibradoAutomatico=false;
//                    modoCalibradoManual=false;
//                    medicionRealizada=true;
//                    relacion = false;
//                    calibrado = false;
//                    zoom=false;
//                    touchDentroVista=true;
//                    touchFueraVista=false;
//                    obtenerColor=false;
//                    linearLayout2.setVisibility(View.VISIBLE);
////                    addBoton.setVisibility(View.VISIBLE);
////                    removeBoton.setVisibility(View.VISIBLE);
////                    rotateRight.setVisibility(View.VISIBLE);
////                    rotateLeft.setVisibility(View.VISIBLE);
////                    guardarColores.setVisibility(View.INVISIBLE);
//                    modo();
//                }else{
//                    Toast.makeText(this,"Primero obtenga la escala o vuelva a realizarlo...",Toast.LENGTH_SHORT).show();
//                }
//                vista.postInvalidate();
//                break;
            case R.id.medir:
                medicion();
                break;
            case R.id.config:
                Intent intent=new Intent(this,Preferencias.class);
//                recreate();
                startActivity(intent);
                configuracion=true;
                break;
//            case R.id.zoom:
//                if(relacion){
//                    relacion=true;
//                    medir=false;
//                    calibrado=false;
//                    obtenerColor=false;
//                    linearLayout2.setVisibility(View.VISIBLE);
////                    addBoton.setVisibility(View.VISIBLE);
////                    removeBoton.setVisibility(View.VISIBLE);
////                    rotateRight.setVisibility(View.VISIBLE);
////                    rotateLeft.setVisibility(View.VISIBLE);
////                    guardarColores.setVisibility(View.INVISIBLE);
//                }else if(medir){
//                    medir=true;
//                    relacion=false;
//                    calibrado=false;
//                    obtenerColor=false;
//                    linearLayout2.setVisibility(View.VISIBLE);
////                    addBoton.setVisibility(View.VISIBLE);
////                    removeBoton.setVisibility(View.VISIBLE);
////                    rotateRight.setVisibility(View.VISIBLE);
////                    rotateLeft.setVisibility(View.VISIBLE);
////                    guardarColores.setVisibility(View.INVISIBLE);
//                }else if(calibrado){
//                    calibrado=true;
//                    relacion=false;
//                    medir=false;
//                    obtenerColor=false;
//                    linearLayout2.setVisibility(View.INVISIBLE);
//                    addBoton.setClickable(false);
//                    removeBoton.setClickable(false);
//                }else if(obtenerColor){
//                    obtenerColor=true;
//                    calibrado=false;
//                    relacion=false;
//                    medir=false;
//                    linearLayout2.setVisibility(View.INVISIBLE);
////                    addBoton.setVisibility(View.INVISIBLE);
////                    removeBoton.setVisibility(View.INVISIBLE);
////                    rotateRight.setVisibility(View.INVISIBLE);
////                    rotateLeft.setVisibility(View.INVISIBLE);
////                    guardarColores.setVisibility(View.VISIBLE);
//                } else{
//                    relacion=false;
//                    medir=false;
//                    calibrado=false;
//                    obtenerColor=false;
//                    linearLayout2.setVisibility(View.INVISIBLE);
//                    addBoton.setClickable(false);
//                    removeBoton.setClickable(false);
//                }
////                if(medicionRealizada)
////                    medir = true;
////                else
////                    medir=false;
//                modoZoom =true;
//                zoom=true;
//                modoMedir=false;
//                //relacion = false;
//                //calibrado = false;
//                touchDentroVista=false;
//                touchFueraVista=true;
////                linearLayout2.setVisibility(View.VISIBLE);
////                addBoton.setClickable(true);
////                removeBoton.setClickable(true);
//
////                ZoomControls zoomControls=new ZoomControls(this);
////                zoomControls.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
////                        ViewGroup.LayoutParams.WRAP_CONTENT,0.3f));
////                linearLayout.addView(zoomControls);
//                modo();
//                break;

//            case R.id.vistaPrevia:
//                //guardarEnCache();
////                if(bitmapImage!=null)
////                    bitmapImage.recycle();
////                if(bitmapBuffer!=null)
////                    bitmapBuffer.recycle();
//
//                Intent intentVista=new Intent(this,VistaPrevia.class);
//                startActivity(intentVista);
//                break;
            case R.id.guardar:
                guardar(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void extraerColor() {
        accion=null;
        modoCalibradoManual = false;
        relacion = false;
        medicionRealizada = false;
        modoCalibradoAutomatico = false;
        modoZoom = false;
        modoMedir = false;
        touchDentroVista = true;
        touchFueraVista = false;
        medir = false;
        calibrado = false;
        zoom = false;


//                if(bitmapImage!=null)
//                    bitmapColor=bitmapImage;
//                else {
        vista.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        vista.buildDrawingCache();
        bitmapColor = vista.getDrawingCache();
//                }
        calibrarBotonA.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        calibrarBotonM.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        medirBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        zoomBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        obtenerColorBoton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));

        linearLayout2.setVisibility(View.VISIBLE);
                addBoton.setVisibility(View.INVISIBLE);
                removeBoton.setVisibility(View.INVISIBLE);
                rotateRight.setVisibility(View.INVISIBLE);
                rotateLeft.setVisibility(View.INVISIBLE);
                agregarBoton.setVisibility(View.VISIBLE);
                obtenerColorBoton.setVisibility(View.INVISIBLE);

        zoomBoton.setActivated(false);
        calibrarBotonA.setActivated(false);
        calibrarBotonM.setActivated(false);
        medirBoton.setActivated(false);
        obtenerColorBoton.setActivated(true);

        obtenerColor=false;
        vista.postInvalidate();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void aplicarZoom() {
        if(relacion){
            relacion=true;
            medir=false;
            calibrado=false;
            obtenerColor=false;

            linearLayout2.setVisibility(View.VISIBLE);
            calibrarBotonA.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            calibrarBotonM.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));
            medirBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            obtenerColorBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            zoomBoton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));

            zoomBoton.setActivated(true);
            calibrarBotonA.setActivated(false);
            calibrarBotonM.setActivated(true);
            medirBoton.setActivated(false);
            obtenerColorBoton.setActivated(false);
//                    addBoton.setVisibility(View.VISIBLE);
//                    removeBoton.setVisibility(View.VISIBLE);
//                    rotateRight.setVisibility(View.VISIBLE);
//                    rotateLeft.setVisibility(View.VISIBLE);
//                    guardarColores.setVisibility(View.INVISIBLE);
        }else if(medir){
            medir=true;
            relacion=false;
            calibrado=false;
            obtenerColor=false;
            linearLayout2.setVisibility(View.VISIBLE);
//                    addBoton.setVisibility(View.VISIBLE);
//                    removeBoton.setVisibility(View.VISIBLE);
//                    rotateRight.setVisibility(View.VISIBLE);
//                    rotateLeft.setVisibility(View.VISIBLE);
//                    guardarColores.setVisibility(View.INVISIBLE);
            calibrarBotonA.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            medirBoton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));
            calibrarBotonM.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            obtenerColorBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            zoomBoton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));

            zoomBoton.setActivated(true);
            calibrarBotonA.setActivated(false);
            calibrarBotonM.setActivated(false);
            medirBoton.setActivated(true);
            obtenerColorBoton.setActivated(false);
        }else if(calibrado){
            calibrado=true;
            relacion=false;
            medir=false;
            obtenerColor=false;
            linearLayout2.setVisibility(View.INVISIBLE);
            addBoton.setClickable(false);
            removeBoton.setClickable(false);

            zoomBoton.setActivated(true);
            calibrarBotonA.setActivated(false);
            calibrarBotonM.setActivated(false);
            medirBoton.setActivated(false);
            obtenerColorBoton.setActivated(false);
        }else if(obtenerColor){
            obtenerColor=false;
            calibrado=false;
            relacion=false;
            medir=false;
            linearLayout2.setVisibility(View.INVISIBLE);

            zoomBoton.setActivated(true);
            calibrarBotonA.setActivated(false);
            calibrarBotonM.setActivated(false);
            medirBoton.setActivated(false);
            obtenerColorBoton.setActivated(false);
//                    addBoton.setVisibility(View.INVISIBLE);
//                    removeBoton.setVisibility(View.INVISIBLE);
//                    rotateRight.setVisibility(View.INVISIBLE);
//                    rotateLeft.setVisibility(View.INVISIBLE);
//                    guardarColores.setVisibility(View.VISIBLE);
        }else{
            relacion=false;
            medir=false;
            calibrado=false;
            obtenerColor=false;
            linearLayout2.setVisibility(View.INVISIBLE);
            addBoton.setClickable(false);
            removeBoton.setClickable(false);

            zoomBoton.setActivated(true);
            calibrarBotonA.setActivated(false);
            calibrarBotonM.setActivated(false);
            medirBoton.setActivated(false);
            obtenerColorBoton.setActivated(false);
        }
//                if(medicionRealizada)
//                    medir = true;
//                else
//                    medir=false;
//        calibrarBotonA.setImageTintList(ColorStateList.valueOf(Color.WHITE));
//        calibrarBotonM.setImageTintList(ColorStateList.valueOf(Color.WHITE));
//        medirBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
//        obtenerColorBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
//        zoomBoton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));

        modoZoom =true;
        zoom=true;
        modoMedir=false;
        //relacion = false;
        //calibrado = false;
        touchDentroVista=false;
        touchFueraVista=true;
//                linearLayout2.setVisibility(View.VISIBLE);
//                addBoton.setClickable(true);
//                removeBoton.setClickable(true);

//                ZoomControls zoomControls=new ZoomControls(this);
//                zoomControls.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,0.3f));
//                linearLayout.addView(zoomControls);
        vista.invalidate();
//        modo();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void calibracionManual() {

        modoCalibradoManual=true;
        relacion = true;
        medicionRealizada=true;
        modoCalibradoAutomatico=false;
        modoZoom=false;
        modoMedir=false;
        touchDentroVista=true;
        touchFueraVista=false;
        medir = false;
        calibrado = false;
        zoom=false;
        obtenerColor=false;
        linearLayout2.setVisibility(View.VISIBLE);

//                addBoton.setVisibility(View.VISIBLE);
//                removeBoton.setVisibility(View.VISIBLE);
//                rotateRight.setVisibility(View.VISIBLE);
//                rotateLeft.setVisibility(View.VISIBLE);
//                obtenerColorBoton.setVisibility(View.INVISIBLE);
        addBoton.setVisibility(View.INVISIBLE);
        removeBoton.setVisibility(View.INVISIBLE);
        rotateRight.setVisibility(View.INVISIBLE);
        rotateLeft.setVisibility(View.INVISIBLE);
        obtenerColorBoton.setVisibility(View.INVISIBLE);
        listoBoton.setVisibility(View.INVISIBLE);
        agregarBoton.setVisibility(View.VISIBLE);
        cancelarBoton.setVisibility(View.INVISIBLE);

        calibrarBotonA.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        calibrarBotonM.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));
        medirBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        zoomBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        obtenerColorBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        calibrarBotonM.setActivated(true);
        calibrarBotonA.setActivated(false);
        zoomBoton.setActivated(false);
        medirBoton.setActivated(false);
        obtenerColorBoton.setActivated(false);


        vista.invalidate();
//        modo();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void calibracionAutomatica() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String aumento=preferences.getString("aumento",null);
        String patron=preferences.getString("patron",null);
        if((aumento != null) && (patron != null)) {

                    modoCalibradoAutomatico = true;
                    modoCalibradoManual = false;
                    modoZoom = false;
                    modoMedir = false;
                    obtenerColor = false;
                    medir=false;
                    relacion=false;

                    linearLayout2.setVisibility(View.VISIBLE);
                    addBoton.setVisibility(View.INVISIBLE);
                    removeBoton.setVisibility(View.INVISIBLE);
                    rotateRight.setVisibility(View.INVISIBLE);
                    rotateLeft.setVisibility(View.INVISIBLE);
                    listoBoton.setVisibility(View.INVISIBLE);
                    cancelarBoton.setVisibility(View.INVISIBLE);
                    agregarBoton.setVisibility(View.VISIBLE);
                    obtenerColorBoton.setVisibility(View.VISIBLE);

            calibrarBotonA.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));
            calibrarBotonM.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            medirBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            zoomBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            obtenerColorBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));

            calibrarBotonM.setActivated(false);
            calibrarBotonA.setActivated(true);
            zoomBoton.setActivated(false);
            medirBoton.setActivated(false);
            obtenerColorBoton.setActivated(false);
//                    realizarCalibracionAutomatica();
//                        touchDentroVista=true;
//                        touchFueraVista=false;
//                        medir = false;
//                        relacion = false;
//                        calibrado = true;
//                        zoom=false;

//                        vista.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//                        vista.buildDrawingCache();
//                        bitmapImage = vista.getDrawingCache();
//                        int w = bitmapImage.getWidth();
//                        int h = bitmapImage.getHeight();
//                        Log.i("calibrado","w: " + String.valueOf(w) + " h :" +  String.valueOf(h));
//                        int r = 0;
//                        int g = 0;
//                        int b = 0;
//
//                        //ArrayList<Integer> pixeles = new ArrayList<Integer>();
//                        int[][] calibrado=new int[h][4];
//                        //banderaDos=true;
//                        for (int i = 0; i < h; i++) {
//                            //pixeles.add(0);
//                            banderaDos=false;
//                            for (int j = 0; j < w; j++) {
//                                r = Color.red(bitmapImage.getPixel(j, i));
//                                g = Color.green(bitmapImage.getPixel(j, i));
//                                b = Color.blue(bitmapImage.getPixel(j, i));
//
//
//                                if ((r > 100) &&  g < 50 && b < 50) {
//                                    //pixeles.set(i, pixeles.get(i) + 1);
//                                    if(!banderaDos) {
//                                        //calibradoIncialX = j;
//                                        //calibradoInicialY = i;
//                                        calibrado[i][0]= j;
//                                        calibrado[i][2]= i;
//                                    }
//                                    banderaDos=true;
//                                    bandera=true;
//                                    continue;
//                                }
//                                if(bandera){
//                                    //calibradoFinalX=j;
//                                    //calibradoFinalY=i;
//                                    calibrado[i][1]= j;
//                                    calibrado[i][3]= i;
//                                    bandera=false;
//                                    //banderaDos=false;
//                                }
//                            }
//                        }
//                        /*
//                        Log.i("linea","x: " + String.valueOf(calibradoIncialX) + " y :" +
//                                String.valueOf(calibradoInicialY) + "x2: " +
//                                String.valueOf(calibradoFinalX) + " y2 :" +
//                                String.valueOf(calibradoFinalY) );
//                                */
//                        /*
//                        int l = 0;
//                        int k = 0;
//                        pixelesMayores = pixeles.get(l);
//                        while (l < h) {
//                            if (pixeles.get(l) > pixeles.get(k)) {
//                                pixelesMayores = pixeles.get(l);
//                                k = l;
//                            }
//                            l++;
//                        }
//                        */
//
//                        int x,y,xMax, yMax;
//
//                        calibradoMaximo=new int[4];
//                        xMax=calibrado[0][1]-calibrado[0][0];
//                        yMax=calibrado[0][3]-calibrado[0][2];
//                        tamañoMax=(int)Math.sqrt(xMax*xMax+yMax*yMax);
//
//                        for(int i=1;i<h;i++){
//                            x=calibrado[i][1]-calibrado[i][0];
//                            y=calibrado[i][3]-calibrado[i][2];
//                            tamaño=(int)Math.sqrt(x*x+y*y);
//                            if(tamaño>tamañoMax){
//                                tamañoMax=tamaño;
//                                for(int j=0;j<4;j++){
//                                    calibradoMaximo[j]=calibrado[i][j];
//                                }
//                            }
//                        }
//                        bitmapImage.recycle();
//                        calibradoRealizado = true;
        }else{
            Toast.makeText(this, "No esta configurado el tamaño del patron ni el aumento. Entre en configuracion...", Toast.LENGTH_LONG).show();
        }
        //linearLayout2.setVisibility(View.INVISIBLE);
//        modo();
        vista.postInvalidate();
    }

    private void realizarCalibracionAutomatica() {
        vista.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        vista.buildDrawingCache();

        bitmapImage = vista.getDrawingCache();


        int h = bitmapImage.getHeight();
        int max = h / 10;


        pDialog = new ProgressDialog(MedicionesCuantitativas.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMax(max);
        miTareaAsincronaDialog = new MiTareaAsincronaDialog();
        miTareaAsincronaDialog.execute();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void medicion() {
        if(escala!=0 && !Double.isInfinite(escala)) {
            modoMedir=true;
            medir = true;
            modoZoom =false;
            modoCalibradoAutomatico=false;
            modoCalibradoManual=false;
            medicionRealizada=true;
            relacion = false;
            calibrado = false;
            zoom=false;
            touchDentroVista=true;
            touchFueraVista=false;
            obtenerColor=false;
            linearLayout2.setVisibility(View.VISIBLE);

            addBoton.setVisibility(View.VISIBLE);
            removeBoton.setVisibility(View.VISIBLE);
            rotateRight.setVisibility(View.VISIBLE);
            rotateLeft.setVisibility(View.VISIBLE);
            agregarBoton.setVisibility(View.VISIBLE);
            obtenerColorBoton.setVisibility(View.INVISIBLE);
            listoBoton.setVisibility(View.INVISIBLE);
            cancelarBoton.setVisibility(View.INVISIBLE);

            calibrarBotonA.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            calibrarBotonM.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            medirBoton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));
            zoomBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            obtenerColorBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));

            medirBoton.setActivated(true);
            zoomBoton.setActivated(false);
            calibrarBotonA.setActivated(false);
            calibrarBotonM.setActivated(false);
            obtenerColorBoton.setActivated(false);
//            modo();
        }else{
            Toast.makeText(this,"Primero obtenga la escala o vuelva a realizarlo...",Toast.LENGTH_SHORT).show();
            LL.removeView(linearLayoutMedicion);
            calibrarBotonM.setActivated(false);
            calibrarBotonM.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }
        vista.postInvalidate();
    }

//    private void modo() {
//        if(modoMedir)
//            M.setText("M: " + "Med.");
//        else if(modoZoom)
//            M.setText("M: " + "Zoom");
//        else if(modoCalibradoManual)
//            M.setText("M: " + "Cal.");
//        else if(modoCalibradoAutomatico)
//            M.setText("M: " + "Cal.");
//        else
//            M.setText("M: " + "-");
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Restart",String.valueOf(configuracion));
//        recreate();

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);

        rMax=Integer.parseInt(preferences.getString("rMax","0"));
        rMin=Integer.parseInt(preferences.getString("rMin","0"));
        gMax=Integer.parseInt(preferences.getString("gMax","0"));
        gMin=Integer.parseInt(preferences.getString("gMin","0"));
        bMax=Integer.parseInt(preferences.getString("bMax","0"));
        bMin=Integer.parseInt(preferences.getString("bMin","0"));

        escala=Float.parseFloat(preferences.getString("escala","0"));
        aumentoMedicion=Integer.parseInt(preferences.getString("aumentoMedicion","0"));

        float incremento=(float)(aumentoPreferencias)/aumentoMedicion;

        escala*=incremento;

        //registra los cambios de la preferencia
        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("patron")||key.equals("aumento")){
                    escala=0;

                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("escala",String.valueOf(escala));
                    editor.apply();
                }
            }
        });

        mostrarEnToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(bitmapImage!=null)
//            bitmapImage.recycle();
//        if(bitmapBuffer!=null)
//            bitmapBuffer.recycle();
//
//        Intent intentVista=new Intent(this,VistaPrevia.class);
//        startActivity(intentVista);
    }

    private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
//            kmeans();
            int w = bitmapImage.getWidth();
            int h = bitmapImage.getHeight();
            Log.i("calibrado","w: " + String.valueOf(w) + " h :" +  String.valueOf(h));
            int r = 0;
            int g = 0;
            int b = 0;
            int progreso=0;
            int conteoProgreso=4;

            int[][] calibrado=new int[h][4];
            Log.i("calibrado","rMax: " + String.valueOf(rMax) + " rMin :" +  String.valueOf(rMin));
            for (int i = 0; i < h; i++) {
                //pixeles.add(0);
                banderaDos=false;
                if(conteoProgreso==10) {
                    publishProgress(progreso++);
                    conteoProgreso=0;
                }
                for (int j = 0; j < w; j++) {
                    r = Color.red(bitmapImage.getPixel(j, i));
                    g = Color.green(bitmapImage.getPixel(j, i));
                    b = Color.blue(bitmapImage.getPixel(j, i));
                    if(isCancelled())
                        break;

                    if ((r >= rMin && r <= rMax ) &&  (g <= gMax && g >= gMin) && (b <= bMax && b >= bMin)) {
                        //pixeles.set(i, pixeles.get(i) + 1);

                        if(!banderaDos) {
                            //calibradoIncialX = j;
                            //calibradoInicialY
                            // = i;
                            calibrado[i][0]= j;
                            calibrado[i][2]= i;
                        }
                        banderaDos=true;
                        bandera=true;
                        continue;
                    }
                    if(bandera){
                        //calibradoFinalX=j;
                        //calibradoFinalY=i;
                        calibrado[i][1]= j;
                        calibrado[i][3]= i;
                        bandera=false;
                        //banderaDos=false;
                    }
                }
                conteoProgreso++;
            }


            int x,y,xMax, yMax;

            calibradoMaximo=new int[4];
            xMax=calibrado[0][1]-calibrado[0][0];
            yMax=calibrado[0][3]-calibrado[0][2];
            tamañoMax=(int)Math.sqrt(xMax*xMax+yMax*yMax);
            publishProgress(progreso++);
            boolean Noentro=true;

            for(int i=1;i<h;i++){
                x=calibrado[i][1]-calibrado[i][0];
                y=calibrado[i][3]-calibrado[i][2];
                tamaño=(int)Math.sqrt(x*x+y*y);
                if(conteoProgreso==10) {
                    publishProgress(progreso++);
                    conteoProgreso=0;
                }
                if(tamaño>tamañoMax){
                    tamañoMax=tamaño;
                    Noentro=false;
                    for(int j=0;j<4;j++){
                        calibradoMaximo[j]=calibrado[i][j];
                    }
                }
                conteoProgreso++;
            }
            if(Noentro){
                for(int j=0;j<4;j++){
                    calibradoMaximo[j]=calibrado[0][j];
                }
            }
            /*for(int i=1; i<=10; i++) {
                publishProgress(i*10);
                if(isCancelled())
                    break;
            }*/
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            pDialog.setProgress(progreso);
        }
        @Override
        protected void onPreExecute() {
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MedicionesCuantitativas.MiTareaAsincronaDialog.this.cancel(true);
                }
            });

            pDialog.setProgress(0);
            pDialog.show();
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                pDialog.dismiss();
                Toast.makeText(MedicionesCuantitativas.this, "¡Calibración finalizada!",
                        Toast.LENGTH_SHORT).show();
            }
            touchDentroVista=true;
            touchFueraVista=false;
            medir = false;
            relacion = false;
            calibrado = true;
            zoom=false;
            calibradoRealizado = true;

            calibrarBotonA.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBotonActivado)));
            calibrarBotonM.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            medirBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            zoomBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            obtenerColorBoton.setImageTintList(ColorStateList.valueOf(Color.WHITE));

            calibrarBotonA.setActivated(true);
            zoomBoton.setActivated(false);
            calibrarBotonM.setActivated(false);
            medirBoton.setActivated(false);
            obtenerColorBoton.setActivated(false);
//            bitmapImage.recycle();
            vista.invalidate();
            invalidateOptionsMenu();
            escala = obtenerEscala(tamañoMax);
            guardarConfiguracionCalibracion();
            Log.i("Progress",String.valueOf(pDialog.getProgress()));
        }
        @Override
        protected void onCancelled() {
            Toast.makeText(MedicionesCuantitativas.this, "¡Calibración cancelada!",
                    Toast.LENGTH_SHORT).show();

        }

    }

    private void kmeans() {
        final Mat labels=new Mat();
        final Mat centroide=new Mat();
        Mat centroideGris=new Mat();

        Utils.bitmapToMat(bitmapImage, imagenMatriz);
        imagenProcesada = imagenMatriz.clone();

        imagenProcesada.convertTo(imagenProcesada, CvType.CV_8UC3);
        imagenProcesada=imagenProcesada.reshape(1,imagenProcesada.rows()*imagenProcesada.cols());
        imagenProcesada.convertTo(imagenProcesada,CvType.CV_32F);

                TermCriteria criteria=new TermCriteria(TermCriteria.EPS|TermCriteria.MAX_ITER,20,0.5);

                Core.kmeans(imagenProcesada,3,labels,criteria,10,Core.KMEANS_PP_CENTERS,centroide);

//                /////////////////////////////////////////////obtengo los valores minimos y sus indices//////////
//                int indiceMax=0;
//                int indiceMin=0;
//                double valorMin;
//                double[] intensidad=new double[centroide.rows()];
//
//                double bl = centroide.get(0, 0)[0];
//                double gr = centroide.get(0, 1)[0];
//                double re = centroide.get(0, 2)[0];
//
//                intensidad[0] = (bl + gr + re) / 3;
//                valorMin=intensidad[0];
//
//                for(int i=1; i<centroide.rows();i++) {
//                    bl = centroide.get(i, 0)[0];
//                    gr = centroide.get(i, 1)[0];
//                    re = centroide.get(i, 2)[0];
//                    intensidad[i] = (bl + gr + re) / 3;
//                    if(intensidad[i]<valorMin){
//                        indiceMin=i;
//                        valorMin=intensidad[i];
//                    }
//                }
//
//                double valor1;
//                indice1=0;
//                valor1=intensidad[0];
//                for(int j=0;j<intensidad.length;j++){
//                    if(intensidad[j]<valor1){
//                        indice1=j;
//                        valor1=intensidad[j];
//                    }
//                }
//                double valor2;
//                indice2=0;
//                boolean entro=false;
//                valor2=intensidad[indice1];
//                for(int j=0;j<intensidad.length;j++){
//                    if(intensidad[j]>valor1 && !entro){
//                        indice2 = j;
//                        valor2=intensidad[j];
//                        entro=true;
//                    }
//                    if(intensidad[j]<valor2 && intensidad[j]!=valor1) {
//                        indice2 = j;
//                        valor2 = intensidad[j];
//                    }
//                }
//                entro=false;
//                double valor3;
//                indice3=0;
//                valor3=intensidad[indice2];
//                for(int j=0;j<intensidad.length;j++){
//                    if(intensidad[j]>valor2 && !entro){
//                        indice3=j;
//                        valor3=intensidad[j];
//                        entro=true;
//                    }
//                    if(intensidad[j]<valor3 && intensidad[j]!=valor2 && intensidad[j]!=valor1) {
//                        indice3 = j;
//                        valor3 = intensidad[j];
//                    }
//                }
//                double valor4;
//                indice4=0;
//                for(int j=0;j<intensidad.length;j++){
//                    if(intensidad[j]>valor3){
//                        indice4=j;
//                        valor4=intensidad[j];
//                    }
//                }
//                //////////////////////////////////////////////////////////////////////////////////////////

                centroide.convertTo(centroide, CvType.CV_8UC1,255);
//        centroide.reshape(3);

                clusters = new ArrayList<Mat>();
                for(int i = 0; i < centroide.rows(); i++) {
                    clusters.add(Mat.zeros(imagenMatriz.size(), CvType.CV_8UC3));
                }

                int rows = 0;
                for(int y = 0; y < imagenMatriz.rows(); y++) {
                    for(int x = 0; x < imagenMatriz.cols(); x++) {
                        int label = (int)labels.get(rows, 0)[0];
                        int r = (int)centroide.get(label, 2)[0];
                        int g = (int)centroide.get(label, 1)[0];
                        int b = (int)centroide.get(label, 0)[0];
                        clusters.get(label).put(y, x, b, g, r);
                        rows++;
                    }
                }

                ///////////////////////////////////////////////////////mejora imagen celulas malignas///////////////

    }

    private void guardarConfiguracionCalibracion() {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
//        configuraciones= sharedPreferences.getString("microscopio","") + "\n" +
//                "Objetivo: " + sharedPreferences.getString("aumento","4") + "X" +
//                " " + "-" + " " + "Patron: " + sharedPreferences.getString("patron","10") + "um" +
//                " " + "-" + " " + "Escala: "+ String.format("%.3f",escala) + "\n";
//

        configuraciones=sharedPreferences.getString("microscopio","Desconocido") + "\n" +
                        sharedPreferences.getString("aumento","4") + "\n" +
                        sharedPreferences.getString("patron","10") + "\n" +
                        String.valueOf(escala) + "\n";

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("escala",String.valueOf(escala));
        editor.apply();

        try
        {
            File ruta_sd = getExternalFilesDir(null);
            File f = new File(ruta_sd, "configuraciones.txt");
            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            new FileOutputStream(f,true));
            fout.write(configuraciones);
            fout.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }
        mostrarEnToolbar();


//        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
//
//        int aumento=Integer.parseInt(sharedPreferences.getString("aumento","4"));
//        float patron=Float.parseFloat(sharedPreferences.getString("patron","10"));
//
//        String nombreConfiguracion=sharedPreferences.getString("aumento","4")+
//                sharedPreferences.getString("patron","10");
//
//        nombresConfiguraciones.add(nombreConfiguracion);
//
//        SharedPreferences preferences=getSharedPreferences(nombreConfiguracion,MODE_PRIVATE);
//        SharedPreferences.Editor editor= preferences.edit();
//
//        editor.putInt("aumento",aumento);
//        editor.putFloat("patron",patron);
//        editor.putFloat("escala",escala);
//        editor.commit();

    }

}
