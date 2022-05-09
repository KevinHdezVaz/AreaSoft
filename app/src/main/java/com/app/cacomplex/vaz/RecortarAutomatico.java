package com.app.cacomplex.vaz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;



public class RecortarAutomatico{
    private static boolean banderaFinColor=false;

    Bitmap imagen;
    private static int[] centroCirculo=new int[2];

    public static final String TAG="MainActivityPrincipal";

    /*
    inicializa opencv
    static{
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"Cargo exitosamente :)");
        }else
            Log.d(TAG,"No pudo cargar el paquete openCV :(");
    }


     */

    public RecortarAutomatico(){
    }
    public static Bitmap recortarImagenAutomatico(Context context,Bitmap imagen,float densidad) {

        Bitmap imagenReducida;
        int reduccion;

        if (imagen.getWidth() * imagen.getHeight() > 2000000 && imagen.getWidth() * imagen.getHeight() < 8000000) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            reduccion = 2;
            options.inMutable = true;

            imagenReducida = BitmapFactory.decodeFile(VistaPrevia.currentFileName, options);
        } else if (imagen.getWidth() * imagen.getHeight() > 8000000) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            reduccion = 4;
            options.inMutable = true;

            imagenReducida = BitmapFactory.decodeFile(VistaPrevia.currentFileName, options);
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            reduccion = 1;
            options.inMutable = true;

            imagenReducida = BitmapFactory.decodeFile(VistaPrevia.currentFileName, options);
        }

    
        int ancho=0;
        int alto=0;
        int pixel;
        int color;
        int colorVerificacion=0;
        int min[]=new int[2];
        int max[]=new int[2];
        int ubicacion[]=new int[4];
        int contadorDistanciaIguales=0;
        int contadorDistanciaMenores=0;
        int distancia=0;
        int distanciaMax=0;
        int radio=0;
        boolean detectaIntensidad=false;
        boolean errorVerficacionFlags=false;
        int esquinaXi,esquinaYi,esquinaXf,esquinaYf;

        Log.i("Recortar",String.valueOf(imagenReducida.getWidth())+" "+String.valueOf(imagenReducida.getHeight()));


           for (int i = 0; i < imagenReducida.getHeight(); i++) {
               detectaIntensidad = false;
               for (int j = 0; j < imagenReducida.getWidth(); j++) {
                   pixel = imagenReducida.getPixel(j, i);
                   color = Color.red(pixel);
                   if(i==0)
                    colorVerificacion+=color;
                   if (color == 255) {
                       if (!detectaIntensidad) {
                           min[0] = j;
                           min[1] = i;
                       }
                       detectaIntensidad = true;
                       banderaFinColor = true;
                   }
                   if (banderaFinColor) {
                       max[0] = j;
                       max[1] = i;

                       distancia = max[0] - min[0];

                       if (distancia > distanciaMax) {
                           contadorDistanciaIguales=0;
                           distanciaMax = distancia;
                           ubicacion[0] = min[0];
                           ubicacion[1] = min[1];
                           ubicacion[2] = max[0];
                           ubicacion[3] = max[1];
                       }else if(distancia == distanciaMax){
                           contadorDistanciaIguales++;
                       }
                       banderaFinColor = false;
                   }
               }
               if(distancia<distanciaMax){
                   contadorDistanciaMenores++;
                   if(contadorDistanciaMenores>10)
                    break;
               }
               if(i==0) {
                   colorVerificacion = colorVerificacion / imagenReducida.getWidth();
                   if (colorVerificacion > 50) {
                       errorVerficacionFlags = true;
                       break;
                   }
               }
           }

           if(!errorVerficacionFlags) {
               radio = Math.round(distanciaMax / 2f);

               ubicacion[1]=ubicacion[1]+contadorDistanciaIguales/2;
               ubicacion[3]=ubicacion[3]+contadorDistanciaIguales/2;

               centroCirculo[0] = ubicacion[0] + (int) Math.round(radio);
               centroCirculo[1] = (int) Math.round(ubicacion[1]);

               esquinaYi = (int) Math.round((centroCirculo[1] - Math.sin(45 * Math.PI / 180) * radio)+3*densidad);
               esquinaXi = (int) Math.round((centroCirculo[0] - Math.cos(45 * Math.PI / 180) * radio)+3*densidad);

               esquinaYf = (int) Math.round((centroCirculo[1] + Math.sin(45 * Math.PI / 180) * radio)-3*densidad);
               esquinaXf = (int) Math.round((centroCirculo[0] + Math.cos(45 * Math.PI / 180) * radio)-3*densidad);

               ////////////////////Visualizar rectangulo y diametro//////////////////
//        Canvas canvas=new Canvas(imagenReducida);
//        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(1);
////        canvas.drawCircle(centroCirculo[0],centroCirculo[1],radio,paint);
//        canvas.drawRect(esquinaXi,esquinaYi,esquinaXf,esquinaYf,paint);
//        paint.setColor(Color.YELLOW);
//        canvas.drawLine(ubicacion[0],ubicacion[1],ubicacion[2],ubicacion[3],paint);

//                Utils.matToBitmap(imagenMatrizProcesada,imagen);
               //Debemos comentar imagen y cambiar el return por imagenReducida//
               ///////////////////////////////////////////////////////////

               imagen = Bitmap.createBitmap(imagen, esquinaXi * reduccion, esquinaYi * reduccion,
                       (esquinaXf - esquinaXi) * reduccion, (esquinaYf - esquinaYi) * reduccion);

               return imagen;
           }else{
//               Toast.makeText(context,"Debes elegir u obtener una imagen histologica no recortada previamente..",Toast.LENGTH_LONG).show();
               return  null;
           }
    }
}
