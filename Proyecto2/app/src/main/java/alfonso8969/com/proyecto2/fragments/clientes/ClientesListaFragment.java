package alfonso8969.com.proyecto2.fragments.clientes;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog.Builder;
import java.io.IOException;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.clientes.ClientesActivity;
import alfonso8969.com.proyecto2.adapters.ClientesListaAdapter;
import alfonso8969.com.proyecto2.clases.RecyclerItemClickListener;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 07/11/2016.
 */

public class ClientesListaFragment extends Fragment {
    RecyclerView recyclerViewClientes;
    public static final String TAG="RecyclerViewClientesFragment";
    ClientesListaAdapter clientesListaAdapter;
    DataBaseHelper myDBHelper;
    TextView delete;
    TextView nombreCompañia;
    String clienteId;
    String nombreCliente;
    FloatingActionButton fb;
    public  ViewGroup containerCliente;
    public static LayoutInflater inflaterCliente;

    public interface CallBacksClientes{
        void onItemSelected(String clienteId);
    }
    public  interface Refresh{
        void refreshList();
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        final View viewRoot = inflater.inflate(R.layout.lista_clientes_fragment, container, false);
        viewRoot.setTag(TAG);
        containerCliente=container;
        inflaterCliente=inflater;
        recyclerViewClientes = (RecyclerView)viewRoot.findViewById(R.id.lista_Clientes);
        recyclerViewClientes.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        fb = (FloatingActionButton)viewRoot.findViewById(R.id.fbClientes);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCreateView(inflater, container, saveInstanceState);
                final View viewAgregarNuevoCliente = inflater.inflate(R.layout.insertar_cliente, container, false);
                final EditText edtClienteID = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_clienteId);
                final EditText edtNombreCompañia = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_nombreCompañia);
                final EditText edtNombreContacto = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_nombreContacto);
                final EditText edtPuesto = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_puestoContacto);
                final EditText edtDireccion = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_direccion);
                final EditText edtCiudad = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_ciudad);
                final EditText edtRegion = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_region);
                final EditText edtCodPost = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_codPostal);
                final EditText edtPais = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_pais);
                final EditText edtTelefono = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_telefono);
                final EditText edtFax = (EditText) viewAgregarNuevoCliente.findViewById(R.id.edt_fax);

                final Builder builderAgregarNuevoCliente = new Builder(getActivity());
                builderAgregarNuevoCliente.setTitle("Agregar un cliente");
                builderAgregarNuevoCliente.setView(viewAgregarNuevoCliente);
                builderAgregarNuevoCliente.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper myDbHelper = new DataBaseHelper(getActivity().getApplicationContext());
                        SQLiteDatabase db = myDbHelper.getWritableDatabase();
                        ContentValues valores = new ContentValues();

                            if (edtNombreCompañia.getText().toString().isEmpty() || edtClienteID.getText().toString().isEmpty()) {
                                String message = null;
                                if (edtNombreCompañia.getText().toString().isEmpty() && edtClienteID.getText().toString().isEmpty())
                                    message="Debe asignar  Nombre Compañia y ClienteID";
                                else if (edtClienteID.getText().toString().isEmpty())
                                    message="Debe asignar ClienteId";
                                else if(edtNombreCompañia.getText().toString().isEmpty() )
                                    message="Debe asignar  Nombre Compañia";

                                Builder builderInsertClienteErr = new Builder(getActivity());
                                builderInsertClienteErr.setTitle("Error al insertar Cliente")
                                .setMessage(message)
                                .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((ViewGroup)viewAgregarNuevoCliente.getParent()).removeView(viewAgregarNuevoCliente);
                                        builderAgregarNuevoCliente.show();
                                        if (edtNombreCompañia.getText().toString().isEmpty() && edtClienteID.getText().toString().isEmpty())
                                            edtClienteID.requestFocus();
                                        else if(edtClienteID.getText().toString().isEmpty())
                                            edtClienteID.requestFocus();
                                        else if(edtNombreCompañia.getText().toString().isEmpty() )
                                            edtNombreCompañia.requestFocus();
                                    }
                                })
                                .show();
                            }

                          else{
                            valores.put("ClienteId",edtClienteID.getText().toString().toUpperCase());
                            valores.put("NombreCompania", edtNombreCompañia.getText().toString());
                            valores.put("NombreContacto", edtNombreContacto.getText().toString());
                            valores.put("TratoContacto", edtPuesto.getText().toString());
                            valores.put("Direccion", edtDireccion.getText().toString());
                            valores.put("Ciudad", edtCiudad.getText().toString());
                            valores.put("Region", edtRegion.getText().toString());
                            valores.put("CodigoPostal", edtCodPost.getText().toString());
                            valores.put("Pais", edtPais.getText().toString());
                            valores.put("Telefono", edtTelefono.getText().toString());
                            valores.put("Fax", edtFax.getText().toString());

                            db.insert("Clientes", null, valores);
                            db.close();

                            try {
                                myDBHelper.openDatabase();
                                Cursor nCursor = myDBHelper.fetchAllEmpleados();
                                myDBHelper.close();
                                if (nCursor != null) {
                                    Builder builderInsertClienteSuccess = new Builder(getActivity());
                                    builderInsertClienteSuccess.setTitle("Nuevo cliente")
                                    .setMessage("Se agregó el cliente: " + edtNombreCompañia.getText().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onDestroyInsert();
                                        }
                                    })
                                    .show();
                                }

                            } catch (SQLException e) {
                                Builder builderInsertClienteErr = new Builder(getActivity());
                                builderInsertClienteErr.setTitle("Error al insertar cliente")
                                .setMessage(e.getMessage())
                                .setNegativeButton("Aceptar", null)
                                .show();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancelar",null)
                .setIcon(R.drawable.clientes)
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
            Cursor cursor = myDBHelper.fetchAllClientes();
            if(cursor != null){
                clientesListaAdapter = new ClientesListaAdapter(getActivity().getApplicationContext(),cursor);
                recyclerViewClientes.setAdapter(clientesListaAdapter);
                recyclerViewClientes.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
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
            TextView textViewId = (TextView) childView.findViewById(R.id.idCliente);
            clienteId=textViewId.getText().toString();
            nombreCompañia = (TextView) childView.findViewById(R.id.textclienteNombreCompañia);
            ((CallBacksClientes) getActivity()).onItemSelected(clienteId);
            Toast.makeText(getActivity(), "Ha seleccionado el cliente: " + nombreCompañia.getText().toString()
                    +"\nCon Id: " + clienteId, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onItemLongPress(View childView, int position){

            delete = (TextView) childView.findViewById(R.id.idCliente);
            nombreCompañia = (TextView) childView.findViewById(R.id.textclienteNombreCompañia);
            clienteId=delete.getText().toString();
            nombreCliente=nombreCompañia.getText().toString();
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Eliminar Empleado");
            dialog.setMessage("¿Seguro que deseas eliminar al Cliente: "+ nombreCliente+"?");
            dialog.setNegativeButton("Cancelar", null);
            dialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDBHelper = new DataBaseHelper(getActivity().getApplication());
                    SQLiteDatabase db = myDBHelper.getWritableDatabase();
                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{clienteId};

                    db.delete("Clientes", where, whereArgs);

                    db.close();

                    Toast.makeText(getActivity(), "Se borro el cliente: " + nombreCliente, Toast.LENGTH_SHORT).show();

                    try {
                        myDBHelper.openDatabase();
                        Cursor nCursor = myDBHelper.fetchAllClientes();
                        if (nCursor != null) {
                            clientesListaAdapter = new ClientesListaAdapter(getActivity().getApplicationContext(), nCursor);
                            recyclerViewClientes.setAdapter(clientesListaAdapter);
                            recyclerViewClientes.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new OnItemClickListener()));
                        }


                        if (ClientesActivity.mTwoPane) {
                            ((Refresh)getActivity()).refreshList();
                        }

                    } catch (SQLException e) {
                        Builder builderOpenDatabaseErr = new Builder(getActivity());
                        builderOpenDatabaseErr .setTitle("Error al abrir base de datos")
                                .setMessage(e.getMessage())
                                .setNegativeButton("Aceptar", null)
                                .show();
                    }

                }
            })
            .show();
        }
    }
    public void onDestroyInsert(){
        Intent intent = new Intent(getActivity(), ClientesActivity.class);
        getActivity().finish();
        startActivity(intent);
    }
}