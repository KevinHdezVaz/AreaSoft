package com.app.cacomplex.vaz;


import static com.app.cacomplex.vaz.RecortarAutomatico.TAG;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;
import com.tfb.fbtoast.FBToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class VistaPrevia extends AppCompatActivity {


    int r,g,b,a,gr;

    ImageView iv1;
    static Bitmap imagen;
    Bitmap imagenFiltrada;
    String currentDirectory;
    static String currentFileName;
    static String nameFileDirectory;
    RelativeLayout layout1;
    boolean  destruido=false;
    int grados=90;
    Spinner s1;
    ImageView cerrar ;


    public static final int REQUEST_CODE_TAKE_PHOTO = 0 /*1*/;
    private String mCurrentPhotoPath;
    private Uri photoURI;
    int[] pixels;
        Map config = new HashMap();
    Dialog epicDialog,epicDialog2;
    ImageButton button;

    private Uri imagePath;
    final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE=2;
    final int REQUEST_TAKE_PHOTO=4;
    final int SELECT_PICTURE=8;

/////////////////////////////////////////////////////////////////////////////////////////////////////



    NotificationCompat.Builder mBuilder;
    private int REQUEST_CROP_ICON=16;
     public static Uri selectedImage;
    static boolean AgregarMuestra=false;
ImageButton buttonupload;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Capturar imagen");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);





        button = findViewById(R.id.button);

        buttonupload = findViewById(R.id.botonupload);
        buttonupload.setEnabled(false);


        buttonupload.setOnClickListener(view -> {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                // Si hay conexión a Internet en este momento

                metodoenviarimagenCamara();
                mostrarInfo();

            } else {
                // No hay conexión a Internet en este momento
                FBToast.errorToast(VistaPrevia.this,"Necesitas conexión a internet para continuar", FBToast.LENGTH_LONG);


            }

        });

        // setTitle("Imagen Previa");

        iv1=(ImageView)findViewById(R.id.iv1);
        layout1=(RelativeLayout)findViewById(R.id.layout1);

        //s1=(Spinner)findViewById(R.id.s1);

        if(currentFileName!=null) {
            imagen = BitmapFactory.decodeFile(currentFileName);
            iv1.setImageBitmap(imagen);
        }
        if(isFirstTime()){

            ShowcaseConfig config2 = new ShowcaseConfig();
            config2.setMaskColor(getResources().getColor(R.color.purple_500));
            config2.setRenderOverNavigationBar(true);
            config2.setDelay(500);

            final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(VistaPrevia.this, "sacatepec");
            sequence.setConfig(config2);
            button.post(new Runnable() {
                @Override
                public void run() {
                    sequence.addSequenceItem(button,"Para capturar una imagen, toca este icono.","->CONTINUAR");

                    sequence.addSequenceItem(buttonupload,"Ya tomada la fotografia, toca este icono para cargar la imagen y posteriormente procesarla. :)","->CONTINUAR");



                    sequence.start();
                }
            });



        }

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                        if(currentFileName!=null) {
//                            File file = new File(currentFileName);
//                            Uri uri = Uri.fromFile(file);
//                            Intent i = new Intent(Intent.ACTION_VIEW);
//                            i.setDataAndType(uri, "image/*");
//                            startActivity(i.createChooser(i,"Eliga la aplicación"));
//                        }else {
//                            Toast.makeText(VistaPrevia.this, "No existe imagen previa", Toast.LENGTH_SHORT).show();
//                        }
                if(imagen!=null){
                    Intent i=new Intent(VistaPrevia.this,MainActivityVisualizarFoto.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(VistaPrevia.this, "No existe imagen previa...", Toast.LENGTH_SHORT).show();
                }

            }
        });


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(VistaPrevia.this,"El permiso de usar la camara fue denegado",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }//Fin del onCreate.

///Manejo de los resultados de los permisos./////////////////////////////////////////////////////////////

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void editar(View v){
        if(currentFileName!=null){
            Intent intent = new Intent(this, Edicion.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,"No existe imagen previa",Toast.LENGTH_SHORT).show();
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////




///Método que realiza la extracción de cada pixel del bitmap par
/// a luego extraerle el ARGB.
///Posteriormente se promedia el rgb y se lo setea a cada pixel de la imagen.
private boolean isFirstTime()
{
    SharedPreferences preferences = getPreferences(MODE_PRIVATE);
    boolean ranBefore = preferences.getBoolean("RanBefore2", false);
    if (!ranBefore) {
        // first time
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("RanBefore2", true);
        editor.commit();
    }
    return !ranBefore;


////////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////////

///Método que guarda la imagen en un directorio especifico de la memoria externa.////////////////////

    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

///Método que abre la cámara, realiza una captura y guarda la imagen en la ubicación definida previamente.////



    public void foto(View v) {

                dispatchTakePictureIntent();


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                photoURI = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                //Uri photoURI = FileProvider.getUriForFile(AddActivity.this, "com.example.android.fileprovider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);

                buttonupload.setEnabled(true);


            }
        }
    }

public void metodoenviarimagenCamara()
{
//envia la imagen de la galeria al servidor
    Log.d(TAG, ": "+" button clicked");
    MediaManager.get().upload(photoURI).callback(new UploadCallback() {
        @Override
        public void onStart(String requestId) {
            Log.d(TAG, "onStart: "+"started");


        }

        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
            Log.d(TAG, "onStart: "+"uploading");


        }

        @Override
        public void onSuccess(String requestId, Map resultData) {
            Log.d(TAG, "onStart: "+"usuccess");

            FBToast.successToast(VistaPrevia.this,"Cargando...", FBToast.LENGTH_LONG);

            epicDialog.dismiss();
            Intent intent = new Intent(VistaPrevia.this, web2.class);
             intent.putExtra("Clave", Objects.requireNonNull(resultData.get("url")).toString());
            startActivity(intent);

        }

        @Override
        public void onError(String requestId, ErrorInfo error) {
            Log.d(TAG, "onStart: "+error);
            Toast.makeText(VistaPrevia.this, "Error", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onReschedule(String requestId, ErrorInfo error) {
            Log.d(TAG, "onStart: "+error);
        }
    }).dispatch();



}


    public void mostrarInfo(){

        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.about2);


        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        epicDialog.show();
epicDialog.setCancelable(false);





    }





    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                iv1.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////


    public  File createFilePath() {

         nameFileDirectory="HistoSoft"+"/";

        File file=getAlbumStorageDir(nameFileDirectory);
        currentDirectory=file.getAbsoluteFile().toString();
        if(!file.exists()&&!file.mkdirs()){
            Toast.makeText(this,"No se pudo crear el directorio para grabar la imagen",Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("ddMMyyyyhhmmss");
        String date=simpleDateFormat.format(new Date());
        String name="img"+date+".jpg";
        String file_name=file.getAbsolutePath()+"/"+name;
        File new_file=new File(file_name);
        currentFileName=file_name;
        return new_file;
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

/////Método que obtiene el directorio donde se va a ubicar la imágen.///////////////////////////////////

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory(), albumName);
        file.mkdirs();
        if (!file.mkdirs()) {
            Log.e("directorio", "Directory not created");
        }
        return file;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////

////Manejo del resultado de la cámara./////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////////////////////////////////

///Escala la imagen y setea la imagen resultante en el imageView.//////////////////////////////////////////

    private void setPic() {


        // Get the dimensions of the View
        int targetW = iv1.getWidth();
        int targetH = iv1.getHeight();
        //if(!(targetW==0||targetH==0)) {
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentFileName, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;


        imagen = BitmapFactory.decodeFile(currentFileName, bmOptions);

        Bitmap imagenRotada= null;
        try {
            imagenRotada = rotateImageIfRequired(imagen,currentFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagen=imagenRotada;
        iv1.setImageBitmap(imagen);

        //}else{}
        //Drawable imagen2=Drawable.createFromPath(mCurrentPhotoPath);
        //mImageView.setImageDrawable(imagen2);
    }

    private Bitmap rotateImageIfRequired(Bitmap image, String path) throws IOException{

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

///Método que realiza un escaneo de los archivos de multimedia para que aparezca en la galería.///////
    private void galleryAddPic() {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(currentFileName);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    this.sendBroadcast(mediaScanIntent);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////
    public void rotarImagen(int grados){
    if(imagen!=null) {
        Matrix matrix = new Matrix();
        matrix.postRotate(grados);
        int w = imagen.getWidth();
        int h = imagen.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(imagen, 0, 0, w, h, matrix, false);
        //imagen.recycle();
        imagen = bitmap;
        iv1.setImageBitmap(imagen);

    }else{
        Toast.makeText(this,"No existe imagen previa",Toast.LENGTH_SHORT).show();
    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu2,menu);

        if(imagen==null){
            menu.findItem(R.id.aplicarFiltro).setVisible(false);
            menu.findItem(R.id.cortar).setVisible(false);
            menu.findItem(R.id.medir).setVisible(false);
            menu.findItem(R.id.editar).setVisible(false);
            menu.findItem(R.id.rotar).setVisible(false);
            menu.findItem(R.id.cortar_automatico).setVisible(false);
            menu.findItem(R.id.recuentoKi67).setVisible(false);
        }else{
            menu.findItem(R.id.aplicarFiltro).setVisible(true);
            menu.findItem(R.id.cortar).setVisible(true);
            menu.findItem(R.id.medir).setVisible(true);
            menu.findItem(R.id.editar).setVisible(true);
            menu.findItem(R.id.rotar).setVisible(true);
            menu.findItem(R.id.cortar_automatico).setVisible(true);
            menu.findItem(R.id.recuentoKi67).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.cortar:
                if(currentFileName != null) {

                    Intent intentCortar = new Intent(this,CortarImagen.class);
                    startActivity(intentCortar);

                }else{
                    Toast.makeText(this,"No existe imagen previa...",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cortar_automatico:
                if(currentFileName != null) {
                    final ProgressDialog dialog=new ProgressDialog(this);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("Procesando...");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap imagenCortada;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();
                                }
                            });
                            imagenCortada= RecortarAutomatico.recortarImagenAutomatico(VistaPrevia.this,imagen,getResources().getDisplayMetrics().density);
                            if(!(imagenCortada==null)){
                                imagen=imagenCortada.copy(Bitmap.Config.ARGB_8888,true);
                                imagenCortada.recycle();

                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(VistaPrevia.this,"Debes elegir u obtener una imagen histologica no recortada previamente..",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            dialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv1.setImageBitmap(imagen);
                                }
                            });
                        }
                    }).start();

                }else{
                    Toast.makeText(this,"No existe imagen previa...",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.medir:
                if(currentFileName != null) {
//                    if(imagen!=null)
//                        imagen.recycle();

                }else{
                    Toast.makeText(this,"No existe imagen previa...",Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.actualizar:
//                recreate();
//                Toast.makeText(this,"Actualizado...",Toast.LENGTH_SHORT).show();
//                break;
            case R.id.editar:
//                if(imagen!=null)
//                    imagen.recycle();
                editar(null);
                break;
            case R.id.aplicarFiltro:
//                if(imagen!=null)
//                    imagen.recycle();

                break;
//            case R.id.guardarImagen:
//                guardarImagen(null);
//                break;
            case R.id.rotarDerecha:
                rotarImagen(90);
                break;
            case R.id.rotarIzquierda:
                rotarImagen(-90);
                break;

            case R.id.recuentoKi67A:
                 break;
            case R.id.recuentoKi67M:
                Intent intentRecuentoM= new Intent(this, RecuentoKi67ManualActivity.class);
                startActivity(intentRecuentoM);
                break;
            case R.id.acercaDe:
                Intent intentAcercaDe=new Intent(this,AcercaDe2.class);
                startActivity(intentAcercaDe);
                break;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentFileName!=null) {
            imagen = BitmapFactory.decodeFile(currentFileName);
            iv1.setImageBitmap(imagen);
            invalidateOptionsMenu();
        }else{
            invalidateOptionsMenu();
           // iv1.setImageResource(R.drawable.ic_image_black_36dp);
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("¿Estas seguro que quieres salir?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAndRemoveTask();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");// if you want to you can use pdf/gif/video
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
        buttonupload.setEnabled(true);


    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        imagePath=data.getData();
                        Picasso.get().load(imagePath).into(iv1);

                    }
                }
            });
}
