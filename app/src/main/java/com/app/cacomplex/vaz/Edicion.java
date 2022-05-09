package com.app.cacomplex.vaz;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
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
import android.text.InputType;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Edicion extends AppCompatActivity {



    LinearLayout linearLayout;
    LinearLayout linearLayoutControles;
    FrameLayout frameLayout;
    ImageView zoomBoton;
    vista vista;
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

    ArrayList<Path>  paths=new ArrayList<Path>();
    ArrayList<Paint> paints=new ArrayList<Paint>();
    ArrayList<Bitmap> bitmapFiguras=new ArrayList<Bitmap>();
    ArrayList<Integer> xf=new ArrayList<Integer>();
    ArrayList<Integer> yf=new ArrayList<Integer>();


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
    boolean touchDentroVista=false;
    boolean touchFueraVista=true;
    private  float densidad;
    /////////////////////////////////////////////////
    private ImageButton lapizBoton;
    boolean activo=false;
    private ImageButton flechaBoton;
    private VistaColor colorImagen;
    private RelativeLayout RelativeLayoutControles;
    private SeekBar seekbar;
    private ImageButton grosorLapiz;
    private float tamañoStroke=1*densidad;
    ArrayList<Float> tamañoStrokeArray=new ArrayList<Float>();
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
    private VistaColor vistaColorTexto;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

       //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setTitle("Edición");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Drawable fondo = getResources().getDrawable(R.drawable.fondo);
        getSupportActionBar().setBackgroundDrawable(fondo);

        linearLayout=(LinearLayout)findViewById(R.id.l1);
        frameLayout=(FrameLayout) findViewById(R.id.frame1);

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        densidad = getResources().getDisplayMetrics().density;
        Log.i("densidad",String.valueOf(densidad));

        //zoomBoton=(ImageView)findViewById(R.id.zoomButton);
        //imageView=(ImageView)findViewById(R.id.imageView);

        iniciarLayoutTexto();
        onClickControlesTexto();

        iniciarLayoutControles();
        onClickControles();


        decorView = getWindow().getDecorView();
        //Hace que desaparezca el Status Bar, el navigation bar y action bar
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        oculto=true;



        //Detecta en que fase se encuentra decorView
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            //getSupportActionBar().show();
//                            layoutParams.setMargins(50,20,20,120);
//                            frameLayout.removeView(zoomBoton);
//                            frameLayout.addView(zoomBoton);
                            RelativeLayoutControles.setVisibility(View.VISIBLE);
                            relativeLayoutTexto.setVisibility(View.VISIBLE);

                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                            getSupportActionBar().hide();
                            //layoutParams.setMargins(50,20,20,50);
//                            frameLayout.removeView(zoomBoton);
                            //frameLayout.addView(zoomBoton);
                            RelativeLayoutControles.setVisibility(View.INVISIBLE);
                            relativeLayoutTexto.setVisibility(View.INVISIBLE);
                        }
                    }
                });

        mDetector = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.i("onSingle","entro");
                if(!oculto) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                    oculto=true;
                }else {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    getSupportActionBar().show();
                    oculto=false;
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });

        recuperado=savedInstanceState;

        if(savedInstanceState==null) {

            color=Color.RED;

//            colorImagen.setBackgroundColor(color);

            vista = new vista(this);
            //linearLayout.addView(vista);
            frameLayout.addView(vista);
            frameLayout.addView(RelativeLayoutControles);

        }else {
//            /*
//            String memoria=savedInstanceState.getString("bitmap",null);
//            byte[] data = Base64.decode(memoria , Base64.DEFAULT);
//            bitmapRecuperado = BitmapFactory.decodeByteArray(data, 0, data.length);
//            */
//            /*
//
//            SharedPreferences prefs =getSharedPreferences("paths",Context.MODE_PRIVATE);
//            Gson gson = new Gson(); //Instancia Gson.
//            String json = prefs.getString("path", "");
//            pathRecuperado=gson.fromJson(json, Path.class);
//
//            */
            color=savedInstanceState.getInt("color",0);
            destruido=savedInstanceState.getBoolean("destruido",false);


//            colorImagen.setBackgroundColor(color);

            vista = new vista(this);
            //linearLayout.addView(vista);
            frameLayout.addView(vista);
            frameLayout.addView(RelativeLayoutControles);
        }

    }

    private void onClickControles() {

        zoomBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom=true;
                flecha=false;
                lapiz=false;
                cGrosorFlecha=false;
                cGrosorLapiz=false;
                touchFueraVista=true;
                touchDentroVista=false;
                /*if(vista.getScaleX()==1 && vista.getScaleY()==1) {
                    vista.setScaleY(2);
                    vista.setScaleX(2);
                }else{
                    vista.setScaleY(1);
                    vista.setScaleX(1);
                }*/

                zoomBoton.setActivated(true);
                lapizBoton.setActivated(false);
                flechaBoton.setActivated(false);
                grosorLapiz.setActivated(false);
                botonTexto.setActivated(false);

                seekbar.setVisibility(View.INVISIBLE);

                invalidateOptionsMenu();
            }
        });
        //llama al onClick de zoomBoton
        zoomBoton.performClick();


        lapizBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                touchFueraVista=false;
                touchDentroVista=true;
                lapiz=true;
                flecha=false;
                medir=false;
                zoom=false;

                lapizBoton.setActivated(true);
                zoomBoton.setActivated(false);
                flechaBoton.setActivated(false);
                grosorLapiz.setActivated(false);
                botonTexto.setActivated(false);

                cGrosorLapiz=true;
                cGrosorFlecha=false;

                if(indice>-1) {
                    float tsa = tamañoStrokeArray.get(indice);
                    seekbar.setProgress((int) (tsa / densidad));
                    seekbar.setVisibility(View.INVISIBLE);
                }

                invalidateOptionsMenu();
            }
        });

        flechaBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                touchFueraVista=false;
                touchDentroVista=true;
                flecha=true;
                lapiz=false;
                medir=false;
                zoom=false;


                flechaBoton.setActivated(true);
                zoomBoton.setActivated(false);
                lapizBoton.setActivated(false);
                grosorLapiz.setActivated(false);
                botonTexto.setActivated(false);

                cGrosorFlecha=true;
                cGrosorLapiz=false;
                if(!flechaShapeDrawable.isEmpty()) {
                    float tfa = tamañoFlechaArrayW.get(numeroFlecha);
                    seekbar.setProgress((int) (tfa / Math.pow(densidad, 3)));
                }else{
                    generarFlecha();
                }
                seekbar.setVisibility(View.INVISIBLE);

                invalidateOptionsMenu();
            }
        });

        grosorLapiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lapizBoton.isActivated()){
//                    seekbar.setProgress((int)());
                    cGrosorLapiz=true;
                    cGrosorFlecha=false;
                    lapizBoton.setActivated(true);

                    botonTexto.setActivated(false);
                    zoomBoton.setActivated(false);
                    grosorLapiz.setActivated(true);

                    seekbar.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                }else if(flechaBoton.isActivated()){
//                    seekbar.setProgress((int)());
                    cGrosorFlecha=true;
                    cGrosorLapiz=false;
                    flechaBoton.setActivated(true);
                    botonTexto.setActivated(false);
                    zoomBoton.setActivated(false);
                    grosorLapiz.setActivated(true);

                    seekbar.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                }
            }
        });

        botonTexto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        touchFueraVista=false;
                        touchDentroVista=true;
                        flecha=false;
                        lapiz=false;
                        medir=false;
                        zoom=false;

                        flechaBoton.setActivated(false);
                        zoomBoton.setActivated(false);
                        lapizBoton.setActivated(false);
                        grosorLapiz.setActivated(false);
                        botonTexto.setActivated(true);

                        currentTexto++;
                        numeroTexto++;

                        xTexts.add((int)(150*densidad));
                        yTexts.add((int)(350*densidad));

                        strings.add("texto...");

                        textsPaintCanvas.add(new TextPaint(TextPaint.ANTI_ALIAS_FLAG));
                        textsPaintCanvas.get(currentTexto).setColor(color);
                        textsPaintCanvas.get(currentTexto).setTextSize(15*densidad);
                        textsPaintCanvas.get(currentTexto).setStrokeWidth(5*densidad);
                        textsPaintCanvas.get(currentTexto).setTypeface(Typeface.SERIF);

                        frameLayout.removeView(RelativeLayoutControles);
                        frameLayout.addView(relativeLayoutTexto);
                        invalidateOptionsMenu();
                    }
                });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cambiarTamaño(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void onClickControlesTexto() {
        grosorTexto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setActivated(true);
                seekBarTexto.setVisibility(View.VISIBLE);
            }
        });

        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.removeView(relativeLayoutTexto);
                frameLayout.addView(RelativeLayoutControles);
                zoomBoton.performClick();
            }
        });

        botonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                frameLayout.removeView(relativeLayoutTexto);
                frameLayout.addView(RelativeLayoutControles);

                if(currentTexto>-1) {
                    xTexts.remove(currentTexto);
                    yTexts.remove(currentTexto);

                    strings.remove(currentTexto);

                    textsPaintCanvas.remove(currentTexto);


                    EditTextCanvas.setText("");

                    currentTexto--;
                    numeroTexto--;
                }

                zoomBoton.performClick();
            }
        });

        seekBarTexto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                aumentoTamañoTexto(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void iniciarLayoutControles() {
        RelativeLayoutControles =new RelativeLayout(this);
        RelativeLayoutControles.setPadding((int)(15*densidad),(int)(15*densidad),(int)(15*densidad),(int)(15*densidad));
        RelativeLayoutControles.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.BOTTOM|Gravity.START));
        RelativeLayoutControles.setBackgroundResource(R.drawable.degradado_bar_options);
//        linearLayoutControles.setOrientation(LinearLayout.HORIZONTAL);


        Drawable drawable=getResources().getDrawable(R.drawable.boton_zoom);

//        final FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(100,100,Gravity.START|Gravity.BOTTOM);
//        layoutParams.setMargins((int)(50*densidad),(int)(20*densidad),(int)(20*densidad),(int)(50*densidad));
        final RelativeLayout.LayoutParams layoutParamsSeekbar=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(40*densidad));
        layoutParamsSeekbar.setMargins(0,0,0,(int)(10*densidad));
        layoutParamsSeekbar.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        layoutParamsSeekbar.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);
        layoutParamsSeekbar.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);

        final RelativeLayout.LayoutParams layoutParamsZoom=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsZoom.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsZoom.setMarginEnd((int)(10*densidad));
        layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        layoutParamsZoom.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);

        final RelativeLayout.LayoutParams layoutParamsLapiz=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsLapiz.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsLapiz.setMarginEnd((int)(10*densidad));


        final RelativeLayout.LayoutParams layoutParamsFlecha=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsFlecha.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsFlecha.setMarginEnd((int)(10*densidad));


        final RelativeLayout.LayoutParams layoutParamsGrosor=new RelativeLayout.LayoutParams((int)(40*densidad), (int)(40*densidad));
        layoutParamsGrosor.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsGrosor.setMarginEnd((int)(10*densidad));

        final RelativeLayout.LayoutParams layoutParamsBotonTexto=new RelativeLayout.LayoutParams((int)(40*densidad), (int)(40*densidad));
        layoutParamsBotonTexto.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsBotonTexto.setMarginEnd((int)(10*densidad));

        final RelativeLayout.LayoutParams layoutParamsColor=new RelativeLayout.LayoutParams((int)(40*densidad), (int)(40*densidad));
        layoutParamsColor.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        layoutParamsColor.addRule(RelativeLayout.ALIGN_PARENT_END,RelativeLayout.TRUE);

        seekbar=new SeekBar(this);
        seekbar.setId(View.generateViewId());
        seekbar.setProgress((int)(1*densidad));
        seekbar.setMax(30);
        seekbar.setLayoutParams(layoutParamsSeekbar);
        seekbar.setVisibility(View.INVISIBLE);

        layoutParamsZoom.addRule(RelativeLayout.BELOW,seekbar.getId());

        zoomBoton=new ImageButton(this);
        zoomBoton.setId(R.id.zoomBoton);
        zoomBoton.setBackground(drawable);
        zoomBoton.setImageResource(R.drawable.ic_zoom_in_white_36dp);
        zoomBoton.setLayoutParams(layoutParamsZoom);


//        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        layoutParams.addRule(RelativeLayout.RIGHT_OF,zoomBoton.getId());
        layoutParamsLapiz.addRule(RelativeLayout.RIGHT_OF,zoomBoton.getId());
        layoutParamsLapiz.addRule(RelativeLayout.END_OF,zoomBoton.getId());
        layoutParamsLapiz.addRule(RelativeLayout.ALIGN_BOTTOM,zoomBoton.getId());

        lapizBoton=new ImageButton(this);
        lapizBoton.setId(View.generateViewId());
        lapizBoton.setBackgroundResource(R.drawable.boton_lapiz);
        lapizBoton.setImageResource(R.drawable.ic_action_lapiz);
        lapizBoton.setLayoutParams(layoutParamsLapiz);

//        layoutParams.removeRule(RelativeLayout.RIGHT_OF);
//        layoutParams.addRule(RelativeLayout.RIGHT_OF,lapizBoton.getId());
        layoutParamsFlecha.addRule(RelativeLayout.RIGHT_OF,lapizBoton.getId());
        layoutParamsFlecha.addRule(RelativeLayout.END_OF,lapizBoton.getId());
        layoutParamsFlecha.addRule(RelativeLayout.ALIGN_BOTTOM,lapizBoton.getId());


        flechaBoton=new ImageButton(this);
        flechaBoton.setId(ImageButton.generateViewId());
        flechaBoton.setBackgroundResource(R.drawable.boton_lapiz);
        flechaBoton.setImageResource(R.drawable.ic_action_flecha);
        flechaBoton.setLayoutParams(layoutParamsFlecha);

//        layoutParams.removeRule(RelativeLayout.RIGHT_OF);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);

        layoutParamsGrosor.addRule(RelativeLayout.RIGHT_OF,flechaBoton.getId());
        layoutParamsGrosor.addRule(RelativeLayout.END_OF,flechaBoton.getId());
        layoutParamsGrosor.addRule(RelativeLayout.ALIGN_BOTTOM,flechaBoton.getId());


        grosorLapiz=new ImageButton(this);
        grosorLapiz.setId(View.generateViewId());
        grosorLapiz.setBackgroundResource(R.drawable.boton_lapiz);
        grosorLapiz.setContentDescription("Grosor del lapiz");
        grosorLapiz.setImageResource(R.drawable.ic_action_tamanho_lapiz);
        grosorLapiz.setLayoutParams(layoutParamsGrosor);

        layoutParamsBotonTexto.addRule(RelativeLayout.RIGHT_OF,grosorLapiz.getId());
        layoutParamsBotonTexto.addRule(RelativeLayout.END_OF,grosorLapiz.getId());
        layoutParamsBotonTexto.addRule(RelativeLayout.ALIGN_BOTTOM,grosorLapiz.getId());

        botonTexto=new ImageButton(this);
        botonTexto.setId(View.generateViewId());
        botonTexto.setBackgroundResource(R.drawable.boton_lapiz);
        botonTexto.setContentDescription("Modo texto");
        botonTexto.setImageResource(R.drawable.ic_action_text);
        botonTexto.setLayoutParams(layoutParamsBotonTexto);


        layoutParamsColor.addRule(RelativeLayout.ALIGN_BOTTOM,flechaBoton.getId());

        colorImagen=new VistaColor(this);
        colorImagen.setContentDescription("Color actualmente");
        colorImagen.setLayoutParams(layoutParamsColor);

        RelativeLayoutControles.addView(zoomBoton);
        RelativeLayoutControles.addView(lapizBoton);
        RelativeLayoutControles.addView(flechaBoton);
        RelativeLayoutControles.addView(seekbar);
        RelativeLayoutControles.addView(colorImagen);
        RelativeLayoutControles.addView(grosorLapiz);
        RelativeLayoutControles.addView(botonTexto);


    }

    private void iniciarLayoutTexto() {
        relativeLayoutTexto=new RelativeLayout(this);
        relativeLayoutTexto.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.BOTTOM|Gravity.START));
        relativeLayoutTexto.setPadding((int)(15*densidad),(int)(15*densidad),(int)(15*densidad),(int)(15*densidad));
        relativeLayoutTexto.setBackgroundResource(R.drawable.degradado_bar_options);
//        getResources().getDisplayMetrics().widthPixels/2;

        RelativeLayout.LayoutParams layoutParamsSeekbarTexto=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(40*densidad));
        layoutParamsSeekbarTexto.setMargins(0,0,0,(int)(10*densidad));
        layoutParamsSeekbarTexto.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        layoutParamsSeekbarTexto.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);
        layoutParamsSeekbarTexto.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);

        seekBarTexto=new SeekBar(this);
        seekBarTexto.setId(View.generateViewId());
        seekBarTexto.setProgress((int)(1*densidad));
        seekBarTexto.setMax(100);
        seekBarTexto.setLayoutParams(layoutParamsSeekbarTexto);
        seekBarTexto.setVisibility(View.INVISIBLE);


        RelativeLayout.LayoutParams layoutParamsTexto=new RelativeLayout.LayoutParams((int)(120*densidad), ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTexto.setMarginEnd((int)(20*densidad));
        layoutParamsTexto.setMargins(0,0,(int)(20*densidad),(int)(20*densidad));
        layoutParamsTexto.addRule(RelativeLayout.BELOW,seekBarTexto.getId());
        layoutParamsTexto.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        layoutParamsTexto.addRule(RelativeLayout.ALIGN_PARENT_START,RelativeLayout.TRUE);

        EditTextCanvas=new EditText(this);
        EditTextCanvas.setId(View.generateViewId());
        EditTextCanvas.setHint("Texto...");
        EditTextCanvas.setTextSize(6*densidad);
        EditTextCanvas.setInputType(InputType.TYPE_CLASS_TEXT);
        EditTextCanvas.setLayoutParams(layoutParamsTexto);

        RelativeLayout.LayoutParams layoutParamsGrosorTexto=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsGrosorTexto.setMarginEnd((int)(10*densidad));
        layoutParamsGrosorTexto.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsGrosorTexto.addRule(RelativeLayout.RIGHT_OF,EditTextCanvas.getId());
        layoutParamsGrosorTexto.addRule(RelativeLayout.END_OF,EditTextCanvas.getId());
        layoutParamsGrosorTexto.addRule(RelativeLayout.ALIGN_BOTTOM,EditTextCanvas.getId());

        grosorTexto=new ImageButton(this);
        grosorTexto.setId(View.generateViewId());
        grosorTexto.setBackgroundResource(R.drawable.boton_lapiz);
        grosorTexto.setContentDescription("Grosor del lapiz");
        grosorTexto.setImageResource(R.drawable.ic_action_tamanho_lapiz);
        grosorTexto.setLayoutParams(layoutParamsGrosorTexto);

        RelativeLayout.LayoutParams layoutParamsOk=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsOk.setMarginEnd((int)(10*densidad));
        layoutParamsOk.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsOk.addRule(RelativeLayout.RIGHT_OF,grosorTexto.getId());
        layoutParamsOk.addRule(RelativeLayout.END_OF,grosorTexto.getId());
        layoutParamsOk.addRule(RelativeLayout.ALIGN_BOTTOM,grosorTexto.getId());

        botonOk=new ImageButton(this);
        botonOk.setId(View.generateViewId());
        botonOk.setBackgroundResource(R.drawable.boton_lapiz);
        botonOk.setContentDescription("Terminar escritura");
        botonOk.setImageResource(R.drawable.ic_action_ok);
        botonOk.setLayoutParams(layoutParamsOk);

        RelativeLayout.LayoutParams layoutParamsCancel=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsCancel.setMarginEnd((int)(10*densidad));
        layoutParamsCancel.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsCancel.addRule(RelativeLayout.RIGHT_OF,botonOk.getId());
        layoutParamsCancel.addRule(RelativeLayout.END_OF,botonOk.getId());
        layoutParamsCancel.addRule(RelativeLayout.ALIGN_BOTTOM,botonOk.getId());

        botonCancel=new ImageButton(this);
        botonCancel.setId(View.generateViewId());
        botonCancel.setBackgroundResource(R.drawable.boton_lapiz);
        botonCancel.setContentDescription("Cancelar escritura");
        botonCancel.setImageResource(R.drawable.ic_action_text_close);
        botonCancel.setLayoutParams(layoutParamsCancel);

        RelativeLayout.LayoutParams layoutParamsColorTexto=new RelativeLayout.LayoutParams((int)(40*densidad),(int)(40*densidad));
        layoutParamsColorTexto.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        layoutParamsColorTexto.addRule(RelativeLayout.ALIGN_PARENT_END,RelativeLayout.TRUE);
        layoutParamsColorTexto.addRule(RelativeLayout.ALIGN_BOTTOM,botonCancel.getId());

        vistaColorTexto=new VistaColor(this);
        vistaColorTexto.setContentDescription("Color actualmente");
        vistaColorTexto.setLayoutParams(layoutParamsColorTexto);


        relativeLayoutTexto.addView(EditTextCanvas);
        relativeLayoutTexto.addView(grosorTexto);
        relativeLayoutTexto.addView(botonOk);
        relativeLayoutTexto.addView(seekBarTexto);
        relativeLayoutTexto.addView(botonCancel);
        relativeLayoutTexto.addView(vistaColorTexto);


    }

    private void aumentoTamañoTexto(int progress) {
        textsPaintCanvas.get(numeroTexto).setTextSize(progress*densidad);
        vista.invalidate();
        vistaColorTexto.invalidate();
    }

    private void cambiarTamaño(int progress) {
        if(cGrosorLapiz) {
            if(indice>-1)
                tamañoStrokeArray.set(indice, progress * densidad);

        }else if(cGrosorFlecha){
            tamañoFlechaArrayW.set(numeroFlecha, progress * (float) Math.pow(densidad,3));
            tamañoFlechaArrayH.set(numeroFlecha, (progress * (float) Math.pow(densidad,3))/3);
        }
//        if()
//        radioInval=(int)Math.hypot(tamañoFlechaArray.get(currentBitmap),tamañoFlechaArray.get(currentBitmap));
//        cenX=(int)(xf.get(currentBitmap)-tamañoFlechaArray.get(currentBitmap)/2);
//        cenY=(int)(yf.get(currentBitmap)-tamañoFlechaArray.get(currentBitmap)/2);
        vista.invalidate();
        colorImagen.invalidate();
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
        guardarImagen(null);
        galleryAddPic();
    }

    public void guardarImagen(View v){

        if(bitmap!=null) {
            FileOutputStream outputStream = null;
            File file =createFilePath();
            VistaPrevia.currentFileName=file.getAbsolutePath();
            VistaPrevia.selectedImage=Uri.fromFile(file);
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
    public class VistaColor extends View{

        float ancho,alto;
        TextPaint textPaint;
        StaticLayout staticLayout;
        String string;
        Rect rectText;

        private final Paint paint;
        private float radio;

        public VistaColor(Context context) {
            super(context);

            string="A";
            textPaint=new TextPaint();
            rectText=new Rect();
            paint=new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(1*densidad);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            setBackgroundColor(0x000000);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);


            ancho=canvas.getWidth();
            alto=canvas.getHeight();

//            paint.setColor(color);
//            paint.setAlpha(20);

            if(ancho<alto){
                radio=ancho/3;
            }else{
                radio=alto/3;
            }
            if(botonTexto.isActivated()){
                textPaint.setColor(color);
                textPaint.setTextSize(textsPaintCanvas.get(numeroTexto).getTextSize());
                textPaint.getTextBounds(string,0,string.length(),rectText);
                canvas.drawText(string,ancho / 2 - rectText.width()/2, alto / 2 + rectText.height()/2,textPaint);
            }else if(lapizBoton.isActivated()&& !tamañoStrokeArray.isEmpty()) {
                paint.setColor(color);
                canvas.drawCircle(ancho / 2, alto / 2, tamañoStrokeArray.get(indice), paint);
            }else{
                paint.setColor(color);
                canvas.drawCircle(ancho / 2, alto / 2, seekbar.getProgress()*densidad , paint);
            }
        }
    }



    public class vista extends View{

        String accion=null;
        Drawable image;
        Paint paintCordenada;
        LinearLayout linearLayout;

        int x;
        int y;
        int xMedirInicial;
        int yMedirInicial;
        int xMedirFinal;
        int yMedirFinal;
//        int distancia;
        private int xi,yi;

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public vista(Context context) {
            super(context);

            if (VistaPrevia.currentFileName!=null) {
//                image= new BitmapDrawable(getResources(),VistaPrevia.currentFileName);
                  image = new BitmapDrawable(getResources(), VistaPrevia.imagen);
            }
            else
                image=getResources().getDrawable(R.drawable.ic_image_black_36dp);

            matrix=new Matrix();
            matrixBuffer=new Matrix();
            paintCordenada = new Paint();
            paintCordenada.setColor(Color.RED);
            paintCordenada.setTextSize(40);
            paintCordenada.setAntiAlias(true);
            paintCordenada.setStyle(Paint.Style.STROKE);

//            paths.add(new Path());
//            tamañoStrokeArray.add(1*densidad);
//            paints.add(new Paint());
//            paints.get(indice).setTextSize(40);
//            paints.get(indice).setAntiAlias(true);
//            paints.get(indice).setStyle(Paint.Style.STROKE);
//            paints.get(indice).setColor(color);
//            paintsFlechas.add(new Paint());

            setBackground(image);
//            setLayerType(LAYER_TYPE_SOFTWARE,paintSofware);


//            xTexts.add(200);
//            yTexts.add(200);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
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

            /*
            float w=canvas.getWidth();
            float h=canvas.getHeight();

            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(VistaPrevia.currentFileName,options);

            int photoW = options.outWidth;
            int photoH = options.outHeight;
            int targetW=canvas.getWidth();
            int targetH=canvas.getHeight();

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            options.inJustDecodeBounds = false;
            options.inSampleSize =scaleFactor;
            options.inPurgeable = true;

            bitmap = BitmapFactory.decodeFile(VistaPrevia.currentFileName,options);

            canvas.drawBitmap(bitmap,0,0,null);
            */


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

            /*
            if(!(imagenX<w && imagenY<h)){
                if (R1 > R2) {
                    ancho = (int) (h / R1);
                    alto = (int) h;
                } else if (R1 < R2) {
                    ancho = (int) w;
                    alto = (int) (R1 * w);
                    altoInicial = (int) (h / 2 - alto / 2);
                    altoFinal = (int) (h / 2 + alto / 2);
                } else if (R1 == R2) {
                    ancho = (int) w;
                    alto = (int) h;
                }

            }
            */

            /*
            paint[indice].setTextSize(40);
            paint[indice].setAntiAlias(true);
            paint[indice].setStyle(Paint.Style.STROKE);
            paint[indice].setStrokeWidth(4);
            */





            Log.i("info","Paso por aqui");

//            /*
//            for(int i=0;i<3;i++) {
//                if(path[i]!=null) {
//                    canvas.drawPath(path[i], paint[i]);
//                }
//            }
//            */
            if(indice>-1) {
//                paints.get(indice).setTextSize(40);
//                paints.get(indice).setAntiAlias(true);
//                paints.get(indice).setStyle(Paint.Style.STROKE);
//                paints.get(indice).setColor(color);

                for (int i = 0; i <= indice; i++) {
                    paints.get(indice).setStrokeWidth(tamañoStrokeArray.get(indice));
                    if ((paths.get(i) != null)) {
                        canvas.drawPath(paths.get(i), paints.get(i));
                    }
                }
            }
//            if(rotarFlecha||rotarFlechaLeft){
//                if(rotarFlecha){
////                    matrix.preRotate(30,wBitmap/2,hBitmap/2);
////                    matrixBuffer.preRotate(30,wBitmap/2,hBitmap/2);
//
//                }
//                if(rotarFlechaLeft){
////                    matrix.preRotate(-30,wBitmap/2,hBitmap/2);
////                    matrixBuffer.preRotate(-30,wBitmap/2,hBitmap/2);
//                }
//
////                bitmapFiguras.set(numeroFlecha,Bitmap.createBitmap(bitmapFiguras.get(numeroFlecha),
////                        0,0,bitmapFiguras.get(numeroFlecha).getWidth(),
////                        bitmapFiguras.get(numeroFlecha).getHeight(),matrix, true));
////                numeroRotacion+=1;
////                matrix.reset();
////
////                if(numeroRotacion>2){
////                    bitmapBuffer=Bitmap.createBitmap(bitmapModificado,0,0,wBitmap,hBitmap,matrixBuffer,true);
////                    bitmapFiguras.remove(numeroFlecha);
////                    bitmapFiguras.add(numeroFlecha,bitmapBuffer);
////                    numeroRotacion=0;
////                }
//
//                rotarFlecha=false;
//                rotarFlechaLeft=false;
//            }
            if(numeroFlecha>-1){
                shapes.get(numeroFlecha).resize(tamañoFlechaArrayW.get(numeroFlecha),tamañoFlechaArrayH.get(numeroFlecha));
            }
            for(int i=0;i<=currentBitmap;i++) {
//////////////////////////////////////////////////////////////////////////////////////

//                canvas.drawBitmap(bitmapFiguras.get(i),
//                        xf.get(i) - bitmapFiguras.get(i).getWidth() / 2,
//                        yf.get(i) - bitmapFiguras.get(i).getHeight()/ 2, null);
/////////////////////////////////////////////////////////////////////////////////////

                xi=(int)(xf.get(i)-shapes.get(i).getWidth()/2);
                yi=(int)(yf.get(i)-shapes.get(i).getHeight()/2);
                cenX=(int)(xf.get(i));
                cenY=(int)(yf.get(i));
                Log.i("flechaAncho",String.valueOf(shapes.get(i).getWidth()));
                Log.i("flechaAlto",String.valueOf(shapes.get(i).getHeight()));
//                flechaShapeDrawable.get(i).setIntrinsicWidth((int) shapes.get(i).getWidth()-xi);
//                flechaShapeDrawable.get(i).setIntrinsicHeight((int) shapes.get(i).getHeight()-yi);
                flechaShapeDrawable.get(i).setBounds(xi,yi
                        ,xi+(int) shapes.get(i).getWidth(),yi+(int) shapes.get(i).getHeight());
                canvas.save();
                canvas.rotate(rotaciones.get(i),cenX,cenY);
                flechaShapeDrawable.get(i).draw(canvas);
                canvas.restore();
                invalidate(cenX-radioInval,cenY-radioInval,cenX+radioInval,cenY+radioInval);
            }
            /////////////////////////////////////////////////////////////////////////////////
                for(int i=0;i<=currentTexto;i++) {
                    if(EditTextCanvas.isFocused())
                        strings.set(currentTexto,EditTextCanvas.getText().toString());
                    canvas.drawText(strings.get(i), xTexts.get(i),
                            yTexts.get(i), textsPaintCanvas.get(i));
                }

//Muestra las coordenadas!
//            if(accion=="down" || accion=="move") {
//                // canvas.drawText("X: " + String.valueOf((int)x)+" Y: "+String.valueOf((int)y),50,50,paint[0]);
//                canvas.drawText("X: " + String.valueOf((int) x) + " Y: " + String.valueOf((int) y), 50*densidad,
//                        100*densidad,paintCordenada);
//            }

        }
        public boolean onTouchEvent(MotionEvent e){
            Edicion.this.mDetector.onTouchEvent(e);
            x=(int)e.getX();
            y=(int)e.getY();
            int sumaFlechas=0;

            if(e.getAction()==MotionEvent.ACTION_DOWN){
                //path[indice].moveTo(x, y);
                if(flecha){
                    for (int i = 0; i <= currentBitmap; i++) {
                        sumaFlechas += shapes.get(i).getWidth();
                    }
                    int promedioFlechas = sumaFlechas / shapes.size();
                    for(int i=0;i<=currentBitmap;i++) {

                        int cenX = x - xf.get(i);
                        int cenY = y - yf.get(i);
                        int distancia=(int)Math.sqrt(cenX*cenX+cenY*cenY);
                        if(distancia<=promedioFlechas/2){
                            numeroFlecha=i;
                        }
                    }
                }
                if(lapiz) {
                    indice+=1;
                    tamañoStrokeArray.add(seekbar.getProgress()*densidad);
                    paths.add(new Path());
                    paints.add(new Paint());
                    paints.get(indice).setColor(color);
                    paints.get(indice).setAntiAlias(true);
                    paints.get(indice).setStyle(Paint.Style.STROKE);
                    paths.get(indice).moveTo(x, y);
                }
                if(botonTexto.isActivated()){
                    for(int i=0;i<=currentTexto;i++) {
                        int cenX = x - xTexts.get(i);
                        int cenY = y - yTexts.get(i);
                        int distancia=(int)Math.sqrt(cenX*cenX+cenY*cenY);
                        Log.i("distancia",String.valueOf(distancia));
                        Log.i("distanciaMax",String.valueOf(Math.abs(textsPaintCanvas.get(0).measureText(strings.get(0)))));
                        if(distancia<=Math.abs(textsPaintCanvas.get(0).measureText(strings.get(0)))){
                            numeroTexto=i;
                        }
                    }

                    Log.i("texto",String.valueOf(numeroTexto));
                    Log.i("textoCurrent",String.valueOf(currentTexto));
                }

                accion="down";
            }

            if(e.getAction()==MotionEvent.ACTION_MOVE) {
                //path[indice].lineTo(x, y);
                if(flecha) {
                    if(numeroFlecha>-1) {
                        xf.set(numeroFlecha, x);
                        yf.set(numeroFlecha, y);
                    }
                }

                if(lapiz) {
                    paths.get(indice).lineTo(x, y);
                }
                if(botonTexto.isActivated()){
                    if(numeroTexto>-1) {
                        xTexts.set(numeroTexto, x);
                        yTexts.set(numeroTexto, y);
                    }
                }

                accion="move";
            }
            if(e.getAction()==MotionEvent.ACTION_UP) {
                // path[indice].moveTo(x, y);
                if(lapiz) {
                    paths.get(indice).moveTo(x, y);

                    Log.i("indice",String.valueOf(indice));

                    Log.i("indice",String.valueOf(indice));
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
        this.mDetector.onTouchEvent(event);
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

        getMenuInflater().inflate(R.menu.menu_edicion,menu);

        if(flechaBoton.isActivated()){
            menu.findItem(R.id.rotarFlecha).setVisible(true);
            menu.findItem(R.id.rotarFlecha2).setVisible(true);
        }else{
            menu.findItem(R.id.rotarFlecha).setVisible(false);
            menu.findItem(R.id.rotarFlecha2).setVisible(false);
        }
        if(botonTexto.isActivated()){
            menu.findItem(R.id.agregarFlecha).setVisible(false);
        }else{
            menu.findItem(R.id.agregarFlecha).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
//            case R.id.aplicarFiltro:
//                //guardarEnCache();
//                Intent intentFiltros=new Intent(this,Filtros.class);
//                startActivity(intentFiltros);
//                break;
//            case R.id.vistaPrevia:
//                //guardarEnCache();
//                Intent intentVista=new Intent(this,VistaPrevia.class);
//                startActivity(intentVista);
//                break;
            case R.id.rotarFlecha2:
                if(currentBitmap>-1) {
                    rotarFlechaLeft = true;
                    rotaciones.set(numeroFlecha,rotaciones.get(numeroFlecha)-12);
                }
                else
                    Toast.makeText(this,"No existe ninguna flecha",Toast.LENGTH_SHORT).show();
                vista.postInvalidate();
                break;
            case R.id.rotarFlecha:
                if(currentBitmap>-1) {
                    rotarFlecha = true;
                    rotaciones.set(numeroFlecha,rotaciones.get(numeroFlecha)+12);
                }
                else
                    Toast.makeText(this,"No existe ninguna flecha",Toast.LENGTH_SHORT).show();
                vista.postInvalidate();
                break;
//            case R.id.lapiz:
//                item.setChecked(true);
//
//                touchFueraVista=false;
//                touchDentroVista=true;
//                lapiz=true;
//                flecha=false;
//                medir=false;
//                zoom=false;
//                break;
//            case R.id.flecha:
//                item.setChecked(true);
//
//                touchFueraVista=false;
//                touchDentroVista=true;
//                flecha=true;
//                lapiz=false;
//                medir=false;
//                zoom=false;
//
//                break;
            case R.id.agregarFlecha:
                generarFlecha();
                break;
//            case R.id.rotar:
//                Toast.makeText(this,"Rote la imagen desde el menu 'Rotar Imagen'"
//                        ,Toast.LENGTH_LONG).show();
//                Intent intent=new Intent(this,VistaPrevia.class);
//                startActivity(intent);
//                break;
            case R.id.guardar:
                guardar(null);
                break;
            case R.id.borrarTodo:

//                for(Path path:paths){
//                    path.reset();
//                }
                if(lapizBoton.isActivated()) {
                    paths.remove(indice);
                    paints.remove(indice);
                    indice -= 1;
                    if(indice<=0) {
                        indice = 0;
                        paths.add(new Path());
                        paints.add(new Paint());
                        paints.get(indice).setColor(color);
                        paints.get(indice).setAntiAlias(true);
                        paints.get(indice).setStyle(Paint.Style.STROKE);
                    }
                }else if(flechaBoton.isActivated()&&currentBitmap>-1){
                    flechaShapeDrawable.remove(currentBitmap);
                    tamañoFlechaArrayW.remove(currentBitmap);
                    tamañoFlechaArrayH.remove(currentBitmap);
                    currentBitmap-=1;
                    numeroFlecha-=1;
                }else if(botonTexto.isActivated()&&currentTexto>-1){
                    strings.remove(currentTexto);
                    textsPaintCanvas.remove(currentTexto);
                    currentTexto-=1;
                    numeroTexto-=1;
                }
//                for(ShapeDrawable shapeDrawable:flechaShapeDrawable){
//                    flechaShapeDrawable.clear();
//                }
//                frameLayout.removeView(vista);
//                frameLayout.addView(vista);
                vista.invalidate();
                break;
            case R.id.verde:
                color=Color.rgb(27,142,32);
//                indice+=1;
                nuevaFlecha=true;
                /*
                path[indice]=new Path();
                paint[indice]=new Paint();
                paint[indice].setColor(color);
                */
                if(flechaBoton.isActivated())
                    flechaShapeDrawable.get(numeroFlecha).getPaint().setColor(color);

                if(botonTexto.isActivated())
                    textsPaintCanvas.get(numeroTexto).setColor(color);

//                tamañoStrokeArray.add((float)seekbar.getProgress()*densidad);

//                tamañoStrokeArray.add(1*densidad);
//                paths.add(new Path());
//                paints.add(new Paint());
                if(indice>-1 && lapizBoton.isActivated()) {
                    paints.get(indice).setColor(color);
                    paints.get(indice).setAntiAlias(true);
                    paints.get(indice).setStyle(Paint.Style.STROKE);
                }
                colorImagen.invalidate();
                vistaColorTexto.invalidate();
                break;
            case R.id.rojo:
                color=Color.rgb(182,28,28);
//                indice+=1;
                nuevaFlecha=true;
                /*
                path[indice]=new Path();
                paint[indice]=new Paint();
                paint[indice].setColor(color);
                */
                if(flechaBoton.isActivated())
                    flechaShapeDrawable.get(numeroFlecha).getPaint().setColor(color);

                if(botonTexto.isActivated())
                    textsPaintCanvas.get(numeroTexto).setColor(color);

//                tamañoStrokeArray.add(1*densidad);
//                paths.add(new Path());
//                paints.add(new Paint());
                if(indice>-1 && lapizBoton.isActivated()) {
                    paints.get(indice).setColor(color);
                    paints.get(indice).setAntiAlias(true);
                    paints.get(indice).setStyle(Paint.Style.STROKE);
                }
                colorImagen.invalidate();
                vistaColorTexto.invalidate();
                break;
            case R.id.azul:
                color=Color.rgb(0,0,147);
//                indice+=1;
                nuevaFlecha=true;
                /*
                path[indice]=new Path();
                paint[indice]=new Paint();
                paint[indice].setColor(color);
                */
                if(flechaBoton.isActivated())
                    flechaShapeDrawable.get(numeroFlecha).getPaint().setColor(color);

                if(botonTexto.isActivated())
                    textsPaintCanvas.get(numeroTexto).setColor(color);

//                tamañoStrokeArray.add(1*densidad);
//                paths.add(new Path());
//                paints.add(new Paint());
                if(indice>-1 && lapizBoton.isActivated()) {
                    paints.get(indice).setColor(color);
                    paints.get(indice).setAntiAlias(true);
                    paints.get(indice).setStyle(Paint.Style.STROKE);
                }
                colorImagen.invalidate();
                vistaColorTexto.invalidate();
                break;
            case R.id.amarillo:
                color=Color.rgb(251,226,3);
//                indice+=1;
                nuevaFlecha=true;
                /*
                path[indice]=new Path();
                paint[indice]=new Paint();
                paint[indice].setColor(color);
                */
                if(flechaBoton.isActivated())
                    flechaShapeDrawable.get(numeroFlecha).getPaint().setColor(color);

                if(botonTexto.isActivated())
                    textsPaintCanvas.get(numeroTexto).setColor(color);

//                tamañoStrokeArray.add(1*densidad);
//                paths.add(new Path());
//                paints.add(new Paint());
                if(indice>-1 && lapizBoton.isActivated()) {
                    paints.get(indice).setColor(color);
                    paints.get(indice).setAntiAlias(true);
                    paints.get(indice).setStyle(Paint.Style.STROKE);
                }
                colorImagen.invalidate();
                vistaColorTexto.invalidate();
                break;
            case R.id.magenta:
                color=Color.rgb(220,30,94);
//                indice+=1;
                nuevaFlecha=true;
                /*
                path[indice]=new Path();
                paint[indice]=new Paint();
                paint[indice].setColor(color);
                */
                if(flechaBoton.isActivated())
                    flechaShapeDrawable.get(numeroFlecha).getPaint().setColor(color);

                if(botonTexto.isActivated())
                    textsPaintCanvas.get(numeroTexto).setColor(color);

//                tamañoStrokeArray.add(1*densidad);
//                paths.add(new Path());
//                paints.add(new Paint());
                if(indice>-1 && lapizBoton.isActivated()) {
                    paints.get(indice).setColor(color);
                    paints.get(indice).setAntiAlias(true);
                    paints.get(indice).setStyle(Paint.Style.STROKE);
                }
                colorImagen.invalidate();
                vistaColorTexto.invalidate();
                break;
            case R.id.celeste:
                color=Color.rgb(100,190,230);
//                indice+=1;
                nuevaFlecha=true;
                /*
                path[indice]=new Path();
                paint[indice]=new Paint();
                paint[indice].setColor(color);
                */
                if(flechaBoton.isActivated())
                    flechaShapeDrawable.get(numeroFlecha).getPaint().setColor(color);

                if(botonTexto.isActivated())
                    textsPaintCanvas.get(numeroTexto).setColor(color);

//                tamañoStrokeArray.add(1*densidad);
//                paths.add(new Path());
//                paints.add(new Paint());
                if(indice>-1 && lapizBoton.isActivated()) {
                    paints.get(indice).setColor(color);
                    paints.get(indice).setAntiAlias(true);
                    paints.get(indice).setStyle(Paint.Style.STROKE);
                }
                colorImagen.invalidate();
                vistaColorTexto.invalidate();
                break;
            case R.id.negro:

                color=Color.BLACK;
//                indice+=1;
                nuevaFlecha=true;
                /*
                path[indice]=new Path();
                paint[indice]=new Paint();
                paint[indice].setColor(color);
                */
                if(flechaBoton.isActivated())
                    flechaShapeDrawable.get(numeroFlecha).getPaint().setColor(color);

                if(botonTexto.isActivated())
                    textsPaintCanvas.get(numeroTexto).setColor(color);

//                tamañoStrokeArray.add(1*densidad);
//                paths.add(new Path());
//                paints.add(new Paint());
                if(indice>-1 && lapizBoton.isActivated()) {
                    paints.get(indice).setColor(color);
                    paints.get(indice).setAntiAlias(true);
                    paints.get(indice).setStyle(Paint.Style.STROKE);
                }
                colorImagen.invalidate();
                vistaColorTexto.invalidate();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void generarFlecha() {
        //Ver de utilizar los gráficos vectoriales!!
        touchFueraVista=false;
        touchDentroVista=true;
        flecha=true;
        lapiz=false;
        medir=false;
        zoom=false;
        zoomBoton.setActivated(false);
        lapizBoton.setActivated(false);
        grosorLapiz.setActivated(false);
        seekbar.setVisibility(View.INVISIBLE);
        flechaBoton.setActivated(true);
        currentBitmap+=1;
        numeroFlecha+=1;
        matrixBuffer.reset();
        invalidateOptionsMenu();
        //Colocar la flecha debidamente en la pantalla
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
            xPaint = 150;
            yPaint = 300;
        }
        ////////////////////////////////////////////

        xf.add(currentBitmap,(int)(xPaint+25*densidad));
        yf.add(currentBitmap,(int)(yPaint+60*densidad));

        if(nuevaFlecha) {
//            Path pFlecha=new Path();
//            pFlecha.moveTo(0.0f,0.2f);
//            pFlecha.lineTo(0.4f,0.2f);
//            pFlecha.lineTo(0.4f,0.3f);
//            pFlecha.lineTo(0.6f,0.15f);
//            pFlecha.lineTo(0.4f,0.0f);
//            pFlecha.lineTo(0.4f,0.1f);
//            pFlecha.lineTo(0.0f,0.1f);
//            pFlecha.lineTo(0.0f,0.2f);

                shapes.add(new Shape() {
                    float w,h;
                    @Override
                    protected void onResize(float width, float height) {
                        super.onResize(width, height);
                        w=width;
                        h=height;
                    }
                    @Override
                public void draw(Canvas canvas, Paint paint) {
//                        paintsFlechas.get(currentBitmap).setColor(color);
                        Path pFlecha=new Path();
                        pFlecha.moveTo(0.0f*w,0.6f*h);
                        pFlecha.lineTo(0.65f*w,0.6f*h);
                        pFlecha.lineTo(0.65f*w,1f*h);
                        pFlecha.lineTo(1f*w,0.5f*h);
                        pFlecha.lineTo(0.65f*w,0.0f*h);
                        pFlecha.lineTo(0.65f*w,0.4f*h);
                        pFlecha.lineTo(0.0f*w,0.4f*h);
                        pFlecha.lineTo(0.0f*w,0.6f*h);
                    canvas.drawPath(pFlecha, paint);
                }
            });
            tamañoFlechaArrayW.add(10*(float)Math.pow(densidad,3));
            tamañoFlechaArrayH.add((10*(float)Math.pow(densidad,3))/3);
            shapes.get(currentBitmap).resize(tamañoFlechaArrayW.get(currentBitmap),tamañoFlechaArrayH.get(currentBitmap));
            radioInval=(int)Math.hypot(tamañoFlechaArrayW.get(currentBitmap),tamañoFlechaArrayH.get(currentBitmap));
            cenX=(int)(xf.get(currentBitmap)-tamañoFlechaArrayW.get(currentBitmap)/2);
            cenY=(int)(yf.get(currentBitmap)-tamañoFlechaArrayH.get(currentBitmap)/2);
            rotaciones.add(0);
            flechaShapeDrawable.add(new ShapeDrawable(shapes.get(currentBitmap)));
            flechaShapeDrawable.get(currentBitmap).getPaint().setColor(color);

            Log.i("flechaAncho",String.valueOf(shapes.get(currentBitmap).getWidth()));
            Log.i("flechaAlto",String.valueOf(shapes.get(currentBitmap).getHeight()));

//            flechaShapeDrawable=new ShapeDrawable(new PathShape(pFlecha,1,1));
//            flechaShapeDrawable=new ShapeDrawable(shape);
////            paintFlecha.setStrokeWidth(1);
//
////            paintSofware= flechaShapeDrawable.getPaint();
////            paintSofware.setColor(color);
////            paintSofware.setStyle(Paint.Style.FILL_AND_STROKE);
////            flechaShapeDrawable.setIntrinsicWidth(500);
////            flechaShapeDrawable.setIntrinsicHeight(300);
//            float w=flechaShapeDrawable.getIntrinsicHeight();
//            float h=flechaShapeDrawable.getIntrinsicHeight();
//            flechaDrawable=flechaShapeDrawable;

//            Log.i("FlechaSDrawable",String.valueOf(w));
//            Log.i("FlechaSDrawable2",String.valueOf(h));


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;
//            options.inMutable = true;
//            bitmapFiguras.add(BitmapFactory.decodeResource(getResources(), R.drawable.flecha2, options));
//
//            wBitmap = bitmapFiguras.get(currentBitmap).getWidth();
//            hBitmap = bitmapFiguras.get(currentBitmap).getHeight();
//
//            bitmapModificado = Bitmap.createBitmap(wBitmap, hBitmap, Bitmap.Config.ARGB_8888);
//
//            for (int i = 0; i < wBitmap; i++) {
//                for (int j = 0; j < hBitmap; j++) {
//                    r = Color.red(bitmapFiguras.get(currentBitmap).getPixel(i, j));
//                    g = Color.green(bitmapFiguras.get(currentBitmap).getPixel(i, j));
//                    b = Color.blue(bitmapFiguras.get(currentBitmap).getPixel(i, j));
//                    a = Color.alpha(bitmapFiguras.get(currentBitmap).getPixel(i, j));
//                    if ((r > 200) && (g > 200) && (b > 200)) {
//                        bitmapModificado.setPixel(i, j, Color.argb(0, r, g, b));
//                    } else {
//                        bitmapModificado.setPixel(i, j, color);
//                    }
//                }
//            }
//            bitmapFiguras.set(currentBitmap,bitmapModificado);
//            nuevaFlecha=false;
//        }else{
//            bitmapFiguras.add(bitmapModificado);
        }
        vista.invalidate();
    }
    //Cuando se presiona atrás se recrea la vista previa
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        guardarEnCache();
//        Intent intentVista=new Intent(this,VistaPrevia.class);
//        startActivity(intentVista);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /*
        vista.buildDrawingCache();
        Bitmap bitmap=vista.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        String base64_imagen = Base64.encodeToString(byteArray, Base64.DEFAULT);

        outState.putString("bitmap",base64_imagen);
        */



        outState.putBoolean("destruido",true);

        /*
        Path pathRecuperado=paths.get(0);
        SharedPreferences prefs = getSharedPreferences("paths",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();  //Instancia Gson.
        String json = gson.toJson(pathRecuperado); //convierte a .json el objeto
        prefsEditor.putString("path", json);
        prefsEditor.commit();
        */

        outState.putInt("color",color);
        //guardarEnCache();
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

