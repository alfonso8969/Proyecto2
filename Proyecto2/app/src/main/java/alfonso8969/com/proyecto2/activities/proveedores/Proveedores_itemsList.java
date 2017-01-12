package alfonso8969.com.proyecto2.activities.proveedores;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.proveedores.ProveedoresListaFragment;
import alfonso8969.com.proyecto2.fragments.proveedores.Proveedores_ItemsFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

public class Proveedores_itemsList extends AppCompatActivity {
    public ViewGroup containerProveedor;
    public DataBaseHelper myDBHelper;
    public String proveedorId;
    public Cursor myCursor;
    Context context;
    public String nombreCompañia, nombreContacto, puestoContacto, direccion, ciudad, region, codPos, pais, telefono, fax, web;
    ProveedoresListaFragment proveedoresListaFragment;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores_items_list);

        Bundle intent = getIntent().getExtras();
        Bundle extras = getIntent().getExtras();
        proveedorId = extras.getString("proveedorId");
        if (savedInstanceState == null) {

            Proveedores_ItemsFragment fragment = new Proveedores_ItemsFragment();
            fragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.proveedores_list_item, fragment)
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

            myCursor= myDBHelper.fetchProveedorById(proveedorId);
            myDBHelper.close();
            if (myCursor != null) {

                nombreCompañia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                nombreContacto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                puestoContacto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(3))));
                direccion = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                ciudad = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(5))));
                region = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(6))));
                codPos = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(7))));
                pais = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(8))));
                telefono = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(9))));
                fax = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(10))));
                web =(myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(11))));


            }
            final android.support.v7.app.AlertDialog.Builder builderEditarProveedor = new android.support.v7.app.AlertDialog.Builder(this);
            builderEditarProveedor.setTitle("Editar proveedor");
            Toast.makeText(getApplicationContext(),"Editando proveedor: "+ nombreCompañia, Toast.LENGTH_SHORT).show();


            proveedoresListaFragment = new ProveedoresListaFragment();
            containerProveedor= proveedoresListaFragment.containerProveedor;
            final View viewEditarProveedor= ProveedoresListaFragment.inflaterProveedor.inflate(R.layout.insertar_proveedor,containerProveedor,false);

            builderEditarProveedor.setView(viewEditarProveedor);

            final EditText edtNombreCompañia=(EditText)viewEditarProveedor.findViewById(R.id.edt_nombreCompañia);
            final EditText edtNombreContacto=(EditText)viewEditarProveedor.findViewById(R.id.edt_nombreContacto);
            final EditText edtPuesto        =(EditText)viewEditarProveedor.findViewById(R.id.edt_puestoContacto);
            final EditText edtDireccion     =(EditText)viewEditarProveedor.findViewById(R.id.edt_direccion);
            final EditText edtCiudad        =(EditText)viewEditarProveedor.findViewById(R.id.edt_ciudad);
            final EditText edtRegion        =(EditText)viewEditarProveedor.findViewById(R.id.edt_region);
            final EditText edtCodPost       =(EditText)viewEditarProveedor.findViewById(R.id.edt_codPostal);
            final EditText edtPais          =(EditText)viewEditarProveedor.findViewById(R.id.edt_pais);
            final EditText edtTelefono      =(EditText)viewEditarProveedor.findViewById(R.id.edt_telefono);
            final EditText edtFax           =(EditText)viewEditarProveedor.findViewById(R.id.edt_fax);
            final EditText edtWeb           =(EditText)viewEditarProveedor.findViewById(R.id.edt_web);



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
            edtWeb           .setText(web);


            builderEditarProveedor.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
                    myDBHelper.openDatabase();
                    db = myDBHelper.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    if (edtNombreCompañia.getText().toString().isEmpty()) {
                        android.support.v7.app.AlertDialog.Builder builderEditProveedorErr = new android.support.v7.app.AlertDialog.Builder(context);
                        builderEditProveedorErr.setTitle("Error al actualizar Proveedor");
                        builderEditProveedorErr.setMessage("Debe asignar Nombre Compañia");
                        builderEditProveedorErr.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((ViewGroup)viewEditarProveedor.getParent()).removeView(viewEditarProveedor);
                                builderEditarProveedor.show();
                                edtNombreCompañia.requestFocus();

                            }
                        });
                        builderEditProveedorErr.show();
                    }else{
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
                    valores.put("HomePage",edtWeb.getText().toString().toUpperCase());
                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{proveedorId};

                    try {

                        if ( db.update("Proveedores", valores,where, whereArgs) != 0) {
                            AlertDialog.Builder builderInsertEmpSuccess = new AlertDialog.Builder(context);
                            builderInsertEmpSuccess.setTitle("Editar proveedor");
                            builderInsertEmpSuccess.setMessage("Se actualizó el proveedor: " + nombreCompañia);
                            builderInsertEmpSuccess.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(context,ProveedoresActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            });
                            builderInsertEmpSuccess.show();
                        }
                        db.close();
                        myDBHelper.close();
                    } catch (SQLException e) {
                        AlertDialog.Builder builderInsertProveedorErr = new AlertDialog.Builder(context);
                        builderInsertProveedorErr.setTitle("Error al editar proveedor");
                        builderInsertProveedorErr.setMessage(e.getMessage());
                        builderInsertProveedorErr.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builderInsertProveedorErr.show();
                        }
                    }
                }
            })
                    .setNegativeButton("Cancelar",null );

            builderEditarProveedor.show();

        }
        if (item.getItemId() == R.id.action_delete) {
            context=this;
            myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
            myDBHelper.openDatabase();
            db=myDBHelper.getWritableDatabase();
            myCursor= myDBHelper.fetchProveedorById(proveedorId);
            if (myCursor != null) {

                nombreCompañia = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));


            }

            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
            dialog.setTitle("Eliminar Cliente");
            dialog.setMessage("¿Seguro que deseas eliminar al Proveedor: " + nombreCompañia + "?");
            dialog.setNegativeButton("Cancelar", null);
            dialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{proveedorId};


                    db.delete("Proveedores", where, whereArgs);

                    db.execSQL("DELETE FROM Proveedores WHERE _id=" + proveedorId);

                    db.close();
                    myDBHelper.close();
                    Toast.makeText(getApplicationContext(), "Se borro el proveedor: " + nombreCompañia, Toast.LENGTH_SHORT).show();

                    try {
                        myDBHelper.openDatabase();
                        Cursor nCursor = myDBHelper.fetchAllProveedores();
                        myDBHelper.close();
                        if (nCursor != null) {
                            Intent i = new Intent(context,ProveedoresActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }


                        if (ProveedoresActivity.mTwoPane) {
                            ((ProveedoresListaFragment.Refresh) getApplication()).refreshList();
                        }

                    } catch (SQLException e) {
                        AlertDialog.Builder builderInsertProveedorErr = new AlertDialog.Builder(context);
                        builderInsertProveedorErr.setTitle("Error al borrar proveedor");
                        builderInsertProveedorErr.setMessage(e.getMessage());
                        builderInsertProveedorErr.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builderInsertProveedorErr.show();
                    }

                }
            });

            dialog.show();

        }
        return super.onOptionsItemSelected(item);
    }
}