package com.example.windows10.leerimagen;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class RecuentoKi67Activity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 100;
    private static final int OBTENER_COLOR = 102;

    FrameLayout frameLayout;
    ImageView iv1;

    int ancho=0;
    int alto=0;
    int altoInicial=0;
    int altoFinal=0;
    int anchoInicial=0;
    int anchoFinal=0;
    float R1,R2;
    static boolean procesado=false;

    Mat imagenMatriz=new Mat();
    Mat imagenProcesada;
    Bitmap imagenBitmap;

    String porcentajes;

    public static ArrayList<Float> listaPorcentaje=new ArrayList<Float>();


    public static final String TAG="MainActivityPrincipal";

    static{
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"Cargo exitosamente :)");
        }else
            Log.d(TAG,"No pudo cargar el paquete openCV :(");
    }

    private Vector<Mat> canales;
    private List<Mat> clusters;
    private int indice1,indice2,indice3,indice4;
    private Mat celNMres;
    private Mat celM;
    private double porcentajeCM;
    private LinearLayout L1;
    private TextView tv1,tvPrincipal1,tvPrincipal2,tv2;
    private Uri uriImagenOriginal;
    private Bitmap clase1,clase2,clase3,clase4;
    private Mat clase1Mat,clase2Mat;
    private Mat matZeros;
    private static Bitmap clase1P,clase2P;
    private double maxVal;
    private int contador1,contador2;
    private Mat mask;
    private Mat fillclase1,fillclase2;
    private double[] stdCM,areaMeanCM,stdCNM,areaMeanCNM;
    private double[] sumaAreaCNM,sumaAreaCM;
    private double areaMaximaCNM,areaMaximaCM;
    public static int pRojoCM,pVerdeCM,pAzulCM,pRojoCNM,pVerdeCNM,pAzulCNM,pRojoEE,pVerdeEE,pAzulEE;
    public static boolean valores;
    private int rMin1,rMax1, gMin1,gMax1, bMin1,bMax1,rMin2,rMax2, gMin2,gMax2, bMin2,bMax2,rMin3,rMax3, gMin3,gMax3, bMin3,bMax3;
    private int rPromedio1,gPromedio1,bPromedio1,rPromedio2,gPromedio2,bPromedio2,rPromedio3,gPromedio3,bPromedio3;
    private Mat clasesJuntas;
    public static Bitmap imBitmapJuntas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuento_ki67);
        getSupportActionBar().setTitle("Ki 67");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uriImagenOriginal= VistaPrevia.selectedImage;

        iv1=(ImageView) findViewById(R.id.iv1);
        L1=(LinearLayout) findViewById(R.id.L1);
        tv1=(TextView) findViewById(R.id.tv1);
        tv2=(TextView) findViewById(R.id.tv2);
        tvPrincipal1=(TextView) findViewById(R.id.tvPrincipal1);
        tvPrincipal2=(TextView) findViewById(R.id.tvPrincipal2);

        rMin1 = 141;
        rMax1 = 183;
        gMin1 = 96;
        gMax1 = 147;
        bMin1 = 76;
        bMax1 = 119;

        rMin2 = 153;
        rMax2 = 179;
        gMin2 = 132;
        gMax2 = 161;
        bMin2 = 135;
        bMax2 = 162;

        rMin3=186;
        rMax3=210;
        gMin3=171;
        gMax3=197;
        bMin3=150;
        bMax3=176;


        rPromedio1=(rMin1+rMax1)/2;
        gPromedio1=(gMin1+gMax1)/2;
        bPromedio1=(bMin1+bMax1)/2;

        rPromedio2=(rMin2+rMax2)/2;
        gPromedio2=(gMin2+gMax2)/2;
        bPromedio2=(bMin2+bMax2)/2;

        rPromedio3=(rMin3+rMax3)/2;
        gPromedio3=(gMin3+gMax3)/2;
        bPromedio3=(bMin3+bMax3)/2;

        if(VistaPrevia.imagen!=null) {
            imagenBitmap = VistaPrevia.imagen.copy(Bitmap.Config.RGB_565, true);
            dimensionarBitmap(uriImagenOriginal);
        }

        if(valores)
            tv2.setText("Colores obtenidos");
        else {
            tv2.setText("Colores predeterminados");

            pRojoCM = rPromedio1;
            pVerdeCM = gPromedio1;
            pAzulCM = bPromedio1;

            pRojoCNM = rPromedio2;
            pVerdeCNM = gPromedio2;
            pAzulCNM = bPromedio2;

            pRojoEE = rPromedio3;
            pVerdeEE = gPromedio3;
            pAzulEE = bPromedio3;
        }


        tv1.setText("");

        for(int i=0;i<listaPorcentaje.size();i++)
            tv1.append("Campo "+(i+1)+": " + String.format("%.1f",listaPorcentaje.get(i))+"%\n");

    }

    private void dimensionarBitmap(Uri selectedImage) {

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        try {
            BitmapFactory.decodeStream(this.getContentResolver().openInputStream(selectedImage),null,options);

            int with = options.outWidth;
            int height= options.outHeight;


            options.inSampleSize=(int)Math.max(Math.ceil(options.outWidth/500),Math.ceil(options.outHeight/500));
            options.inJustDecodeBounds=false;

            imagenBitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(selectedImage),null,options);

            iv1.setImageBitmap(imagenBitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void calcularPorcentaje(){

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);



        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.show();
                    }
                });

                clase1=Bitmap.createBitmap(imagenBitmap.getWidth(),imagenBitmap.getHeight(), Bitmap.Config.RGB_565);
                clase2=clase1.copy(Bitmap.Config.RGB_565,true);
                clase3=clase1.copy(Bitmap.Config.RGB_565,true);
                clase4=clase1.copy(Bitmap.Config.RGB_565,true);
                clase1P=clase1.copy(Bitmap.Config.RGB_565,true);
                clase2P=clase1.copy(Bitmap.Config.RGB_565,true);
                imBitmapJuntas=clase1.copy(Bitmap.Config.RGB_565,true);

                clasesJuntas=new Mat();
                clase1Mat=new Mat();
                clase2Mat=new Mat();


                int rMin4=199;
                int rMax4=218;
                int gMin4=189;
                int gMax4=208;
                int bMin4=157;
                int bMax4=180;

                int rPromedio4=(rMin4+rMax4)/2;
                int gPromedio4=(gMin4+gMax4)/2;
                int bPromedio4=(bMin4+bMax4)/2;

                int r,g,b;

                double dist1=0;
                double dist2=0;
                double dist3=0;
                double dist4=0;

                for(int fila=0;fila<imagenBitmap.getHeight();fila++)
                    for(int col=0;col<imagenBitmap.getWidth();col++){
                        r= Color.red(imagenBitmap.getPixel(col,fila));
                        g= Color.green(imagenBitmap.getPixel(col,fila));
                        b= Color.blue(imagenBitmap.getPixel(col,fila));

                        dist1=Math.sqrt(Math.pow(r-pRojoCM,2)+
                                Math.pow(g-pVerdeCM,2)+
                                Math.pow(b-pAzulCM,2));
                        dist2=Math.sqrt(Math.pow(r-pRojoCNM,2)+
                                Math.pow(g-pVerdeCNM,2)+
                                Math.pow(b-pAzulCNM,2));
                        dist3=Math.sqrt(Math.pow(r-pRojoEE,2)+
                                Math.pow(g-pVerdeEE,2)+
                                Math.pow(b-pAzulEE,2));
                        dist4=Math.sqrt(Math.pow(r-rPromedio4,2)+
                                Math.pow(g-gPromedio4,2)+
                                Math.pow(b-bPromedio4,2));


//                        if(r > rMin1 && r < rMax1 && g > gMin1 && g < gMax1 && b > bMin1 && b < bMax1){
//                            clase1.setPixel(col,fila,Color.WHITE);
//                        }else if(r > rMin2 && r < rMax2 && g > gMin2 && g < gMax2 && b > bMin2 && b < bMax2){
//                            clase2.setPixel(col, fila, Color.WHITE);
//                        }
                        if((dist1 < dist2) && (dist1 < dist3) && (dist1 < dist4)){
                            clase1.setPixel(col, fila, Color.WHITE);
                        }else if((dist2 < dist1) && (dist2 < dist3) && (dist2 < dist4)){
                            clase2.setPixel(col, fila, Color.WHITE);
                        }else if((dist3 < dist1) && (dist3 < dist2) && (dist3 < dist4)){
                            clase3.setPixel(col, fila, Color.WHITE);
                        }else if((dist4 < dist1) && (dist4 < dist2) && (dist4 < dist3)){
                            clase4.setPixel(col, fila, Color.WHITE);
                        }

                    }

                procesado=true;
                invalidateOptionsMenu();

                procesamientoCelulasM();
                procesamientoCelulasNM();

                Utils.matToBitmap(clase1Mat,clase1P);
                Utils.matToBitmap(celNMres,clase2P);

                Mat maskClase1=new Mat();
                maskClase1=clase1Mat.clone();

                Mat maskCelNMres=new Mat();
                maskCelNMres=celNMres.clone();

                Imgproc.cvtColor(clase1Mat,clase1Mat,Imgproc.COLOR_GRAY2RGB);
                Imgproc.cvtColor(celNMres,celNMres,Imgproc.COLOR_GRAY2RGB);

                clase1Mat.setTo(new Scalar(160,80,20),maskClase1);
                celNMres.setTo(new Scalar(50,170,200),maskCelNMres);

                Core.add(clase1Mat,celNMres,clasesJuntas);
                Utils.matToBitmap(clasesJuntas,imBitmapJuntas);

                float porcentajeSuperficieCM=((float)((sumaAreaCM[0]*100)/(sumaAreaCNM[0]+sumaAreaCM[0])));

//                listaPorcentaje.add((float)porcentajeCM);
                listaPorcentaje.add(porcentajeSuperficieCM);

                pDialog.dismiss();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv1.setText("");

                        for(int i=0;i<listaPorcentaje.size();i++)
                            tv1.append("Campo "+(i+1)+": "+String.format("%.1f",listaPorcentaje.get(i))+"%\n");
                    }
                });

            }
        }).start();



    }

    private void calcularPorcentajeautomaticoDist(){

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);



        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.show();
                    }
                });

                clase1=Bitmap.createBitmap(imagenBitmap.getWidth(),imagenBitmap.getHeight(), Bitmap.Config.RGB_565);
                clase2=clase1.copy(Bitmap.Config.RGB_565,true);
                clase3=clase1.copy(Bitmap.Config.RGB_565,true);
                clase4=clase1.copy(Bitmap.Config.RGB_565,true);
                clase1P=clase1.copy(Bitmap.Config.RGB_565,true);
                clase2P=clase1.copy(Bitmap.Config.RGB_565,true);
                clase1Mat=new Mat();
                clase2Mat=new Mat();

                int rPromedio1=161;
                int gPromedio1=118;
                int bPromedio1=108;

                int rPromedio2=173;
                int gPromedio2=152;
                int bPromedio2=159;

                int rPromedio3=200;
                int gPromedio3=181;
                int bPromedio3=162;

                int rPromedio4=208;
                int gPromedio4=199;
                int bPromedio4=172;

                double dist1=0;
                double dist2=0;
                double dist3=0;
                double dist4=0;


                for(int fila=0;fila<imagenBitmap.getHeight();fila++)
                    for(int col=0;col<imagenBitmap.getWidth();col++){

                        dist1=Math.sqrt(Math.pow(Color.red(imagenBitmap.getPixel(col,fila))-rPromedio1,2)+
                                Math.pow(Color.green(imagenBitmap.getPixel(col,fila))-gPromedio1,2)+
                                Math.pow(Color.blue(imagenBitmap.getPixel(col,fila))-bPromedio1,2));

                        dist2=Math.sqrt(Math.pow(Color.red(imagenBitmap.getPixel(col,fila))-rPromedio2,2)+
                                Math.pow(Color.green(imagenBitmap.getPixel(col,fila))-gPromedio2,2)+
                                Math.pow(Color.blue(imagenBitmap.getPixel(col,fila))-bPromedio2,2));

                        dist3=Math.sqrt(Math.pow(Color.red(imagenBitmap.getPixel(col,fila))-rPromedio3,2)+
                                Math.pow(Color.green(imagenBitmap.getPixel(col,fila))-gPromedio3,2)+
                                Math.pow(Color.blue(imagenBitmap.getPixel(col,fila))-bPromedio3,2));

                        dist4=Math.sqrt(Math.pow(Color.red(imagenBitmap.getPixel(col,fila))-rPromedio4,2)+
                                Math.pow(Color.green(imagenBitmap.getPixel(col,fila))-gPromedio4,2)+
                                Math.pow(Color.blue(imagenBitmap.getPixel(col,fila))-bPromedio4,2));

                        if((dist1 < dist2) && (dist1 < dist3) && (dist1 < dist4)){
                            clase1.setPixel(col,fila,Color.WHITE);
                        }else if((dist2 < dist1) && (dist2 < dist3)&& (dist2 < dist4)) {
                            clase2.setPixel(col, fila, Color.WHITE);
                        }else if((dist3 < dist1) && (dist3 < dist2) && (dist3 < dist4))
                            clase3.setPixel(col,fila,Color.WHITE);
                        else if((dist4 < dist1) && (dist4 < dist2) && (dist4 < dist3))
                            clase4.setPixel(col,fila,Color.WHITE);

                    }

                procesado=true;
                invalidateOptionsMenu();

                procesamientoCelulasM();
                procesamientoCelulasNM();

                Utils.matToBitmap(clase1Mat,clase1P);
                Utils.matToBitmap(celNMres,clase2P);

                contador1=0;
                contador2=0;
                int pixel=0;
                int cuenta1=0;
                int cuenta0=9;
                int[] vector9=new int[9];

                for(int j=0;j<clase1P.getHeight();j++)
                    for(int i=0;i<clase1P.getWidth();i++){

                        if(Color.green(clase1P.getPixel(i,j))>250){
                            contador1++;
                        }
                        if(Color.green(clase2P.getPixel(i,j))>250){
                            contador2++;
                        }
                    }

                float porcentajeSuperficieCM=(contador1*100)/(contador1+contador2);




                listaPorcentaje.add((float)porcentajeCM);
                listaPorcentaje.add(porcentajeSuperficieCM);

                pDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv1.setText("");

                        for(int i=0;i<listaPorcentaje.size();i++)
                            tv1.append("Campo "+(i+1)+": "+String.format("%.1f",listaPorcentaje.get(i))+"%\n");
                    }
                });

            }
        }).start();



    }

    private void procesamientoCelulasNM() {

        Mat cantCNM=new Mat();
        Mat centroideCNM=new Mat();
        Mat staticsCNM=new Mat();
        centroideCNM.convertTo(centroideCNM,CvType.CV_64F);
        cantCNM.convertTo(cantCNM,CvType.CV_32S);
        MatOfDouble matOfDouble=new MatOfDouble();
        MatOfDouble matOfDoubleStd=new MatOfDouble();
        Mat areas=new Mat();

        celNMres=new Mat();
        Utils.bitmapToMat(clase2,clase2Mat);
        Imgproc.cvtColor(clase2Mat,clase2Mat,Imgproc.COLOR_BGR2GRAY);

        Mat celMdilatada=new Mat();
        celMdilatada=clase1Mat.clone();

        Imgproc.morphologyEx(celMdilatada, celMdilatada,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));

        Imgproc.dilate(celMdilatada, celMdilatada, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
        Imgproc.dilate(celMdilatada, celMdilatada, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));


        Core.subtract(clase2Mat,celMdilatada,celNMres);

        Mat celNMresO=new Mat();

        celNMresO=celNMres.clone();

//        Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));

//Proceso de llenado de huecos
        int col=0;
        for(int i=0;i<celNMres.cols();i++) {
            if (celNMres.get(celNMres.rows()/2, i)[0] == 0) {
                col=i;
                break;
            }
        }

        celNMres.convertTo(celNMres,CvType.CV_8U);
        mask=Mat.zeros(celNMres.rows()+2,celNMres.cols()+2,CvType.CV_8U);

        Mat pruebaFill=new Mat();
        pruebaFill.convertTo(pruebaFill,CvType.CV_32S);

        Imgproc.connectedComponentsWithStats(celNMres,pruebaFill,staticsCNM,centroideCNM);
        areas=staticsCNM.submat(0,staticsCNM.rows(),4,5);

        int[] areasPixels=new int[(int)areas.total()*areas.channels()];
        areas.get(0,0,areasPixels);

        int areaBG=areasPixels[0];

        double maximoFill;
        int i=0;
        int columna=0;
        boolean fillA,fillB;

        do {
                int areaMask;
                Imgproc.floodFill(celNMres, mask, new Point(col, celNMres.rows() / 2), new Scalar(255));

                Imgproc.connectedComponentsWithStats(mask,pruebaFill,staticsCNM,centroideCNM);
                areas=staticsCNM.submat(0,staticsCNM.rows(),4,5);

                areasPixels=new int[(int)areas.total()*areas.channels()];
                areas.get(0,0,areasPixels);

                areaMask=areasPixels[1];

            if(Math.abs(areaMask-areaBG)/areaBG >= 0.2){

                col+=celNMres.cols()/10;
                celNMres=celNMresO.clone();
//                Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));

                for (columna = col; columna < celNMres.cols();columna++) {
                    if (celNMres.get(celNMres.rows() / 2, columna)[0] == 0) {
                        col = columna;
                        break;
                    }
                }
            }else {
                Core.bitwise_not(celNMres,celNMres);

                Core.add(celNMres, celNMresO, celNMres);

                Imgproc.connectedComponentsWithStats(celNMres, pruebaFill, staticsCNM, centroideCNM);
                areas = staticsCNM.submat(0, staticsCNM.rows(), 4, 5);

                areasPixels = new int[(int) areas.total() * areas.channels()];
                areas.get(0, 0, areasPixels);

                sumaAreaCNM = Core.sumElems(areas).val;

                areas = staticsCNM.submat(1, staticsCNM.rows(), 4, 5);
                areas.get(0, 0, areasPixels);

                Core.MinMaxLocResult minMaxLocAreas = Core.minMaxLoc(areas);
                areaMaximaCNM = minMaxLocAreas.maxVal;

                if ((sumaAreaCNM[0] - areaMaximaCNM) / sumaAreaCNM[0] >= 0.2) {
                    Imgproc.morphologyEx(celNMres, celNMres, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
                    Imgproc.morphologyEx(celNMres, celNMres, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
                } else {
                    celNMres = celNMresO.clone();
                    Imgproc.morphologyEx(celNMres, celNMres, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
                    Imgproc.morphologyEx(celNMres, celNMres, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
                }
                break;
            }

            i++;
        }while(i<10);

//        Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(-1,-1),2);
////
//        Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
//        Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));


//        Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
//        Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));

        Scalar areaScalar;
        Point locAreaM=new Point();

        Imgproc.connectedComponentsWithStats(celNMres,cantCNM,staticsCNM,centroideCNM);
        areas=staticsCNM.submat(1,staticsCNM.rows(),4,5);

        areasPixels=new int[(int)areas.total()*areas.channels()];
        areas.get(0,0,areasPixels);

        sumaAreaCNM=Core.sumElems(areas).val;

        Core.MinMaxLocResult minMaxLocAreas=Core.minMaxLoc(areas);
        areaMaximaCNM=minMaxLocAreas.maxVal;
        locAreaM=minMaxLocAreas.maxLoc;

        Core.meanStdDev(areas,matOfDouble,matOfDoubleStd);
        stdCNM=new double[(int)matOfDoubleStd.total()*matOfDoubleStd.channels()];
        areaMeanCNM=new double[(int)matOfDoubleStd.total()*matOfDoubleStd.channels()];
        matOfDoubleStd.get(0,0,stdCNM);
        matOfDouble.get(0,0,areaMeanCNM);

        double difAreaM=Math.abs(areaMaximaCNM-areaMeanCNM[0])/stdCNM[0];

        while(difAreaM > 7){
            areaMaximaCNM=areaMaximaCNM/2;
            areas.put((int)locAreaM.y,(int)locAreaM.x,areaMaximaCNM);

//            areas.get(0,0,areasPixels);

//            sumaAreaCNM=Core.sumElems(areas).val;
//
            minMaxLocAreas=Core.minMaxLoc(areas);
            areaMaximaCNM=minMaxLocAreas.maxVal;
            locAreaM=minMaxLocAreas.maxLoc;
//
//            Core.meanStdDev(areas,matOfDouble,matOfDoubleStd);
//            stdCNM=new double[(int)matOfDoubleStd.total()*matOfDoubleStd.channels()];
//            areaMeanCNM=new double[(int)matOfDoubleStd.total()*matOfDoubleStd.channels()];
//            matOfDoubleStd.get(0,0,stdCNM);
//            matOfDouble.get(0,0,areaMeanCNM);
            difAreaM=(areaMaximaCNM-areaMeanCNM[0])/stdCNM[0];
        }

        sumaAreaCNM=Core.sumElems(areas).val;

        //Se aplica mas operaciones morfologicas para el conteo
//
//        Imgproc.morphologyEx(celNMres, celNMres, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
//        Imgproc.morphologyEx(celNMres, celNMres, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));

        Imgproc.connectedComponentsWithStats(celNMres,cantCNM,staticsCNM,centroideCNM);
        Core.MinMaxLocResult minMaxLocResult2=Core.minMaxLoc(cantCNM);

        double maxVal2=minMaxLocResult2.maxVal;
        double cantT=maxVal+maxVal2;
        porcentajeCM=maxVal*100/cantT;
        double porcentajeCNM=maxVal2*100/cantT;
    }

    private void procesamientoCelulasM() {

        Mat cantCM=new Mat();
        cantCM.convertTo(cantCM,CvType.CV_32S);
        Mat centroideCM=new Mat();
        Mat staticsCM=new Mat();
        centroideCM.convertTo(centroideCM,CvType.CV_64F);

        Mat areas=new Mat();
        MatOfDouble matOfDouble=new MatOfDouble();
        MatOfDouble matOfDoubleStd=new MatOfDouble();

        Utils.bitmapToMat(clase1,clase1Mat);
        Imgproc.cvtColor(clase1Mat,clase1Mat,Imgproc.COLOR_BGR2GRAY);

        Mat clase1Mat0=new Mat();
        clase1Mat0=clase1Mat.clone();

        Imgproc.morphologyEx(clase1Mat,clase1Mat,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));

        int col=0;
        for(int i=0;i<clase1Mat.cols();i++) {
            if (clase1Mat.get(clase1Mat.rows()/2, i)[0] == 0) {
                col=i;
                break;
            }
        }

        clase1Mat.convertTo(clase1Mat,CvType.CV_8U);
        mask=Mat.zeros(clase1Mat.rows()+2,clase1Mat.cols()+2,CvType.CV_8U);

        Mat pruebaFill=new Mat();
        pruebaFill.convertTo(pruebaFill,CvType.CV_32S);

        Imgproc.connectedComponentsWithStats(clase1Mat,pruebaFill,staticsCM,centroideCM);
        areas=staticsCM.submat(0,staticsCM.rows(),4,5);

        int[] areasPixels=new int[(int)areas.total()*areas.channels()];
        areas.get(0,0,areasPixels);

        int areaBG=areasPixels[0];


        double maximoFill;
        int i=0;
        int columna=0;
        boolean fillA,fillB;

        do {

            int areaMask;
            Imgproc.floodFill(clase1Mat, mask, new Point(col, clase1Mat.rows() / 2), new Scalar(255));


            Imgproc.connectedComponentsWithStats(mask,pruebaFill,staticsCM,centroideCM);
            areas=staticsCM.submat(0,staticsCM.rows(),4,5);

            areasPixels=new int[(int)areas.total()*areas.channels()];
            areas.get(0,0,areasPixels);

            areaMask=areasPixels[1];
            if(Math.abs(areaMask-areaBG)/areaBG >= 0.2){
                col+=clase1Mat.cols()/10;
                clase1Mat=clase1Mat0.clone();
                Imgproc.morphologyEx(clase1Mat,clase1Mat,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));

                for (columna = col; columna < clase1Mat.cols(); columna++) {
                    if (clase1Mat.get(clase1Mat.rows() / 2, columna)[0] == 0) {
                        col = columna;
                        break;
                    }
                }
            }else {
                Core.bitwise_not(clase1Mat,clase1Mat);

                Core.add(clase1Mat,clase1Mat0, clase1Mat);

                Imgproc.connectedComponentsWithStats(clase1Mat, pruebaFill, staticsCM, centroideCM);
                areas = staticsCM.submat(0, staticsCM.rows(), 4, 5);

                areasPixels = new int[(int) areas.total() * areas.channels()];
                areas.get(0, 0, areasPixels);

                sumaAreaCM = Core.sumElems(areas).val;

                areas = staticsCM.submat(1, staticsCM.rows(), 4, 5);
                areas.get(0, 0, areasPixels);

                Core.MinMaxLocResult minMaxLocAreas = Core.minMaxLoc(areas);
                areaMaximaCM = minMaxLocAreas.maxVal;

                if ((sumaAreaCM[0] - areaMaximaCM) / sumaAreaCM[0] >= 0.2) {
                    Imgproc.morphologyEx(clase1Mat, clase1Mat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
                    Imgproc.morphologyEx(clase1Mat, clase1Mat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
                } else {
                    clase1Mat = clase1Mat0.clone();
                    Imgproc.morphologyEx(clase1Mat, clase1Mat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
                    Imgproc.morphologyEx(clase1Mat,clase1Mat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
                }
                break;
            }

            i++;
        }while(i<10);

        Scalar areaScalar;
        Point locAreaM=new Point();

        Imgproc.connectedComponentsWithStats(clase1Mat,cantCM,staticsCM,centroideCM);
        areas=staticsCM.submat(1,staticsCM.rows(),4,5);

        areasPixels=new int[(int)areas.total()*areas.channels()];
        areas.get(0,0,areasPixels);

        sumaAreaCM=Core.sumElems(areas).val;

        Core.MinMaxLocResult minMaxLocAreas=Core.minMaxLoc(areas);
        areaMaximaCM=minMaxLocAreas.maxVal;

        Core.meanStdDev(areas,matOfDouble,matOfDoubleStd);
        stdCM=new double[(int)matOfDoubleStd.total()*matOfDoubleStd.channels()];
        areaMeanCM=new double[(int)matOfDoubleStd.total()*matOfDoubleStd.channels()];
        matOfDoubleStd.get(0,0,stdCM);
        matOfDouble.get(0,0,areaMeanCM);

        double difAreaM=(areaMaximaCM-areaMeanCM[0])/stdCM[0];

        while(difAreaM > 7){
            areaMaximaCM=areaMaximaCM/2;
            areas.put((int)locAreaM.y,(int)locAreaM.x,areaMaximaCM);
//            areas.get(0,0,areasPixels);
//
//            sumaAreaCM=Core.sumElems(areas).val;
//
            minMaxLocAreas=Core.minMaxLoc(areas);
            areaMaximaCM=minMaxLocAreas.maxVal;
            locAreaM=minMaxLocAreas.maxLoc;
//
//            Core.meanStdDev(areas,matOfDouble,matOfDoubleStd);
//            stdCM=new double[(int)matOfDoubleStd.total()*matOfDoubleStd.channels()];
//            areaMeanCM=new double[(int)matOfDoubleStd.total()*matOfDoubleStd.channels()];
//            matOfDoubleStd.get(0,0,stdCM);
//            matOfDouble.get(0,0,areaMeanCM);
            difAreaM=(areaMaximaCM-areaMeanCM[0])/stdCM[0];
        }

        sumaAreaCM=Core.sumElems(areas).val;
        sumaAreaCM[0]=sumaAreaCM[0]-sumaAreaCM[0]*0.085;

//        int iMedian,median;
//        int iQ1,Q1,iQ3,Q3,RIQ,lSup,lInf;
//
//        Mat areasOrdenadas=ordenarVector(areas);
//        int[] areasPOrdenadas=new int[(int)areasOrdenadas.total()*areasOrdenadas.channels()];
//        areasOrdenadas.get(0,0,areasPOrdenadas);
//
//        //calculo de la mediana............
//        iMedian=areasPOrdenadas.length%2;
//
//        if(iMedian==0){
//            iMedian=(areasPOrdenadas.length/2+(areasPOrdenadas.length/2-1))/2;
//            median=areasPOrdenadas[iMedian];
//        }else {
//            iMedian = areasPOrdenadas.length / 2;
//            median = areasPOrdenadas[iMedian];
//        }
//        //Calculo del Q1 y Q3
//        iQ1=(areasPOrdenadas.length+1)/4;
//        Q1=areasPOrdenadas[iQ1];
//
//        iQ3=3*iQ1;
//        Q3=areasPOrdenadas[iQ3];
//        //Calculo RIQ y valores extremos
//        RIQ=Q3-Q1;
//        lSup=Q3+3*RIQ;
//        lInf=Q1-3*RIQ;
//        //Calculo de la media intercuartil
//        int promedioIQ=0;
//        int contador=0;
//        for(int j=iQ1;j<=iQ3;j++){
//            promedioIQ+=areasPOrdenadas[j];
//            contador++;
//        }
//        promedioIQ=promedioIQ/contador;

        //Se agregan mas operaciones morfologicas para el conteo
//        Imgproc.morphologyEx(clase1Mat, clase1Mat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));
//        Imgproc.morphologyEx(clase1Mat, clase1Mat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(5, 5)));

        Imgproc.connectedComponentsWithStats(clase1Mat,cantCM,staticsCM,centroideCM);

        Core.MinMaxLocResult minMaxLocResult=Core.minMaxLoc(cantCM);
        maxVal=minMaxLocResult.maxVal;

    }

    private Mat ordenarVector(Mat areasPixels) {

        Core.sort(areasPixels,areasPixels,Core.SORT_EVERY_COLUMN+Core.SORT_ASCENDING);

        return areasPixels;
    }

    private void calcularPorcentajeAutomatico() {
        final Mat labels=new Mat();
        final Mat centroide=new Mat();
        Mat centroideGris=new Mat();

        Utils.bitmapToMat(imagenBitmap, imagenMatriz);
        imagenProcesada = imagenMatriz.clone();

        imagenProcesada.convertTo(imagenProcesada,CvType.CV_8UC3);
        imagenProcesada=imagenProcesada.reshape(1,imagenProcesada.rows()*imagenProcesada.cols());
        imagenProcesada.convertTo(imagenProcesada,CvType.CV_32F);

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.show();
                    }
                });

                TermCriteria criteria=new TermCriteria(TermCriteria.EPS|TermCriteria.MAX_ITER,20,0.5);

                Core.kmeans(imagenProcesada,3,labels,criteria,10,Core.KMEANS_PP_CENTERS,centroide);

                /////////////////////////////////////////////obtengo los valores minimos y sus indices//////////
                int indiceMax=0;
                int indiceMin=0;
                double valorMin;
                double[] intensidad=new double[centroide.rows()];

                double bl = centroide.get(0, 0)[0];
                double gr = centroide.get(0, 1)[0];
                double re = centroide.get(0, 2)[0];

                intensidad[0] = (bl + gr + re) / 3;
                valorMin=intensidad[0];

                for(int i=1; i<centroide.rows();i++) {
                    bl = centroide.get(i, 0)[0];
                    gr = centroide.get(i, 1)[0];
                    re = centroide.get(i, 2)[0];
                    intensidad[i] = (bl + gr + re) / 3;
                    if(intensidad[i]<valorMin){
                        indiceMin=i;
                        valorMin=intensidad[i];
                    }
                }

                double valor1;
                indice1=0;
                valor1=intensidad[0];
                for(int j=0;j<intensidad.length;j++){
                    if(intensidad[j]<valor1){
                        indice1=j;
                        valor1=intensidad[j];
                    }
                }
                double valor2;
                indice2=0;
                boolean entro=false;
                valor2=intensidad[indice1];
                for(int j=0;j<intensidad.length;j++){
                    if(intensidad[j]>valor1 && !entro){
                        indice2 = j;
                        valor2=intensidad[j];
                        entro=true;
                    }
                    if(intensidad[j]<valor2 && intensidad[j]!=valor1) {
                        indice2 = j;
                        valor2 = intensidad[j];
                    }
                }
                entro=false;
                double valor3;
                indice3=0;
                valor3=intensidad[indice2];
                for(int j=0;j<intensidad.length;j++){
                    if(intensidad[j]>valor2 && !entro){
                        indice3=j;
                        valor3=intensidad[j];
                        entro=true;
                    }
                    if(intensidad[j]<valor3 && intensidad[j]!=valor2 && intensidad[j]!=valor1) {
                        indice3 = j;
                        valor3 = intensidad[j];
                    }
                }
                double valor4;
                indice4=0;
                for(int j=0;j<intensidad.length;j++){
                    if(intensidad[j]>valor3){
                        indice4=j;
                        valor4=intensidad[j];
                    }
                }
                //////////////////////////////////////////////////////////////////////////////////////////

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
                celM=new Mat();
                celM=clusters.get(indice1).clone();

                Imgproc.morphologyEx(celM,celM,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(0,0),4);
                Imgproc.morphologyEx(celM,celM,Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
//        Imgproc.medianBlur(celM,celM,3);
//        Imgproc.morphologyEx(celM,celM,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(0,0),2);
//        Imgproc.morphologyEx(celM,celM,Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
//        Imgproc.morphologyEx(celM,celM,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));

//        Imgproc.erode(celM, celM, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
//        Imgproc.dilate(celM, celM, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
                //////////////////////////////////////////////////////Cuenta celulas malignas////////////////////
                Mat cantCM=new Mat();
                cantCM.convertTo(cantCM,CvType.CV_32S);
                Imgproc.cvtColor(celM,celM,Imgproc.COLOR_BGR2GRAY);
                Imgproc.connectedComponents(celM,cantCM);
//                Imgproc.connectedComponentsWithAlgorithm(celM,cantCM,8,CvType.CV_32S,Imgproc.CCL_WU);

                Core.MinMaxLocResult minMaxLocResult=Core.minMaxLoc(cantCM);
                double maxVal=minMaxLocResult.maxVal;
                ///////////////////////////////////////////////////////mejora imagen celulas no malignas///////////
                Mat celNM1=new Mat();
                Mat celNM2=new Mat();
                Mat celNM12=new Mat();
                celNMres=new Mat();

                celNM1=clusters.get(indice2).clone();
                Imgproc.cvtColor(celNM1,celNM1,Imgproc.COLOR_BGR2GRAY);

//Calculo para 4 cluster...

//                celNM2=clusters.get(indice3).clone();
//                Imgproc.cvtColor(celNM2,celNM2,Imgproc.COLOR_BGR2GRAY);
//
//                Core.add(celNM1,celNM2,celNM12);
//
//                Mat celMdilatada=new Mat();
//                celMdilatada=celM.clone();
//
//                Imgproc.dilate(celMdilatada, celMdilatada, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
//                Imgproc.dilate(celMdilatada, celMdilatada, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
//
//                Core.subtract(celNM12,celMdilatada,celNMres);
//
//                Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(0,0),4);
////        Imgproc.medianBlur(celNMres,celNMres,3);
////        Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(0,0),2);
//                Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
//                Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(0,0),2);

//Calculo para 3 clusters...


                Mat celMdilatada=new Mat();
                celMdilatada=celM.clone();

                Imgproc.dilate(celMdilatada, celMdilatada, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
                Imgproc.dilate(celMdilatada, celMdilatada, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));

                Core.subtract(celNM1,celMdilatada,celNMres);

                Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(-1,-1),6);
//        Imgproc.medianBlur(celNMres,celNMres,3);
//        Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(0,0),2);
                Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));
                Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)));

                Imgproc.morphologyEx(celNMres,celNMres,Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.CV_SHAPE_ELLIPSE, new Size(3, 3)),new Point(-1,-1),2);




                Mat cantCNM=new Mat();
                cantCNM.convertTo(cantCNM,CvType.CV_32S);
                Imgproc.connectedComponents(celNMres,cantCNM);
//                Imgproc.connectedComponentsWithAlgorithm(celNMres,cantCNM,8,CvType.CV_32S,Imgproc.CCL_WU);
//                //Imgproc.connectedComponentsWithStats(celNMres,cantCNM,labels,centroide);
                Core.MinMaxLocResult minMaxLocResult2=Core.minMaxLoc(cantCNM);
                double maxVal2=minMaxLocResult2.maxVal;

                double cantT=maxVal+maxVal2;
                porcentajeCM=maxVal*100/cantT;
                double porcentajeCNM=maxVal2*100/cantT;
                procesado=true;

                listaPorcentaje.add((float)porcentajeCM);

//                    final TextView porcentaje = new TextView(RecuentoKi67Activity.this);
//                    LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT);
//                    porcentaje.setTextSize(6 * getResources().getDisplayMetrics().density);
//                    porcentaje.setLayoutParams(layoutParams);
//                    porcentaje.setText("Campo 1: " + String.format("%.1f", porcentajeCM) + "%");


//        Mat mask=Mat.zeros(celNMres.rows()+2,celNMres.cols()+2,celNMres.type());
//
//        Mat celNMresI;
//        Core.bitwise_not(celNMres, celNMres);
//
//        Imgproc.floodFill(celNMres,mask,new Point(0,0),new Scalar(255));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mostrarProcentajeCM();

                        tv1.setText("");

                        for(int i=0;i<listaPorcentaje.size();i++)
                            tv1.append("Campo "+(i+1)+": "+String.format("%.1f",listaPorcentaje.get(i))+"%\n");

                        Utils.matToBitmap(celM,imagenBitmap);
                        iv1.setImageBitmap(imagenBitmap);

                        invalidateOptionsMenu();

                        guardarEnCache();
                    }
                });

                pDialog.dismiss();
            }
        }).start();
    }

    private void mostrarProcentajeCM() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Porcentajes Celulas Anaplasicas Proliferantes");
        builder.setMessage(String.format("%.1f",porcentajeCM)+"%");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void abrirImagen(View v){
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i,SELECT_PICTURE);
    }

    private void calcularPorcentajeTotal() {
        float pTotal=0;
        for(int i=0;i<listaPorcentaje.size();i++)
            pTotal+=listaPorcentaje.get(i);

        pTotal/=listaPorcentaje.size();
        tv1.append("Porcentaje total: "+String.format("%.1f",pTotal)+"%\n");
    }

    private void borrarPorcentajes() {
        listaPorcentaje.clear();
        tv1.setText("");
}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {

            VistaPrevia.selectedImage = data.getData();

            InputStream is;
            try {
                is = getContentResolver().openInputStream(VistaPrevia.selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                imagenBitmap = BitmapFactory.decodeStream(bis);
                String path = getRealPathFromUri(VistaPrevia.selectedImage);
                Bitmap imagenRotada = null;
                try {
                    imagenRotada = rotateImageIfRequired(imagenBitmap, path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //imagen.recycle();
                imagenBitmap = imagenRotada;
                guardarEnCache();
                dimensionarBitmap(VistaPrevia.selectedImage);
                invalidateOptionsMenu();
            } catch (FileNotFoundException e) {
            }

            procesado = false;

        }
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "No seleccionó ninguna imagen", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == OBTENER_COLOR && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();

            pRojoCM = bundle.getInt("rojoCP", rPromedio1);
            pVerdeCM = bundle.getInt("verdeCP", gPromedio1);
            pAzulCM = bundle.getInt("azulCP", bPromedio1);

            pRojoCNM = bundle.getInt("rojoCNP", rPromedio2);
            pVerdeCNM = bundle.getInt("verdeCNP", gPromedio2);
            pAzulCNM = bundle.getInt("azulCNP", bPromedio2);

            pRojoEE = bundle.getInt("rojoEE", rPromedio3);
            pVerdeEE = bundle.getInt("verdeEE", gPromedio3);
            pAzulEE = bundle.getInt("azulEE", bPromedio3);

            valores = true;

            tv2.setText("Colores obtenidos");
        }
        if (requestCode == OBTENER_COLOR && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "No se han obtenidos los colores...", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap rotateImageIfRequired(Bitmap image, String path) throws IOException {

        //String path= getRealPathFromUri(imageUri);
        ExifInterface ei= new ExifInterface(path);
        int orientation= ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(image,90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(image,180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(image,270);
            default:
                return image;
        }

    }

    private static Bitmap rotateImage(Bitmap img, int degree){
        Matrix matrix= new Matrix();
        matrix.postRotate(degree);
        Bitmap imagenRotada= Bitmap.createBitmap(img,0,0,img.getWidth(),img.getHeight(),matrix,true);
        //img.recycle();
        return imagenRotada;
    }

    private String getRealPathFromUri(Uri imgUri){
        String res=null;
        String[] proj={MediaStore.Images.Media.DATA};
        Cursor cursor= getContentResolver().query(imgUri,proj,null,null,null);
        if(cursor.moveToFirst()){
            int colunm_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res= cursor.getString(colunm_index);
        }
        cursor.close();
        return res;

    }

    private void guardarEnCache() {
        Utils.bitmapToMat(imagenBitmap,imagenMatriz);
        Imgproc.cvtColor(imagenMatriz,imagenMatriz,Imgproc.COLOR_BGR2RGB);
//        if(imagenBitmap!=null) {
//            VistaPrevia.imagen = imagenBitmap;
//        }

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
        VistaPrevia.selectedImage=Uri.fromFile(new_file);

        Imgcodecs.imwrite(VistaPrevia.currentFileName,imagenMatriz);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ki67,menu);
        if(procesado){

            menu.findItem(R.id.celulas).setVisible(true);
            menu.findItem(R.id.imagenO).setVisible(true);

        }else{

            menu.findItem(R.id.celulas).setVisible(false);
            menu.findItem(R.id.imagenO).setVisible(false);

        }if(imagenBitmap!=null)
            menu.findItem(R.id.calcularPorcentajes).setVisible(true);
        else
            menu.findItem(R.id.calcularPorcentajes).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.agregarImagen:
                abrirImagen(null);
                break;
            case R.id.tomarFoto:
                Intent intentV=new Intent(this,VistaPrevia.class);
                startActivity(intentV);
                finish();
                break;
            case R.id.borrarColorKi67:

                pRojoCM = rPromedio1;
                pVerdeCM = gPromedio1;
                pAzulCM = bPromedio1;

                pRojoCNM = rPromedio2;
                pVerdeCNM = gPromedio2;
                pAzulCNM = bPromedio2;

                pRojoEE = rPromedio3;
                pVerdeEE = gPromedio3;
                pAzulEE = bPromedio3;

                valores=false;

                tv2.setText("Colores predeterminados");
                break;
            case R.id.obtenerColorKi67:
                Intent intent=new Intent(this,Ki67ObtenerColorActivity.class);
                startActivityForResult(intent,OBTENER_COLOR);
                break;
            case R.id.calcularPorcentajesT:
                if(listaPorcentaje.size()>1)
                    calcularPorcentajeTotal();
                else
                    Toast.makeText(this, "Debe calcular al menos dos porcentajes...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.calcularPorcentajes:
                if(imagenBitmap!=null)
                    calcularPorcentaje();
                else
                    Toast.makeText(this, "Seleccione una imagen...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.borrarP:
                if(!listaPorcentaje.isEmpty())
                    borrarPorcentajes();
                else
                    Toast.makeText(this, "Debe calcular al menos un porcentaje", Toast.LENGTH_SHORT).show();
                break;

            case R.id.imagenO:
                uriImagenOriginal= VistaPrevia.selectedImage;
                dimensionarBitmap(uriImagenOriginal);
                break;
            case R.id.celM:
//                Utils.matToBitmap(clase1Mat,imagenBitmap);
//                iv1.setImageBitmap(imagenBitmap);
                iv1.setImageBitmap(clase1P);
                break;
            case R.id.celNM:
//                Utils.matToBitmap(celNMres,imagenBitmap);
//                iv1.setImageBitmap(imagenBitmap);
                iv1.setImageBitmap(clase2P);
                break;
            case R.id.celulasPyNP:
                iv1.setImageBitmap(imBitmapJuntas);
                break;
            case R.id.comparativa:
                Intent intentComparativa=new Intent(this,Ki67ComparativaActivity.class);
                startActivity(intentComparativa);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
