package alfonso8969.com.proyecto2.fragments.empleados;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.IOException;
import java.io.InputStream;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 18/10/2016.
 */

public class Empleado_ItemsFragment extends Fragment {
    public TextView nombreCompletoEmpleado,puestoEmpleado,telefExtEmpleado,fechas;
    public TextView direcCodPostEmpleado,paCiuReEmpleado,reportAEmpleado,notasEmpleado;
    public ImageView imagenEmpleado;
    public String empleadoID,nombreCompleto,puesto,direcCoPos,teleExt,paCiuRe,notas, reportarA,fechNac_fechCont;
    private Context context;
    public DataBaseHelper myDBHelper;
    public Cursor myCursor;
    public ListView lv;
    public InputStream myInput;
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.empleados_itemsfragment, container, false);
        lv=(ListView)rootView.findViewById(R.id.lista_Empleados) ;
        imagenEmpleado=(ImageView) rootView.findViewById(R.id.imagenEmpleado);
        nombreCompletoEmpleado=(TextView)rootView.findViewById(R.id.tv_nombre_completo);
        fechas=(TextView)rootView.findViewById(R.id.tv_fechas);
        puestoEmpleado=(TextView)rootView.findViewById(R.id.tv_puesto);
        telefExtEmpleado=(TextView)rootView.findViewById(R.id.tv_telefono);
        direcCodPostEmpleado=(TextView)rootView.findViewById(R.id.tv_direccion);
        paCiuReEmpleado=(TextView)rootView.findViewById(R.id.tv_PaCiuRe);
        reportAEmpleado=(TextView)rootView.findViewById(R.id.tv_reportar);
        notasEmpleado=(TextView)rootView.findViewById(R.id.tv_notas);

        context=getActivity().getApplicationContext();

        Bundle bundle = getArguments();

        if(bundle != null) {

            boolean restart = bundle.getBoolean("restart");

            if (restart) {
                lv.setAdapter(null);
            } else {

                empleadoID = (String) bundle.get("empleadoId");
                myDBHelper = new DataBaseHelper(getActivity());


                try {
                    myDBHelper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myDBHelper.openDatabase();
                myCursor = myDBHelper.fetchEmpleadoById(empleadoID);
                myDBHelper.close();
                if (myCursor != null) {
                    String nombre = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                    String apellido = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                    puesto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(3))));
                    String tratoCortesia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                    String fechNac = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(5))));
                    String fecContr = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(6))));
                    String direccion = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(7))));
                    String ciudad = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(8))));
                    String region = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(9))));
                    String codPos = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(10))));
                    String pais = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(11))));
                    String telef = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(12))));
                    String exten = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(13))));
                    notas = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(14))));
                    reportarA = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(15))));
                    nombreCompleto = tratoCortesia + " " + apellido + ", " + nombre;
                    fechNac_fechCont = fechNac + "---" + fecContr;
                    direcCoPos = direccion + "---" + codPos;
                    teleExt = telef + "---" + exten;
                    paCiuRe = pais + "---" + ciudad + "---" + region;
                    nombreCompletoEmpleado.setText(nombreCompleto);
                    fechas.setText(fechNac_fechCont);
                    puestoEmpleado.setText(puesto);
                    telefExtEmpleado.setText(teleExt);
                    direcCodPostEmpleado.setText(direcCoPos);
                    paCiuReEmpleado.setText(paCiuRe);
                    reportAEmpleado.setText(reportarA);
                    notasEmpleado.setText(notas);

                    String avatarUri = myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(16)));
            try {
                myInput = getActivity().getAssets().open(avatarUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (myInput!=null) Glide
                    .with(context)
                    .load(Uri.parse("file:///android_asset/" + avatarUri))
                    .asBitmap()
                    .error(R.drawable.ic_launcher2)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(imagenEmpleado) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable drawable
                                    = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            drawable.setCircular(true);
                            imagenEmpleado.setImageDrawable(drawable);
                        }
                    });
            else Glide
                    .with(context)
                    .load(Uri.parse("file:///sdcard/Android/data/alfonso8969.com.proyecto2/cache/temp/" + avatarUri))
                    .asBitmap()
                    .error(R.drawable.ic_launcher2)
                    .centerCrop()
                    .into(new BitmapImageViewTarget(imagenEmpleado) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable drawable
                                    = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            drawable.setCircular(true);
                            imagenEmpleado.setImageDrawable(drawable);
                        }
                    });
        }
    }
        }
            return rootView;

    }

}
