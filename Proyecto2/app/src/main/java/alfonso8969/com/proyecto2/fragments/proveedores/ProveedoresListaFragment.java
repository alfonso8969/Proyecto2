package alfonso8969.com.proyecto2.fragments.proveedores;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.clientes.ClientesActivity;
import alfonso8969.com.proyecto2.activities.proveedores.ProveedoresActivity;
import alfonso8969.com.proyecto2.adapters.ProveedoresListaAdapter;
import alfonso8969.com.proyecto2.clases.RecyclerItemClickListener;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 08/11/2016.
 */

public class ProveedoresListaFragment extends Fragment {
    RecyclerView recyclerViewProveedores;
    public static final String TAG="RecyclerViewProveedoresFragment";
    String proveedorId;
    ProveedoresListaAdapter proveedoresListaAdapter;
    DataBaseHelper myDBHelper;
    TextView delete;
    TextView nombreCompañia;
    String nameCompañia;
    FloatingActionButton fb;
    public  ViewGroup containerProveedor;
    public static LayoutInflater inflaterProveedor;

    public interface CallBacksProveedores{
        void onItemSelected(String clienteId);
    }

    public  interface Refresh{
        void refreshList();
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        final View viewRoot = inflater.inflate(R.layout.lista_proveedores_fragment, container, false);
        viewRoot.setTag(TAG);
        containerProveedor=container;
        inflaterProveedor=inflater;
        recyclerViewProveedores = (RecyclerView) viewRoot.findViewById(R.id.lista_Proveedores);
        recyclerViewProveedores.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        fb = (FloatingActionButton)viewRoot.findViewById(R.id.fbProveedores);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateView(inflater, container, saveInstanceState);
                final View viewAgregarNuevoProveedor = inflater.inflate(R.layout.insertar_proveedor, container, false);

                final EditText edtNombreCompañia = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_nombreCompañia);
                final EditText edtNombreContacto = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_nombreContacto);
                final EditText edtPuesto = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_puestoContacto);
                final EditText edtDireccion = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_direccion);
                final EditText edtCiudad = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_ciudad);
                final EditText edtRegion = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_region);
                final EditText edtCodPost = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_codPostal);
                final EditText edtPais = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_pais);
                final EditText edtTelefono = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_telefono);
                final EditText edtFax = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_fax);
                final EditText edtWeb = (EditText) viewAgregarNuevoProveedor.findViewById(R.id.edt_web);

                final Builder builderAgregarNuevoProveedor = new Builder(getActivity());
                builderAgregarNuevoProveedor.setTitle("Agregar un proveedor")
                .setView(viewAgregarNuevoProveedor)
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper myDbHelper = new DataBaseHelper(getActivity().getApplicationContext());
                        SQLiteDatabase db = myDbHelper.getWritableDatabase();
                        ContentValues valores = new ContentValues();

                        if (edtNombreCompañia.getText().toString().isEmpty()) {
                            android.support.v7.app.AlertDialog.Builder builderInsertProveedorErr = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builderInsertProveedorErr.setTitle("Error al insertar proveedor");
                            builderInsertProveedorErr.setMessage("Debe asignar Nombre Compañia");
                            builderInsertProveedorErr.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((ViewGroup)viewAgregarNuevoProveedor.getParent()).removeView(viewAgregarNuevoProveedor);
                                    builderAgregarNuevoProveedor.show();
                                    edtNombreCompañia.requestFocus();

                                }
                            });
                            builderInsertProveedorErr.show();
                        }

                        else{

                            valores.put("NombreCompania", edtNombreCompañia.getText().toString());
                            valores.put("NombreContacto", edtNombreContacto.getText().toString());
                            valores.put("CargoContacto", edtPuesto.getText().toString());
                            valores.put("Direccion", edtDireccion.getText().toString());
                            valores.put("Ciudad", edtCiudad.getText().toString());
                            valores.put("Region", edtRegion.getText().toString());
                            valores.put("CodigoPostal", edtCodPost.getText().toString());
                            valores.put("Pais", edtPais.getText().toString());
                            valores.put("Telefono", edtTelefono.getText().toString());
                            valores.put("Fax", edtFax.getText().toString());
                            valores.put("HomePage",edtWeb.getText().toString());

                            try {

                                if (db.insert("Proveedores", null, valores) != 0) {
                                    Builder builderInsertProveedorSuccess = new Builder(getActivity());
                                    builderInsertProveedorSuccess.setTitle("Nuevo proveedor")
                                    .setMessage("Se agregó el proveedor: " + edtNombreCompañia.getText().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onDestroyInsert();
                                        }
                                    })
                                    .show();
                                }
                                db.close();
                            } catch (SQLException e) {
                                Builder builderInsertProveedorErr = new Builder(getActivity());
                                builderInsertProveedorErr.setTitle("Error al insertar proveedor")
                                .setMessage(e.getMessage())
                                .setNegativeButton("Aceptar",null)
                                .show();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(R.drawable.proveedores)
                .show();
            }
        });


        myDBHelper = new DataBaseHelper(getActivity().getApplicationContext());

        try {
            myDBHelper.createDataBase();
        } catch (IOException e) {
            throw new Error("No se puede crear BD");
        }

        try {
            myDBHelper.openDatabase();
            Cursor cursor = myDBHelper.fetchAllProveedores();
            if(cursor != null){
                proveedoresListaAdapter = new ProveedoresListaAdapter(getActivity().getApplicationContext(),cursor);
                recyclerViewProveedores.setAdapter(proveedoresListaAdapter);
                recyclerViewProveedores.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
                        .getApplicationContext(), new OnItemClickListener()));

                myDBHelper.close();
            }
        } catch (SQLException e) {
            throw new Error("Fallo en BD: "+e.getMessage()+"\n"+e.getCause());

        }
        return viewRoot;
    }


    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener{
        public void onItemClick(View childView, int position){
            TextView textViewId = (TextView) childView.findViewById(R.id.idProveedor);
            proveedorId = textViewId.getText().toString();
            nombreCompañia = (TextView) childView.findViewById(R.id.textproveedorNombreCompañia);
            ((CallBacksProveedores) getActivity()).onItemSelected(proveedorId);
            Toast.makeText(getActivity(), "Ha seleccionado el proveedor: " + nombreCompañia.getText().toString()
                    +"\nCon Id: " + proveedorId, Toast.LENGTH_SHORT).show();
        }

    @Override
    public void onItemLongPress(View childView, int position){

        delete = (TextView) childView.findViewById(R.id.idProveedor);
        nombreCompañia = (TextView) childView.findViewById(R.id.textproveedorNombreCompañia);
        proveedorId=delete.getText().toString();
        nameCompañia=nombreCompañia.getText().toString();
        Builder dialog = new Builder(getActivity());
        dialog.setTitle("Eliminar Proveedor")
        .setMessage("¿Seguro que deseas eliminar al Proveedor: "+ nameCompañia+"?");
        dialog.setNegativeButton("Cancelar", null)
        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDBHelper = new DataBaseHelper(getActivity().getApplication());
                SQLiteDatabase db = myDBHelper.getWritableDatabase();
                String where = "_id" + "=?";
                String[] whereArgs = new String[]{proveedorId};

                db.delete("Proveedores", where, whereArgs);

                db.close();

                Toast.makeText(getActivity(), "Se borro el Proveedor: " + nameCompañia, Toast.LENGTH_SHORT).show();

                try {
                    myDBHelper.openDatabase();
                    Cursor nCursor = myDBHelper.fetchAllProveedores();
                    if (nCursor != null) {
                        proveedoresListaAdapter = new ProveedoresListaAdapter(getActivity().getApplicationContext(), nCursor);
                        recyclerViewProveedores.setAdapter(proveedoresListaAdapter);
                        recyclerViewProveedores.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new OnItemClickListener()));
                    }

                    if (ClientesActivity.mTwoPane) {
                        ((Refresh)getActivity()).refreshList();
                    }

                } catch (SQLException e) {
                    Builder builderDeleteProveedoresErr = new Builder(getActivity());
                    builderDeleteProveedoresErr.setTitle("Error al borrar el proveedor")
                    .setMessage(e.getMessage())
                    .setNegativeButton("Aceptar",null)
                    .show();
                }

            }
        });
        dialog.show();
    }
    }
    public void onDestroyInsert(){
        Intent intent = new Intent(getActivity(), ProveedoresActivity.class);
        getActivity().finish();
        startActivity(intent);
    }
}
