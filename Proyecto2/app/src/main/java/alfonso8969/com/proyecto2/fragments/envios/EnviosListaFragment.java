package alfonso8969.com.proyecto2.fragments.envios;

import android.support.v7.app.AlertDialog.Builder;
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

import java.io.IOException;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.clientes.ClientesActivity;
import alfonso8969.com.proyecto2.adapters.EnviosListaAdapter;
import alfonso8969.com.proyecto2.clases.RecyclerItemClickListener;
import alfonso8969.com.proyecto2.fragments.clientes.ClientesListaFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 15/11/2016.
 */

public class EnviosListaFragment extends Fragment {

    RecyclerView recyclerViewEnvios;
    public static final String TAG="RecyclerViewEnviosFragment";
    EnviosListaAdapter enviosListaAdapter;
    DataBaseHelper myDBHelper;
    TextView delete;
    TextView nombreCompañia;
    String envioId;
    FloatingActionButton fb;
    public ViewGroup containerEnvio;
    public static LayoutInflater inflaterEnvio;

    public interface CallBacksEnvios{
        void onItemSelected(String envioId);
    }
    public  interface Refresh{
        void refreshList();
    }
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        final View viewRoot = inflater.inflate(R.layout.lista_envios_fragment, container, false);
        viewRoot.setTag(TAG);
        containerEnvio=container;
        inflaterEnvio=inflater;
        recyclerViewEnvios = (RecyclerView)viewRoot.findViewById(R.id.lista_Envios);
        recyclerViewEnvios.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        fb = (FloatingActionButton)viewRoot.findViewById(R.id.fbEnvios);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               onCreateView(inflater, container, saveInstanceState);
               final View viewAgregarNuevaCompañiaEnvio = inflater.inflate(R.layout.insertar_companiaenvios, container, false);
               final EditText edtNombreCompañia = (EditText) viewAgregarNuevaCompañiaEnvio.findViewById(R.id.edt_nombreCompañiaEnvios);
               final EditText edtTelefono = (EditText) viewAgregarNuevaCompañiaEnvio.findViewById(R.id.edt_telefonoEnvios);

                final Builder builderAgregarNuevaCompañiaEnvio = new Builder(getActivity());
                builderAgregarNuevaCompañiaEnvio.setView(viewAgregarNuevaCompañiaEnvio)
                .setTitle("Agregar Compañia Envios")
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBaseHelper myDbHelper = new DataBaseHelper(getActivity().getApplicationContext());
                        SQLiteDatabase db = myDbHelper.getWritableDatabase();
                        ContentValues valores = new ContentValues();

                        if (edtNombreCompañia.getText().toString().isEmpty()) {
                            Builder builderInsertClienteErr = new Builder(getActivity());
                            builderInsertClienteErr.setTitle("Error al insertar Compañia Envios")
                            .setMessage("Debe asignar Nombre Compañia")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ((ViewGroup)viewAgregarNuevaCompañiaEnvio.getParent()).removeView(viewAgregarNuevaCompañiaEnvio);
                                    builderAgregarNuevaCompañiaEnvio.show();
                                    edtNombreCompañia.requestFocus();
                                }
                            })
                            .show();
                        }

                        else{
                            valores.put("NombreCompania", edtNombreCompañia.getText().toString());
                            valores.put("Telefono", edtTelefono.getText().toString());

                            db.insert("Envios", null, valores);
                            db.close();

                            try {
                                myDBHelper.openDatabase();
                                Cursor nCursor = myDBHelper.fetchAllEnvios();
                                myDBHelper.close();
                                if (nCursor != null) {
                                    Builder builderInsertCompañiaEnviosSuccess = new Builder(getActivity());
                                    builderInsertCompañiaEnviosSuccess.setTitle("Nueva Compañia Envios")
                                    .setMessage("Se agregó la Compañia: " + edtNombreCompañia.getText().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onDestroyInsert();
                                        }
                                    })
                                    .show();
                                }

                            } catch (SQLException e) {
                                Builder builderInsertCompañiaEnviosErr = new Builder(getActivity());
                                builderInsertCompañiaEnviosErr.setTitle("Error al insertar Compañia Envios")
                                .setMessage(e.getMessage())
                                .setNegativeButton("Aceptar", null)
                                .show();
                            }
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
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
            Cursor cursor = myDBHelper.fetchAllEnvios();
            if(cursor != null){
                enviosListaAdapter = new EnviosListaAdapter(getActivity().getApplicationContext(),cursor);
                recyclerViewEnvios.setAdapter(enviosListaAdapter);
                recyclerViewEnvios.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
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
            TextView textViewId = (TextView) childView.findViewById(R.id.idEnvio);
            envioId=textViewId.getText().toString();
            nombreCompañia = (TextView) childView.findViewById(R.id.textEnvioNombreCompañia);
            ((CallBacksEnvios) getActivity()).onItemSelected(envioId);
            Toast.makeText(getActivity(), "Ha seleccionado la compañia de envios: " + nombreCompañia.getText().toString()
                    +"\nCon Id: " + envioId, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onItemLongPress(View childView, int position){

            delete = (TextView) childView.findViewById(R.id.idEnvio);
            nombreCompañia = (TextView) childView.findViewById(R.id.textEnvioNombreCompañia);
            envioId=delete.getText().toString();

            Builder dialog = new Builder(getActivity());
            dialog.setTitle("Eliminar Compañia de envios")
            .setMessage("¿Seguro que deseas eliminar la compañia de envios: "+ nombreCompañia.getText().toString()+"?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDBHelper = new DataBaseHelper(getActivity().getApplication());
                    SQLiteDatabase db = myDBHelper.getWritableDatabase();
                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{envioId};

                    db.delete("Envios", where, whereArgs);

                    db.execSQL("DELETE FROM Envios WHERE _id=" + envioId);

                    db.close();

                    Toast.makeText(getActivity(), "Se borro la compañia de envios: " + nombreCompañia, Toast.LENGTH_SHORT).show();

                    try {
                        myDBHelper.openDatabase();
                        Cursor nCursor = myDBHelper.fetchAllEnvios();
                        myDBHelper.close();
                        if (nCursor != null) {
                            enviosListaAdapter = new EnviosListaAdapter(getActivity().getApplicationContext(), nCursor);
                            recyclerViewEnvios.setAdapter(enviosListaAdapter);
                            recyclerViewEnvios.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new OnItemClickListener()));
                        }


                        if (ClientesActivity.mTwoPane) {
                            ((ClientesListaFragment.Refresh)getActivity()).refreshList();
                        }

                    } catch (SQLException e) {
                        Builder builderInsertEmpErr = new Builder(getActivity());
                        builderInsertEmpErr.setTitle("Error al borrar el cliente")
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

