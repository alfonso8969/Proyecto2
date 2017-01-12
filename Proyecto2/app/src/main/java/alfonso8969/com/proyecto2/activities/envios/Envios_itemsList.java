package alfonso8969.com.proyecto2.activities.envios;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.clientes.ClientesActivity;
import alfonso8969.com.proyecto2.fragments.clientes.ClientesListaFragment;
import alfonso8969.com.proyecto2.fragments.envios.EnviosListaFragment;
import alfonso8969.com.proyecto2.fragments.envios.Envios_ItemsFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

public class Envios_itemsList extends AppCompatActivity {
    public ViewGroup containerEnvio;
    public DataBaseHelper myDBHelper;
    public Cursor myCursor;
    Context context;
    public String envioId,nombreCompañia,telefono;
    EnviosListaFragment enviosListaFragment;
    SQLiteDatabase db;
    EditText edtNombreCompañia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envios_items_list);

        Bundle intent = getIntent().getExtras();
        Bundle extras = getIntent().getExtras();
        envioId = extras.getString("envioId");
        if(savedInstanceState == null){

            Envios_ItemsFragment fragment = new Envios_ItemsFragment();
            fragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.envios_list_item,fragment)
                    .commit();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detaileditdel, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            context = this;

            myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
            myDBHelper.openDatabase();

            myCursor= myDBHelper.fetchEnvioById(envioId);
            myDBHelper.close();
            if (myCursor != null) {
                nombreCompañia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                telefono = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
            }
            final Builder builderEditarCompañiaEnvio = new Builder(this);
            builderEditarCompañiaEnvio.setTitle("Editar Compañia Envios");
            Toast.makeText(getApplicationContext(),"Editando Compañia Envios: "+ nombreCompañia, Toast.LENGTH_SHORT).show();


            enviosListaFragment = new EnviosListaFragment();
            containerEnvio= enviosListaFragment.containerEnvio;
            final View viewEditarCompañiaEnvio= EnviosListaFragment.inflaterEnvio.inflate(R.layout.insertar_companiaenvios,containerEnvio,false);

            builderEditarCompañiaEnvio.setView(viewEditarCompañiaEnvio);
            edtNombreCompañia = (EditText)viewEditarCompañiaEnvio.findViewById(R.id.edt_nombreCompañiaEnvios);
            final EditText edtTelefono = (EditText)viewEditarCompañiaEnvio.findViewById(R.id.edt_telefonoEnvios);

            edtNombreCompañia.setText(nombreCompañia);
            edtTelefono.setText(telefono);

            builderEditarCompañiaEnvio.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    myDBHelper = new DataBaseHelper(getApplication().getApplicationContext());
                    myDBHelper.openDatabase();
                    db = myDBHelper.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    nombreCompañia = edtNombreCompañia.getText().toString();
                    if (nombreCompañia.isEmpty()) {
                        android.support.v7.app.AlertDialog.Builder builderEditCompañiaEnviosErr = new android.support.v7.app.AlertDialog.Builder(context);
                        builderEditCompañiaEnviosErr.setTitle("Error al actualizar Compañia Envio")
                        .setMessage("Debe asignar Nombre Compañia")
                        .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ViewGroup) viewEditarCompañiaEnvio.getParent()).removeView(viewEditarCompañiaEnvio);
                                builderEditarCompañiaEnvio.show();
                                edtNombreCompañia.requestFocus();
                            }
                        })
                        .show();
                    } else{

                    valores.put("NombreCompania", edtNombreCompañia.getText().toString());
                    valores.put("Telefono", edtTelefono.getText().toString());
                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{envioId};

                    try {
                        if (db.update("Envios", valores, where, whereArgs) != 0) {
                            Builder builderUpdateCompañiaEnviosSuccess = new Builder(context);
                            builderUpdateCompañiaEnviosSuccess.setTitle("Editar Compañia Envios")
                            .setMessage("Se actualizó la compañia: " + nombreCompañia)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(context, EnviosActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            })
                            .show();
                        }
                        db.close();
                        myDBHelper.close();
                    } catch (SQLException e) {
                        Builder builderUpdateCompañiaEnviosErr = new Builder(context);
                        builderUpdateCompañiaEnviosErr.setTitle("Error al editar la compañia de envios")
                        .setMessage(e.getMessage())
                        .setNegativeButton("Aceptar", null)
                        .show();
                    }
                  }
                }
            })
                    .setNegativeButton("Cancelar",null);

            builderEditarCompañiaEnvio.show();
        }

        if (item.getItemId() == R.id.action_delete) {
            context=this;
            myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
            myDBHelper.openDatabase();
            db=myDBHelper.getWritableDatabase();
            myCursor= myDBHelper.fetchEnvioById(envioId);
            if (myCursor != null) {

                nombreCompañia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
            }

            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
            dialog.setTitle("Eliminar Compañia Envios")
            .setMessage("¿Seguro que deseas eliminar la Compañia Envios: " + nombreCompañia + "?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{envioId};

                    db.delete("Clientes", where, whereArgs);

                    db.close();
                    myDBHelper.close();
                    Toast.makeText(getApplicationContext(), "Se borro la Compañia Envios: " + nombreCompañia, Toast.LENGTH_SHORT).show();

                    try {
                        myDBHelper.openDatabase();
                        Cursor nCursor = myDBHelper.fetchAllEnvios();
                        myDBHelper.close();
                        if (nCursor != null) {
                            Intent i = new Intent(context,EnviosActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }


                        if (ClientesActivity.mTwoPane) {
                            ((ClientesListaFragment.Refresh) getApplication()).refreshList();
                        }

                    } catch (SQLException e) {
                        Builder builderDeleteCompañiaEnviosErr = new Builder(context);
                        builderDeleteCompañiaEnviosErr.setTitle("Error al borrar cliente")
                        .setMessage(e.getMessage())
                        .setNegativeButton("Aceptar", null)
                        .show();
                    }

                }
            })
            .show();

        }
        return super.onOptionsItemSelected(item);
    }
}

