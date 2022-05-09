package com.app.cacomplex.vaz;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CortarImagen extends AppCompatActivity {
    FrameLayout frameLayout;
    Imagen imagen;
    Bitmap imagenBitmap;
    BitmapFactory.Options bitmapImagenOpciones;
    float Rw,Rh;
    float escalaDensidad;
    private int ancho;
    private int alto;
    private float anchoCorte;
    private float altoCorte;
    private float recX;
    private float recY;
    private float recXMotion;
    private float recYMotion;
    private int altoInicial;
    private int altoFinal;
    private int anchoInicial;
    private int anchoFinal;
    private ProgressDialog pDialog;
    private int x;
    private int y;
    private int width;
    private int height;
    private ScaleGestureDetector mScaleDetector;
    private int mode;
    private float mScaleFactor=1.0f;
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    float saveScale = 1.f;
    private float origScale;
    float minScale = 1.f;
    float maxScale = 10.f;
    private int viewWidth;
    private int viewHeight;
    private float mContentViewX;
    private float mContentViewY;
    private float focusX,focusY;
    private float mPosX;
    private float mPosY;
    private float focusPrevioX=-1;
    private float focusPrevioY=-1;
    private float xFocusContent,yFocusContent;
    private float xTransMin,xTransMax,yTransMin,yTransMax;
    private boolean zoom=false;
    private boolean touchFueraVista=false;
    private boolean touchDentroVista=true;
    private String accion=null;
    private int indiceDoubleTap=0;
    private float mLastTouchX;
    private float mLastTouchY;
    private int w;
    private int h;
    private boolean primerDibujo=false;
    private String a="move";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cortar_imagen);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Recortar");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        frameLayout=(FrameLayout)findViewById(R.id.frame1);

        escalaDensidad = getResources().getDisplayMetrics().density;
        Log.i("densidad",String.valueOf(escalaDensidad));

        imagen=new Imagen(this);

        //Obtengo las dimensiones de la imagen original
        bitmapImagenOpciones= new BitmapFactory.Options();
        bitmapImagenOpciones.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(VistaPrevia.currentFileName,bitmapImagenOpciones);

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        frameLayout.addView(imagen);
    }

    class Imagen extends View{

        Drawable image;
        Drawable foreground;
        Paint paintRect;
        Paint paintStrokeRect;
        private float R1;
        private float R2;
        float x,y;

//        private int w;
//        private int h;
        private boolean fuera=false;
//        private boolean primerDibujo=false;

        @TargetApi(Build.VERSION_CODES.M)
        public Imagen(Context context) {
            super(context);
            if (VistaPrevia.currentFileName!=null) {
//                image = new BitmapDrawable(getResources(),VistaPrevia.currentFileName);
                  image = new BitmapDrawable(getResources(), VistaPrevia.imagen);
            }
            else
                image=getResources().getDrawable(R.drawable.ic_image_black_36dp);

            paintRect=new Paint();
            paintRect.setARGB(255,255,180,0);
            Log.i("densidadVista",String.valueOf(escalaDensidad));
            paintRect.setStrokeWidth(2.5f*escalaDensidad);
            paintRect.setStyle(Paint.Style.STROKE);


//            paintStrokeRect=new Paint();
//            paintStrokeRect.setColor(Color.YELLOW);
//            paintStrokeRect.setStrokeWidth(5);
//            paintStrokeRect.setStyle(Paint.Style.STROKE);

            //setScaleType(ScaleType.FIT_CENTER);
            setBackground(image);
            //setImageDrawable(image);
            //foreground=getResources().getDrawable(R.drawable.foreground);

            //setForeground(foreground);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);


            int wOriginal=bitmapImagenOpciones.outWidth;
            int hOriginal=bitmapImagenOpciones.outHeight;


            float imagenX=image.getIntrinsicWidth();
            float imagenY=image.getIntrinsicHeight();


//            Rw=(int)(wOriginal/imagenX);
//            Rh=(int)(hOriginal/imagenY);

            R1=imagenY/imagenX;

            w=canvas.getWidth();
            h=canvas.getHeight();

            R2=((float)h)/w;

            ancho = 0;
            alto = 0;
            altoInicial = 0;
            altoFinal = 0;
            anchoInicial = 0;
            anchoFinal = 0;

            if ((R1 > R2) && ((R1-R2)>0.02)) {
                ancho = (int) (h / R1);
                alto = (int) h;
                anchoInicial = (int) (w / 2 - ancho / 2);
                anchoFinal = (int) (w / 2 + ancho / 2);
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                    //image.setBounds(anchoInicial/2,0,ancho+anchoInicial/2,alto);
                    image.setBounds(0, 0, ancho, alto);
                } else {
                    image.setBounds(anchoInicial, 0, anchoFinal, alto);
                }
            }else if ((R1 < R2) && ((R2-R1)>0.05)) {
                ancho = (int) w;
                alto = (int) (R1 * w);
                altoInicial = (int) (h / 2 - alto / 2);
                altoFinal = (int) (h / 2 + alto / 2);
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                    //image.setBounds(0,altoInicial/2,ancho,alto+altoInicial/2);
                    image.setBounds(0, 0, ancho, alto);
                } else {
                    image.setBounds(0, altoInicial, ancho, altoFinal);
                }
            }else{
                ancho=w;
                alto=h;
            }

            Rw=((float)wOriginal)/ancho;
            Rh=((float)hOriginal)/alto;
            if(!primerDibujo) {
                recX = w / 2 - 100;
                recY = h / 2 - 100;
                recXMotion = recX + 200;
                recYMotion = recY + 200;
            }
            Log.i("a",a);
            if(a=="move") {
                canvas.drawRect(recX, recY, recXMotion, recYMotion, paintRect);
                primerDibujo=true;
            }
            anchoCorte=recXMotion-recX;
            altoCorte =recYMotion-recY;

            Log.i("Rectangulo","xi: " + String.valueOf(recX) + " yi: " + String.valueOf(recY) + " xmov: " + String.valueOf(recXMotion)
                    + " ymov: " +  String.valueOf(recYMotion));

            Log.i("Ancho y alto","ancho: " + String.valueOf(recXMotion-recX) + " alto: " + String.valueOf(recYMotion-recY));
            Log.i("Ancho y alto real","ancho: " + String.valueOf(ancho) + " alto: " + String.valueOf(alto));
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
           super.onTouchEvent(event);

            x=event.getX();
            y=event.getY();

            int accion= MotionEventCompat.getActionMasked(event);

            if(accion==MotionEvent.ACTION_DOWN){
                if(!zoom) {
                    recX = x;
                    recY = y;
                    //Verfica si el punto esta fuera de los limites de la fotografia.
                    if (y > h - (h - alto) / 2 || x > w - (w - ancho) / 2) {
                        fuera = true;
                    } else if (y < (h - alto) / 2 || x < (w - ancho) / 2) {
                        fuera = true;
                    } else
                        fuera = false;
                    Log.i("down", String.valueOf(fuera));
                    a = "down";
                }
            }
            if(accion==MotionEvent.ACTION_MOVE){
                //verifica si el punto en movimiento no esta fuera de los limites
                // y si esta delimitarlos.
                if(!zoom) {
                    if (y > h - (h - alto) / 2) {
                        recYMotion = h - (h - alto) / 2;
                        if (fuera)
                            a = "down";
                    } else if (x > w - (w - ancho) / 2) {
                        recXMotion = w - (w - ancho) / 2;
                        if (fuera)
                            a = "down";
                    } else if (y < (h - alto) / 2) {
                        recYMotion = (h - alto) / 2;
                        if (fuera)
                            a = "down";
                    } else if (x < (w - ancho) / 2) {
                        recXMotion = (w - ancho) / 2;
                        if (fuera)
                            a = "down";
                    } else if (y < h - (h - alto) / 2 && y > (h - alto) / 2 && x < w - (w - ancho) / 2 && x > (w - ancho) / 2) {
                        Log.i("move", String.valueOf(fuera) + "entro");
                        recXMotion = x;
                        recYMotion = y;
                        a = "move";
                        if (fuera)
                            a = "down";
                    }
                }
            }
            if(accion==MotionEvent.ACTION_UP){
                if(!zoom) {
                    if (a == "down")
                        a = "down";
                    else
                        a = "move";
                }
            }
            invalidate();
            return touchDentroVista;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_cortar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.cortar:
                if(zoom) {
                    zoom = false;
                    a="move";
                    touchDentroVista = true;
                    touchFueraVista = false;
                    primerDibujo=false;
                }else
                    cortar();
                imagen.invalidate();
                break;
            case R.id.zoom:
                zoom=true;
                touchDentroVista=false;
                touchFueraVista=true;
                break;
            case R.id.acercaDe:
                break;

        }


        return true;
    }

    private void cortar() {

        x=(int)((recX-anchoInicial)*Rw);
        y=(int)((recY-altoInicial)*Rh);

        width = (int)(anchoCorte*Rw);
        height= (int)(altoCorte*Rh);

        if(height < 0){
            y=y+height;
            height=-height;
        }
        if(width < 0){
            x=x+width;
            width=-width;
        }
        //final Bitmap ImagenSrc=BitmapFactory.decodeFile(VistaPrevia.currentFileName);

        pDialog = new ProgressDialog(CortarImagen.this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);

        if(width>0 && height>0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.show();
                        }
                    });
                    Log.i("Thread", String.valueOf(x) + " " + String.valueOf(y));
//                    imagenBitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(VistaPrevia.currentFileName), x, y, width, height);
                    imagenBitmap = Bitmap.createBitmap(VistaPrevia.imagen, x, y, width, height);
                    guardarEnCache();

                    pDialog.dismiss();
                    imagenBitmap.recycle();

                    Intent intentVistaPrevia=new Intent(CortarImagen.this,VistaPrevia.class);
                    startActivity(intentVistaPrevia);
                    finish();
                }
            }).start();
        }else{
            Toast.makeText(this, "Seleccione el area a recortar...", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarEnCache() {

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

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new_file);
            imagenBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//
//        if(imagenBitmap!=null)
//            imagenBitmap.recycle();
//
//        Intent intent=new Intent(this,VistaPrevia.class);
//        startActivity(intent);
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

            viewWidth  = imagen.getWidth();
            viewHeight = imagen.getHeight();
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
                    " " + String.valueOf(imagen.getPivotX()));

            //Zoom a vista
            imagen.setScaleX(mScaleFactor);
            imagen.setScaleY(mScaleFactor);

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
                imagen.setPivotX(focusPrevioX);
                imagen.setPivotY(focusPrevioY);
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
                    imagen.setTranslationX(mPosX);
                }
                if (-mPosX > xTransMax) {
                    mPosX = -xTransMax;
                    imagen.setTranslationX(mPosX);
                }
                if (mPosY > yTransMin) {
                    mPosY = yTransMin;
                    imagen.setTranslationY(mPosY);
                }
                if (-mPosY > yTransMax) {
                    mPosY = -yTransMax;
                    imagen.setTranslationY(mPosY);
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
            imagen.invalidate();

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
                    a="move";
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
                                    if (imagen.getScaleX() > 1 && imagen.getScaleY() > 1) {
                                        imagen.setScaleX(1);
                                        imagen.setScaleY(1);

                                        xTransMax=0;
                                        xTransMin=0;
                                        yTransMax=0;
                                        yTransMin=0;

                                        mScaleFactor=1;

                                        mPosX=0;
                                        mPosY=0;

                                        imagen.setTranslationX(mPosX);
                                        imagen.setTranslationY(mPosY);

                                    } else {

                                        mScaleFactor*=3;
                                        saveScale=mScaleFactor;
                                        imagen.setPivotX(x);
                                        imagen.setPivotY(y);
                                        imagen.setScaleX(mScaleFactor);
                                        imagen.setScaleY(mScaleFactor);

                                        viewWidth  = imagen.getWidth();
                                        viewHeight = imagen.getHeight();

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
                    a="move";
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
                                    imagen.setTranslationX(mPosX);
                                    focusX=mPosX;
                                }
                                if (mPosX < 0 && -mPosX <= xTransMax) {
                                    imagen.setTranslationX(mPosX);
                                    focusX=mPosX;
                                }
                                if (mPosY > 0 && mPosY <= yTransMin) {
                                    imagen.setTranslationY(mPosY);
                                    focusY=mPosY;
                                }
                                if (mPosY < 0 && -mPosY <= yTransMax) {
                                    imagen.setTranslationY(mPosY);
                                    focusY=mPosY;
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

                                imagen.invalidate();

                                mLastTouchX = x;
                                mLastTouchY = y;
                            }
                        } else {

                            final float x = event.getX();
                            final float y = event.getY();
                            imagen.invalidate();

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
                    a="move";
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

            imagen.invalidate();
        }

        imagen.invalidate();
        return touchFueraVista;
    }
}
