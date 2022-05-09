package com.app.cacomplex.vaz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivityVisualizarFoto extends AppCompatActivity {

    private View decorView;
    private GestureDetectorCompat mDetector;
    private boolean oculto;
    LinearLayout linearLayout1;
    /////////////////////////////////////////////////
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
    private Imagen imagen;
    /////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_visualizar_foto);
        getSupportActionBar().setTitle("Ver imagen");
        Drawable fondo = getResources().getDrawable(R.drawable.fondo);
        getSupportActionBar().setBackgroundDrawable(fondo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout1=(LinearLayout)findViewById(R.id.linearLayout1);

        decorView = getWindow().getDecorView();
        //Hace que desaparezca el Status Bar, el navigation bar y action bar
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        oculto=true;

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        mDetector = new GestureDetectorCompat(this, new GestureDetector.OnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
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
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                    oculto=true;
                }else {
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
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

                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                            getSupportActionBar().hide();
                        }
                    }
                });
        imagen=new Imagen(this);
        linearLayout1.addView(imagen);



    }

    public class Imagen extends View{
        Drawable image;
        private float R1;
        private float R2;
        private int ancho;
        private int alto;
        private int altoInicial;
        private int altoFinal;
        private int anchoInicial;
        private int anchoFinal;


        public Imagen(Context context) {
            super(context);
//            setScaleType(ScaleType.CENTER_INSIDE);
            if (VistaPrevia.currentFileName!=null) {
                image= new BitmapDrawable(getResources(), VistaPrevia.imagen);
                setBackground(image);
            }
            else {
              //  image = getResources().getDrawable(R.drawable.ic_image_black_36dp);


                //setBackground(image);
            }


        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            
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
                {
                    //image.setBounds(0,altoInicial/2,ancho,alto+altoInicial/2);
                    image.setBounds(0,0,ancho,alto);
                }else {
                    image.setBounds(0, altoInicial, ancho, altoFinal);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            mDetector.onTouchEvent(event);
            return false;
        }
    }
    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {

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


            viewWidth  = imagen.getWidth();
            viewHeight = imagen.getHeight();
            mContentViewX = viewWidth*mScaleFactor;
            mContentViewY = viewHeight*mScaleFactor;

            focusX=detector.getFocusX();
            focusY=detector.getFocusY();
            focusX+=-mPosX/mScaleFactor;
            focusY+=-mPosY/mScaleFactor;

            //Inicializa el focusPrevio
            if(mContentViewX==viewWidth && mContentViewY == viewHeight){
                focusPrevioX = focusX;
                focusPrevioY = focusY;
            }

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

            Log.i("trans", "xTransMax: "+ String.valueOf(xTransMax)+ " xTransMin: " + String.valueOf(xTransMin)+
                    " yTransMax: "+ String.valueOf(yTransMax)+ " yTransMin: " + String.valueOf(yTransMin));
            Log.i("scale", "mPosX: " + String.valueOf(mPosX) + " mPosY: " + String.valueOf(mPosY));
            imagen.invalidate();

            return true;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        mDetector.onTouchEvent(event);
        // Let the ScaleGestureDetector inspect all events.
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(event);
        final int action = MotionEventCompat.getActionMasked(event);

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
//                                focusX=mPosX/mScaleFactor;
                            }
                            if (mPosX < 0 && -mPosX <= xTransMax) {
                                imagen.setTranslationX(mPosX);
//                                focusX=mPosX/mScaleFactor;
                            }
                            if (mPosY > 0 && mPosY <= yTransMin) {
                                imagen.setTranslationY(mPosY);
//                                focusY=mPosY/mScaleFactor;
                            }
                            if (mPosY < 0 && -mPosY <= yTransMax) {
                                imagen.setTranslationY(mPosY);
//                                focusY=mPosY/mScaleFactor;
                            }

                            if (mPosX > xTransMin)
                                mPosX = xTransMin;
                            if (-mPosX > xTransMax)
                                mPosX = -xTransMax;
                            if (mPosY > yTransMin)
                                mPosY = yTransMin;
                            if (-mPosY > yTransMax)
                                mPosY = -yTransMax;

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
        imagen.invalidate();
        return true;
    }

}
