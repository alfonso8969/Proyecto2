package alfonso8969.com.proyecto2.clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Creado por alfonso en fecha 03/11/2016.
 */

public class Save {
    private Context TheThis;
    private String nombreArchivo;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void SaveImage(Context context, Bitmap ImageToSave, String nombreArchivo) {
        this.nombreArchivo=nombreArchivo;
        TheThis = context;
        File dir = TheThis.getExternalCacheDir();
        assert dir != null;
        String nameOfFolder = "/temp";
        dir = new File(dir.getAbsolutePath()+ nameOfFolder);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, nombreArchivo  + ".png");
        if(!file.exists()) {
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                ImageToSave.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                fOut.flush();

                fOut.close();
                MakeSureFileWasCreatedThenMakeAvailable(file);
                AbleToSave(file);

            } catch (IOException e) {
                UnableToSave(e);
            }
        }else{
            Toast.makeText(context, "Error al guardar imagen"+"\n"+"La foto ya existe,pruebe con otro nombre"
                    , Toast.LENGTH_LONG).show();

        }

    }

    private void MakeSureFileWasCreatedThenMakeAvailable(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }


    private void UnableToSave(IOException e) {
        Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!"+"\n"+e.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void AbleToSave(File file) {
        Toast.makeText(TheThis, "Foto: "+nombreArchivo+".png guardada en:"+"\n"+ file.getPath()
                , Toast.LENGTH_LONG).show();
    }
}
