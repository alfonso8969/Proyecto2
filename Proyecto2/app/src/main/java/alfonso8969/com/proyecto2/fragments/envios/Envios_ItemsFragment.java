package alfonso8969.com.proyecto2.fragments.envios;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 15/11/2016.
 */

public class Envios_ItemsFragment extends Fragment {
    public TextView tvNombreCompañia,tvTelefono;
    public String envioId, strNombreCompañia,strTelefono;
    public DataBaseHelper myDBHelper;

    public Cursor myCursor;
    public ListView lv;
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.envios_itemsfragment, container, false);

        lv=(ListView)rootView.findViewById(R.id.lista_Envios);
        tvNombreCompañia=(TextView)rootView.findViewById(R.id.tv_nombre_compañiaEnvios);
        tvTelefono=(TextView)rootView.findViewById(R.id.tv_telefonoEnvios);

        Bundle bundle = getArguments();

        if(bundle != null) {

            boolean restart = bundle.getBoolean("restart");

            if (restart) {
                lv.setAdapter(null);
            } else {

                envioId = (String) bundle.get("envioId");
                myDBHelper = new DataBaseHelper(getActivity());


                try {
                    myDBHelper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myDBHelper.openDatabase();
                myCursor = myDBHelper.fetchEnvioById(envioId);
                myDBHelper.close();
                if (myCursor != null) {

                    strNombreCompañia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                    strTelefono = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                    tvNombreCompañia.setText(strNombreCompañia);
                    tvTelefono.setText(strTelefono);
                    
                }
            }
        }

        return  rootView;
    }

}
