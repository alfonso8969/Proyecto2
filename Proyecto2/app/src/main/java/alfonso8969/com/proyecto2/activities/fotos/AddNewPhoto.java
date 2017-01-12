package alfonso8969.com.proyecto2.activities.fotos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.Menu_principal;
import alfonso8969.com.proyecto2.clases.PhotoUtils;
import alfonso8969.com.proyecto2.clases.Save;

/**
 * Creado por alfonso en fecha 29/10/2016.
 */

public class AddNewPhoto extends AppCompatActivity {
    private AlertDialog _photoDialog;
    private Uri mImageUri;
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040;
    private PhotoUtils photoUtils;
    private Button photoButton, hechoButton;
    private ImageView photoViewer;
    private String titulo;
    boolean fromShare;
    public static String nombreFotoBuscar;
    public Context myContex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_photo);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myContex=getApplicationContext();
        photoUtils = new PhotoUtils(this);
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        titulo=extras.getString("nombreFoto");
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                fromShare = true;
            } else if (type.startsWith("image/")) {
                fromShare = true;
                mImageUri = intent
                        .getParcelableExtra(Intent.EXTRA_STREAM);
                getImageShare(mImageUri);
            }
        }
        photoButton = (Button) findViewById(R.id.photoButton);
        hechoButton =(Button)findViewById(R.id.hechoButton);
        photoViewer = (ImageView) findViewById(R.id.photoViewer);
        TextView title = (TextView) findViewById(R.id.title);
        if(titulo.contains("."))
            titulo = titulo.substring(0,titulo.length()-4);
        title.setText(titulo);
        getPhotoDialog();
        setPhotoButton();
        setHechoButton();

    }

    private void setPhotoButton(){
        photoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(!getPhotoDialog().isShowing() && !isFinishing())
                    getPhotoDialog().show();
            }
        });
    }

    private AlertDialog getPhotoDialog() {
        if (_photoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    this);
            builder.setTitle(R.string.photo_source);
            builder.setPositiveButton(R.string.cámara, new DialogInterface.OnClickListener() {

                @SuppressWarnings("ResultOfMethodCallIgnored")
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    File photo=null;

                    try {

                        // lugar donde se guardará la foto tomada
                        if(titulo.contains("."))
                            titulo = titulo.substring(0,titulo.length()-4);

                            photo = PhotoUtils.createTemporaryFile(titulo, ".png", AddNewPhoto.this);
                            nombreFotoBuscar = photo.getName();
                            Toast.makeText(AddNewPhoto.this, nombreFotoBuscar + " ha sido creada", Toast.LENGTH_LONG).show();
                            //photo.delete();

                    } catch (Exception e) {
                        Log.v(getClass().getSimpleName(),
                                "No se puede crear fichero de la foto tomada!");
                    }
                    mImageUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);
                }

            });

            builder.setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
                }

            });

            builder.setNeutralButton("Cancelar",null);
            _photoDialog = builder.create();

        }
        return _photoDialog;
    }

    public void setHechoButton(){
        hechoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(getApplication().getApplicationContext(), Menu_principal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null)
            outState.putString("Uri", mImageUri.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("Uri")) {
            mImageUri = Uri.parse(savedInstanceState.getString("Uri"));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            getImageGallery(mImageUri);
        } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
                && resultCode == RESULT_OK) {
            getImageCamara(mImageUri);
        }
    }

    public void getImageGallery(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {
            setImageBitmapGallery(bounds);
        } else {
            showErrorToast();

        }
    }
    public void getImageCamara(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {
            setImageBitmapCamara(bounds);
        } else {
            showErrorToast();

        }
    }

    public void getImageShare(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {
            setImageBitmapShare(bounds);
        } else {
            showErrorToast();

        }
    }

    private void setImageBitmapGallery(Bitmap bitmap) {
        photoViewer.setImageBitmap(bitmap);
        //photoViewer.buildDrawingCache();
        //Bitmap bitmap1 = photoViewer.getDrawingCache();
        Save guardarFoto = new Save();
        nombreFotoBuscar=titulo+".png";
        guardarFoto.SaveImage(myContex, bitmap,titulo);
    }

    private void setImageBitmapCamara(Bitmap bitmap) {
        photoViewer.setImageBitmap(bitmap);
    }

    private void setImageBitmapShare(Bitmap bitmap) {
        photoViewer.setImageBitmap(bitmap);
    }

    private void showErrorToast() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }
}
