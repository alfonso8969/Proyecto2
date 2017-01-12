package alfonso8969.com.proyecto2.clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Creado por alfonso en fecha 29/10/2016.
 */

public  class PhotoUtils{
    private  Context mContext;
    private BitmapFactory.Options generalOptions;

    public PhotoUtils(Context context) {
        mContext = context;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createTemporaryFile(String part, String ext,Context myContext) throws Exception {
        File tempDir = myContext.getExternalCacheDir();
        assert tempDir != null;
        tempDir = new File(tempDir.getAbsolutePath() + "/temp/");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public Bitmap getImage(Uri uri) {
      //  Bitmap result = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream is;
        try {
            is = mContext.getContentResolver().openInputStream(uri);
           // result = BitmapFactory.decodeStream(is, null, options);
            assert is != null;
            is.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
        this.generalOptions = options;
        return scaleImage(options, uri, 300);
    }

    private static int nearest2pow(int value) {
        return value == 0 ? 0
                : (32 - Integer.numberOfLeadingZeros(value - 1)) / 2;
    }


    @SuppressWarnings("deprecation")
    private Bitmap scaleImage(BitmapFactory.Options options, Uri uri,
                              int targetWidth) {
        if (options == null)
            options = generalOptions;
        Bitmap bitmap = null;
        double ratioWidth = ((float) targetWidth) / (float) options.outWidth;
        double ratioHeight = ((float) targetWidth) / (float) options.outHeight;
        double ratio = Math.min(ratioWidth, ratioHeight);
        int dstWidth = (int) Math.round(ratio * options.outWidth);
        int dstHeight = (int) Math.round(ratio * options.outHeight);
        ratio = Math.floor(1.0 / ratio);
        int sample = nearest2pow((int) ratio);

        options.inJustDecodeBounds = false;
        if (sample <= 0) sample = 1;
        options.inSampleSize = sample;
        options.inPurgeable = true;
        try {
            InputStream is;
            is = mContext.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (sample > 1)
                bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight,true);
            assert is != null;
            is.close();
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
