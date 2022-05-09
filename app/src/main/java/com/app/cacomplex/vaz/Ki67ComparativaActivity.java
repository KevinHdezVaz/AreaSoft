package com.app.cacomplex.vaz;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Ki67ComparativaActivity extends AppCompatActivity {

    private ImageView iv1,iv2;
    private LinearLayout lL1;
    //////////////////////////////////////////
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
    /////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ki67_comparativa);

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        getSupportActionBar().setTitle("Comparativa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv1=(ImageView)findViewById(R.id.iv1);
        iv2=(ImageView)findViewById(R.id.iv2);
        lL1=(LinearLayout)findViewById(R.id.lL1);

        iv1.setImageBitmap(VistaPrevia.imagen);


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


            viewWidth  = lL1.getWidth();
            viewHeight = lL1.getHeight();
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
                    " " + String.valueOf(lL1.getPivotX()));

            //Zoom a vista
            lL1.setScaleX(mScaleFactor);
            lL1.setScaleY(mScaleFactor);


            //Calculo de la distancia que debe trasladarse la lL1 cuando se realiza zoom.
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
                lL1.setPivotX(focusPrevioX);
                lL1.setPivotY(focusPrevioY);
                focusPrevioX += ((focusX - focusPrevioX)/mScaleFactor) * Math.abs(mScaleFactor-origScale)*0.5;
                focusPrevioY += ((focusY - focusPrevioY)/mScaleFactor) * Math.abs(mScaleFactor-origScale)*0.5;
            }
                /*
                if((xTransMax==0) && (yTransMax==0)){
                    vista.setTranslationX(0);
                    vista.setTranslationY(0);
                }
                */
            //Reacomoda la lL1 cuando se realiza el zoom out
            if((origScale-mScaleFactor)>0) {
                if (mPosX > xTransMin) {
                    mPosX = xTransMin;
                    lL1.setTranslationX(mPosX);
                }
                if (-mPosX > xTransMax) {
                    mPosX = -xTransMax;
                    lL1.setTranslationX(mPosX);
                }
                if (mPosY > yTransMin) {
                    mPosY = yTransMin;
                    lL1.setTranslationY(mPosY);
                }
                if (-mPosY > yTransMax) {
                    mPosY = -yTransMax;
                    lL1.setTranslationY(mPosY);
                }
            }

            Log.i("trans", "xTransMax: "+ String.valueOf(xTransMax)+ " xTransMin: " + String.valueOf(xTransMin)+
                    " yTransMax: "+ String.valueOf(yTransMax)+ " yTransMin: " + String.valueOf(yTransMin));
            Log.i("scale", "mPosX: " + String.valueOf(mPosX) + " mPosY: " + String.valueOf(mPosY));
            lL1.invalidate();

            return true;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

//        mDetector.onTouchEvent(event);
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
                                if (lL1.getScaleX() > 1 && lL1.getScaleY() > 1) {
                                    lL1.setScaleX(1);
                                    lL1.setScaleY(1);

                                    xTransMax=0;
                                    xTransMin=0;
                                    yTransMax=0;
                                    yTransMin=0;

                                    mScaleFactor=1;

                                    mPosX=0;
                                    mPosY=0;

                                    lL1.setTranslationX(mPosX);
                                    lL1.setTranslationY(mPosY);

                                } else {

                                    mScaleFactor*=3;
                                    saveScale=mScaleFactor;
                                    lL1.setPivotX(x);
                                    lL1.setPivotY(y);
                                    lL1.setScaleX(mScaleFactor);
                                    lL1.setScaleY(mScaleFactor);

                                    viewWidth  = lL1.getWidth();
                                    viewHeight = lL1.getHeight();

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
                                lL1.setTranslationX(mPosX);
//                                focusX=mPosX/mScaleFactor;
                            }
                            if (mPosX < 0 && -mPosX <= xTransMax) {
                                lL1.setTranslationX(mPosX);
//                                focusX=mPosX/mScaleFactor;
                            }
                            if (mPosY > 0 && mPosY <= yTransMin) {
                                lL1.setTranslationY(mPosY);
//                                focusY=mPosY/mScaleFactor;
                            }
                            if (mPosY < 0 && -mPosY <= yTransMax) {
                                lL1.setTranslationY(mPosY);
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

                            lL1.invalidate();

                            mLastTouchX = x;
                            mLastTouchY = y;
                        }
                    } else {

                        final float x = event.getX();
                        final float y = event.getY();
                        lL1.invalidate();

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
        lL1.invalidate();
        return true;
    }
}
