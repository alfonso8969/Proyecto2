package alfonso8969.com.proyecto2.activities.clientes;

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
import alfonso8969.com.proyecto2.fragments.clientes.ClientesListaFragment;
import alfonso8969.com.proyecto2.fragments.clientes.Clientes_ItemsFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

public class Clientes_itemsList extends AppCompatActivity {
    public ViewGroup containerEmp;
    public DataBaseHelper myDBHelper;
    public String clienteId;
    public Cursor myCursor;
    Context context;
    public String clienteID,nombreCompañia,nombreContacto,puestoContacto,direccion
            ,ciudad,region,codPos,pais, telefono, fax;
    ClientesListaFragment clientesListaFragment;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_items_list);

        Bundle intent = getIntent().getExtras();
        Bundle extras = getIntent().getExtras();
        clienteId = extras.getString("clienteId");
        if(savedInstanceState == null){

            Clientes_ItemsFragment fragment = new Clientes_ItemsFragment();
            fragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.clientes_list_item,fragment)
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

            myCursor= myDBHelper.fetchClienteById(clienteId);
            myDBHelper.close();
            if (myCursor != null) {
                clienteID=(myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                nombreCompañia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                nombreContacto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(3))));
                puestoContacto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                direccion = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(5))));
                ciudad = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(6))));
                region = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(7))));
                codPos = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(8))));
                pais = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(9))));
                telefono = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(10))));
                fax = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(11))));
            }


            Toast.makeText(getApplicationContext(),"Editando cliente: "+ nombreCompañia, Toast.LENGTH_SHORT).show();

            clientesListaFragment = new ClientesListaFragment();
            containerEmp= clientesListaFragment.containerCliente;
            final View viewEditarCliente= ClientesListaFragment.inflaterCliente.inflate(R.layout.insertar_cliente,containerEmp,false);

            final EditText edtClienteId     =(EditText)viewEditarCliente.findViewById(R.id.edt_clienteId);
            final EditText edtNombreCompañia=(EditText)viewEditarCliente.findViewById(R.id.edt_nombreCompañia);
            final EditText edtNombreContacto=(EditText)viewEditarCliente.findViewById(R.id.edt_nombreContacto);
            final EditText edtPuesto        =(EditText)viewEditarCliente.findViewById(R.id.edt_puestoContacto);
            final EditText edtDireccion     =(EditText)viewEditarCliente.findViewById(R.id.edt_direccion);
            final EditText edtCiudad        =(EditText)viewEditarCliente.findViewById(R.id.edt_ciudad);
            final EditText edtRegion        =(EditText)viewEditarCliente.findViewById(R.id.edt_region);
            final EditText edtCodPost       =(EditText)viewEditarCliente.findViewById(R.id.edt_codPostal);
            final EditText edtPais          =(EditText)viewEditarCliente.findViewById(R.id.edt_pais);
            final EditText edtTelefono      =(EditText)viewEditarCliente.findViewById(R.id.edt_telefono);
            final EditText edtFax           =(EditText)viewEditarCliente.findViewById(R.id.edt_fax);


            edtClienteId     .setText(clienteID);
            edtNombreCompañia.setText(nombreCompañia);
            edtNombreContacto.setText(nombreContacto);
            edtPuesto        .setText(puestoContacto);
            edtDireccion     .setText(direccion);
            edtCiudad        .setText(ciudad);
            edtRegion        .setText(region);
            edtCodPost       .setText(codPos);
            edtPais          .setText(pais);
            edtTelefono      .setText(telefono);
            edtFax           .setText(fax);

            final Builder builderEditarCliente = new Builder(this);
            builderEditarCliente.setView(viewEditarCliente)
            .setTitle("Editar cliente")
            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
                    myDBHelper.openDatabase();
                    db = myDBHelper.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    if (edtNombreCompañia.getText().toString().isEmpty() || edtClienteId.getText().toString().isEmpty()) {
                        String message = null;
                        if (edtNombreCompañia.getText().toString().isEmpty() && edtClienteId.getText().toString().isEmpty())
                            message="Debe asignar  Nombre Compañia y ClienteID";
                        else if (edtClienteId.getText().toString().isEmpty())
                            message="Debe asignar ClienteId";
                        else if(edtNombreCompañia.getText().toString().isEmpty() )
                            message="Debe asignar  Nombre Compañia";
                        Builder builderEditClienteErr = new Builder(context);
                        builderEditClienteErr.setTitle("Error al actualizar Cliente")
                        .setMessage(message)
                        .setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ViewGroup)viewEditarCliente.getParent()).removeView(viewEditarCliente);
                                builderEditarCliente.show();
                                if(edtNombreCompañia.getText().toString().isEmpty())
                                edtNombreCompañia.requestFocus();
                                else if(edtClienteId.getText().toString().isEmpty())
                                edtClienteId.requestFocus();
                                else
                                edtClienteId.requestFocus();
                            }
                        })
                        .show();
                    }
                    else{
                    valores.put("ClienteId",edtClienteId.getText().toString().toUpperCase());
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
                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{clienteId};

                    try {
                        if (db.update("Clientes",valores,where, whereArgs)!=0) {
                            Builder builderUpdateEmpleadoSuccess = new Builder(context);
                            builderUpdateEmpleadoSuccess.setTitle("Editar cliente")
                            .setMessage("Se actualizó el cliente: " + nombreCompañia)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(context,ClientesActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            })
                            .show();
                        }
                        db.close();
                        myDBHelper.close();
                    } catch (SQLException e) {
                        Builder builderUpdateEmpleadoErr = new Builder(context);
                        builderUpdateEmpleadoErr.setTitle("Error al editar cliente")
                        .setMessage(e.getMessage())
                        .setNegativeButton("Aceptar",null)
                        .show();
                        }
                    }
                }
            })
            .setNegativeButton("Cancelar",null )
            .show();
        }

        if (item.getItemId() == R.id.action_delete) {
            context=this;
            myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
            myDBHelper.openDatabase();
            db=myDBHelper.getWritableDatabase();
            myCursor= myDBHelper.fetchClienteById(clienteId);
            if (myCursor != null) {

                nombreCompañia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
            }

            Builder dialog = new Builder(this);
            dialog.setTitle("Eliminar Cliente")
            .setMessage("¿Seguro que deseas eliminar al Cliente: " + nombreCompañia + "?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{clienteId};


                    db.delete("Clientes", where, whereArgs);

                    db.execSQL("DELETE FROM Clientes WHERE _id=" + clienteId);

                    db.close();
                    myDBHelper.close();
                    Toast.makeText(getApplicationContext(), "Se borro el empleado: " + nombreCompañia, Toast.LENGTH_SHORT).show();

                    try {
                        myDBHelper.openDatabase();
                        Cursor nCursor = myDBHelper.fetchAllClientes();
                        myDBHelper.close();
                        if (nCursor != null) {
                            Intent i = new Intent(context,ClientesActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }


                        if (ClientesActivity.mTwoPane) {
                            ((ClientesListaFragment.Refresh) getApplication()).refreshList();
                        }

                    } catch (SQLException e) {
                        Builder builderInsertEmpErr = new Builder(context);
                        builderInsertEmpErr.setTitle("Error al borrar cliente")
                        .setMessage(e.getMessage())
                        .setNegativeButton("Aceptar", null)
                        .show();
                    }
                }
            });

            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }
}
