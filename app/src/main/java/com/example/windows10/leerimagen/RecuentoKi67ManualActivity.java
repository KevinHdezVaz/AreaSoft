package com.example.windows10.leerimagen;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
//todo activarse los botones de colores.
//todo sacar boton grosor y agregar solo la seekbar.
//todo contar rojos y verdes y calcular porcentaje. Por último mostrarlos en posiblemente toolbar.

public class RecuentoKi67ManualActivity extends AppCompatActivity {
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
    private ImageButton circuloBoton;
    boolean activo=false;
    private ImageButton flechaBoton;
    private ImageButton colorBoton;
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
    private boolean obtenerColorP=false,obtenerColorNP;
    private int rMax,rMin,gMax,gMin,bMax,bMin;
    private Bitmap bitmapColor;
    private int xColor,yColor;
    private Path pathObtenerColor;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuento_k67_automatico);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbarKi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable fondo = getResources().getDrawable(R.drawable.fondo);
        getSupportActionBar().setBackgroundDrawable(fondo);

        linearLayout=(LinearLayout)findViewById(R.id.l1);
        linearLayout2=(LinearLayout) findViewById(R.id.l2);
        rojo=(TextView)findViewById(R.id.r);
        verde=(TextView)findViewById(R.id.v);
        total=(TextView)findViewById(R.id.t);
        porcentaje=(TextView)findViewById(R.id.p);

        mScaleDetector = new ScaleGestureDetector(this, new RecuentoKi67ManualActivity.ScaleListener());
        densidad = getResources().getDisplayMetrics().density;
        Log.i("densidad",String.valueOf(densidad));

        iniciarLayoutControles();
        onClickControles();

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
                grosorLapiz.setActivated(false);
                circuloBoton.setActivated(false);
                colorBoton.setActivated(false);

                invalidateOptionsMenu();
            }
        });
        //llama al onClick de zoomBoton
        zoomBoton.performClick();


        circuloBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                touchFueraVista=false;
                touchDentroVista=true;
                circuloVerde=true;
                circuloRojo=false;
                flecha=false;
                medir=false;
                zoom=false;
                color = Color.argb(255,40,220,120);
                zoomBoton.setActivated(false);
                grosorLapiz.setActivated(false);
                circuloBoton.setActivated(true);
                colorBoton.setActivated(false);


                invalidateOptionsMenu();
            }
        });
        colorBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                touchFueraVista=false;
                touchDentroVista=true;
                circuloVerde=false;
                circuloRojo=true;
                flecha=false;
                medir=false;
                zoom=false;
                color = Color.argb(255,200,40,40);
                zoomBoton.setActivated(false);
                grosorLapiz.setActivated(false);
                circuloBoton.setActivated(false);
                colorBoton.setActivated(true);


                invalidateOptionsMenu();
            }
        });

//        grosorLapiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    seekbar.setVisibility(View.VISIBLE);
//                    invalidateOptionsMenu();
//            }
//        });

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
        colorBoton.performClick();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void iniciarLayoutControles() {
        LinearLayout.LayoutParams layoutParamsR=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,0.15f);
        layoutParamsR.setMargins(0,(int)(10*densidad),0,0);

        RelativeLayoutControles =new RelativeLayout(this);
        RelativeLayoutControles.setPadding((int)(5*densidad),(int)(5*densidad),(int)(5*densidad),(int)(5*densidad));
        RelativeLayoutControles.setLayoutParams(layoutParamsR);
        RelativeLayoutControles.setBackgroundColor(Color.DKGRAY);
        RelativeLayoutControles.setGravity(Gravity.CENTER);

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


        final RelativeLayout.LayoutParams layoutParamsGrosor=new RelativeLayout.LayoutParams((int)(40*densidad), (int)(40*densidad));
        layoutParamsGrosor.setMargins(0,0,(int)(10*densidad),0);
        layoutParamsGrosor.setMarginEnd((int)(10*densidad));

//        layoutParamsZoom.addRule(RelativeLayout.BELOW,seekbar.getId());

        zoomBoton=new ImageButton(this);
        zoomBoton.setId(R.id.zoomBoton);
        zoomBoton.setBackground(drawable);
        zoomBoton.setImageResource(R.drawable.ic_zoom_in_white_36dp);
        zoomBoton.setLayoutParams(layoutParamsZoom);

        layoutParamsCirculo.addRule(RelativeLayout.RIGHT_OF,zoomBoton.getId());
        layoutParamsCirculo.addRule(RelativeLayout.END_OF,zoomBoton.getId());
        layoutParamsCirculo.addRule(RelativeLayout.ALIGN_BOTTOM,zoomBoton.getId());

        circuloBoton=new ImageButton(this);
        circuloBoton.setId(View.generateViewId());
        circuloBoton.setBackgroundResource(R.drawable.boton_lapiz);
        circuloBoton.setImageResource(R.drawable.ic_action_circulo);
        circuloBoton.setImageTintList(ColorStateList.valueOf(Color.argb(255,40,220,120)));
        circuloBoton.setLayoutParams(layoutParamsCirculo);


        layoutParamsColor.addRule(RelativeLayout.RIGHT_OF,circuloBoton.getId());
        layoutParamsColor.addRule(RelativeLayout.END_OF,circuloBoton.getId());
        layoutParamsColor.addRule(RelativeLayout.ALIGN_BOTTOM,circuloBoton.getId());


        colorBoton=new ImageButton(this);
        colorBoton.setId(ImageButton.generateViewId());
        colorBoton.setBackgroundResource(R.drawable.boton_lapiz);
        colorBoton.setImageResource(R.drawable.ic_action_circulo);
        colorBoton.setImageTintList(ColorStateList.valueOf(Color.argb(255,200,40,40)));
        colorBoton.setLayoutParams(layoutParamsColor);


        layoutParamsGrosor.addRule(RelativeLayout.RIGHT_OF,colorBoton.getId());
        layoutParamsGrosor.addRule(RelativeLayout.END_OF,colorBoton.getId());
        layoutParamsGrosor.addRule(RelativeLayout.ALIGN_BOTTOM,colorBoton.getId());


        grosorLapiz=new ImageButton(this);
        grosorLapiz.setId(View.generateViewId());
        grosorLapiz.setBackgroundResource(R.drawable.boton_lapiz);
        grosorLapiz.setContentDescription("Grosor del lapiz");
        grosorLapiz.setImageResource(R.drawable.ic_action_tamanho_lapiz);
        grosorLapiz.setLayoutParams(layoutParamsGrosor);

        layoutParamsSeekbar.addRule(RelativeLayout.RIGHT_OF,colorBoton.getId());
        layoutParamsSeekbar.addRule(RelativeLayout.END_OF,colorBoton.getId());
        layoutParamsSeekbar.addRule(RelativeLayout.ALIGN_BOTTOM,colorBoton.getId());

        seekbar=new SeekBar(this);
        seekbar.setId(View.generateViewId());
        seekbar.setProgress((int)(1*densidad));
        seekbar.setMax(20);
        seekbar.setLayoutParams(layoutParamsSeekbar);


        RelativeLayoutControles.addView(zoomBoton);
        RelativeLayoutControles.addView(circuloBoton);
        RelativeLayoutControles.addView(colorBoton);
        RelativeLayoutControles.addView(seekbar);

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
                image = new BitmapDrawable(getResources(), VistaPrevia.imagen);
            }else
                image=getResources().getDrawable(R.drawable.ic_image_black_36dp);

            matrix=new Matrix();
            matrixBuffer=new Matrix();
            paintCordenada = new Paint();
            paintCordenada.setColor(Color.RED);
            paintCordenada.setTextSize(40);
            paintCordenada.setAntiAlias(true);
            paintCordenada.setStyle(Paint.Style.STROKE);


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

            if(indiceR>-1) {
                for (int i = 0; i <= indiceR; i++) {
//                    paints.get(indice).setStrokeWidth(tamañoStrokeArray.get(indice));
                    canvas.drawCircle(xf.get(i), yf.get(i),radio.get(i),paints.get(i));
                }
            }
            if(indiceV>-1) {
                for (int i = 0; i <= indiceV; i++) {
//                    paints.get(indice).setStrokeWidth(tamañoStrokeArray.get(indice));
                    canvas.drawCircle(xfV.get(i), yfV.get(i),radioV.get(i),paintsV.get(i));
                }
            }

            float pje;
            if((indiceR+indiceV+2)==0)
                pje=0;
            else
                pje=(float)(indiceR+1)*100/(float)(indiceR+indiceV+2);

            rojo.setText("R: "+(indiceR+1));
            verde.setText("V: "+(indiceV+1));
            total.setText("T: "+(indiceR+indiceV+2));

            if(!Float.isInfinite(pje))
                porcentaje.setText("%: "+String.format(Locale.getDefault(),"%.1f",pje));

            /////////////////////////////////////////////////////////////////////////////////
        }
        public boolean onTouchEvent(MotionEvent e){
//            RecuentoKi67ManualActivity.this.mDetector.onTouchEvent(e);
            x=(int)e.getX();
            y=(int)e.getY();
            int sumaFlechas=0;

            if(e.getAction()==MotionEvent.ACTION_DOWN){

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

        getMenuInflater().inflate(R.menu.menu_ki67_manual,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.guardar2:
                guardar(null);
                break;
            case R.id.borrarTodo2:
                if(circuloVerde&&indiceV!=-1){
                    radioV.remove(indiceV);
                    paintsV.remove(indiceV);
                    xfV.remove(indiceV);
                    yfV.remove(indiceV);
                    indiceV--;
                }else if(circuloRojo&&indiceR!=-1){
                    radio.remove(indiceR);
                    paints.remove(indiceR);
                    xf.remove(indiceR);
                    yf.remove(indiceR);
                    indiceR--;
                }
                break;
        }
        vista.invalidate();
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

//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        vista.invalidate();
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
