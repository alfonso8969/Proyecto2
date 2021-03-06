package alfonso8969.com.proyecto2.fragments.clientes;

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
 * Creado por alfonso en fecha 08/11/2016.
 */

public class Clientes_ItemsFragment extends Fragment {
    public TextView tvClienteID,nombreCompañia,nombreContacto,puestoContacto,telefFaxCliente,direcCodPostCliente,paCiuReCliente;
    public String clienteID, nameCompañia, nameContacto,puesto,direcCoPos,teleFax,paCiuRe;
    public DataBaseHelper myDBHelper;
    public Cursor myCursor;
    public ListView lv;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView=inflater.inflate(R.layout.clientes_itemsfragment, container, false);
        lv=(ListView)rootView.findViewById(R.id.lista_Clientes);
        tvClienteID =(TextView) rootView.findViewById(R.id.tv_clienteID);
        nombreCompañia=(TextView)rootView.findViewById(R.id.tv_nombre_compañia);
        nombreContacto=(TextView)rootView.findViewById(R.id.tv_nombre_contacto);
        puestoContacto=(TextView)rootView.findViewById(R.id.tv_puesto_contacto);
        telefFaxCliente=(TextView)rootView.findViewById(R.id.tv_telefono_fax);
        direcCodPostCliente=(TextView)rootView.findViewById(R.id.tv_direccion_cliente);
        paCiuReCliente=(TextView)rootView.findViewById(R.id.tv_PaCiuRe_cliente);




        Bundle bundle = getArguments();

        if(bundle != null) {

            boolean restart = bundle.getBoolean("restart");

            if (restart) {
                lv.setAdapter(null);
            } else {

                clienteID = (String) bundle.get("clienteId");
                myDBHelper = new DataBaseHelper(getActivity());


                try {
                    myDBHelper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myDBHelper.openDatabase();
                myCursor = myDBHelper.fetchClienteById(clienteID);
                myDBHelper.close();
                if (myCursor != null) {
                    clienteID=(myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                    nameCompañia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                    nameContacto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(3))));
                    puesto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                    String direccion = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(5))));
                    String ciudad = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(6))));
                    String region = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(7))));
                    String codPos = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(8))));
                    String pais = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(9))));
                    String telef = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(10))));
                    String fax = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(11))));

                    tvClienteID.setText(clienteID);
                    direcCoPos = direccion + "---" + codPos;
                    teleFax = telef + "---" + fax;
                    paCiuRe = pais + "---" + ciudad + "---" + region;
                    nombreCompañia.setText(nameCompañia);
                    nombreContacto.setText(nameContacto);
                    puestoContacto.setText(puesto);
                    telefFaxCliente.setText(teleFax);
                    direcCodPostCliente.setText(direcCoPos);
                    paCiuReCliente.setText(paCiuRe);

                }
            }
        }

        return  rootView;
    }
}
