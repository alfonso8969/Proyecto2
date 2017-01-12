package alfonso8969.com.proyecto2.activities.productos;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog.Builder;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.productos.ProductosListaFragment;
import alfonso8969.com.proyecto2.fragments.productos.Productos_ItemsFragment;
import alfonso8969.com.proyecto2.fragments.proveedores.ProveedoresListaFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

public class Productos_ItemsList extends AppCompatActivity {

    public ViewGroup containerProducto;
    public DataBaseHelper myDBHelper;
    public Cursor myCursor;
    Context context;
    public String productoId,strNombreProducto,strInterrumpido,avatarUri,strCantidadUnidad,idNombreProveedor,idNombreCategoria,categoriaId,proveedorId;
    public int intProveedorId,intCategoriaId,intUnidadStock,intUnidadPedido,intNivelReaprovisionamiento,intInterrumpido;
    public double precioUnidad;

    ProductosListaFragment productosListaFragment;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_itemslist);

        Bundle intent = getIntent().getExtras();
        Bundle extras = getIntent().getExtras();
        productoId = extras.getString("productoId");
        if(savedInstanceState == null){

            Productos_ItemsFragment fragment = new Productos_ItemsFragment();
            fragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.productos_list_item,fragment)
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_edit) {
            context = this;
            myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
            myDBHelper.openDatabase();
            myCursor= myDBHelper.fetchProductoById(productoId);
            myDBHelper.close();

            if(myCursor!=null){

                strNombreProducto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                intProveedorId = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                intCategoriaId = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(3))));
                strCantidadUnidad = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                precioUnidad= (myCursor.getDouble(myCursor.getColumnIndex(myCursor.getColumnName(5))));
                intUnidadStock = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(6))));
                intUnidadPedido = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(7))));
                intNivelReaprovisionamiento = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(8))));
                intInterrumpido = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(9))));
                strInterrumpido=(intInterrumpido==1) ? "Si": "No";
                avatarUri = myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(10)));
            }


            Toast.makeText(getApplicationContext(),"Editando producto: "+strNombreProducto, Toast.LENGTH_SHORT).show();

            productosListaFragment = new ProductosListaFragment();
            containerProducto= productosListaFragment.containerProducto;
            final View viewEditarProducto= ProductosListaFragment.inflaterProducto.inflate(R.layout.insertar_producto,containerProducto,false);


            final EditText edtImagen                        =(EditText)viewEditarProducto.findViewById(R.id.edt_imagenProducto) ;
            final EditText edtNombreProducto                =(EditText)viewEditarProducto.findViewById(R.id.edt_nombreProducto);
            final EditText edtCantidadPorUnidad             =(EditText)viewEditarProducto.findViewById(R.id.edt_CantidadPorUnidad);
            final EditText edtPrecioPorUnidad               =(EditText)viewEditarProducto.findViewById(R.id.edt_PrecioPorUnidad);
            final EditText edtUnidadesEnStock               =(EditText)viewEditarProducto.findViewById(R.id.edt_UnidadesEnStock);
            final EditText edtUnidadesEnPedido              =(EditText)viewEditarProducto.findViewById(R.id.edt_UnidadesEnPedido);
            final EditText edtNivelDeReaprovisionamiento    =(EditText)viewEditarProducto.findViewById(R.id.edt_NivelDeReaprovisionamiento);
            final EditText edtInterrumpido                  =(EditText)viewEditarProducto.findViewById(R.id.edt_Interrumpido);
            final Spinner  spiNombreProveedor                =(Spinner)viewEditarProducto.findViewById(R.id.spi_nombreProveedor);
            final Spinner  spiNombreCategoria                =(Spinner)viewEditarProducto.findViewById(R.id.spi_nombreCategoria);

            myDBHelper.openDatabase();
            Cursor cursorNombreProveedor = myDBHelper.fetchAllNombresProveedores();
            Cursor cursorNombreCategoria = myDBHelper.fetchAllNombresCategorias();
            myDBHelper.close();

            SimpleCursorAdapter adapterNombreProveedor = new SimpleCursorAdapter(getApplicationContext(),
                    android.R.layout.select_dialog_item,cursorNombreProveedor,new String[] {"NombreCompania"},
                    new int[] {android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            adapterNombreProveedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiNombreProveedor.setAdapter(adapterNombreProveedor);
            spiNombreProveedor.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);


            SimpleCursorAdapter adapterNombreCategoria = new SimpleCursorAdapter(getApplicationContext(),
                    android.R.layout.select_dialog_item,cursorNombreCategoria,new String[]{"NombreCategoria"},
                    new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            adapterNombreCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiNombreCategoria.setAdapter(adapterNombreCategoria);
            spiNombreCategoria.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);

            spiNombreProveedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                    if (parent.getId() == R.id.spi_nombreProveedor) {
                        TextView tvNombreProveedor = (TextView)spiNombreProveedor.getSelectedView();
                        idNombreProveedor = tvNombreProveedor.getText().toString();
                        myDBHelper.openDatabase();
                        Cursor idProveedorCursor= myDBHelper.fetch_idByNombresProveedor(idNombreProveedor);
                        myDBHelper.close();
                        if(idProveedorCursor!=null){
                            proveedorId = idProveedorCursor.getString(idProveedorCursor.getColumnIndex(idProveedorCursor.getColumnName(0))) ;
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            spiNombreCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                    if (parent.getId() == R.id.spi_nombreCategoria) {
                        TextView tvNombreCategoria = (TextView)spiNombreCategoria.getSelectedView();
                        idNombreCategoria =  tvNombreCategoria.getText().toString();
                        myDBHelper.openDatabase();
                        Cursor idCategoriaCursor= myDBHelper.fetch_idByNombresCategoria(idNombreCategoria);
                        myDBHelper.close();
                        if(idCategoriaCursor!=null){
                            categoriaId = idCategoriaCursor.getString(idCategoriaCursor.getColumnIndex(idCategoriaCursor.getColumnName(0))) ;
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            edtImagen                      .setText(avatarUri);
            edtNombreProducto              .setText(strNombreProducto);
            edtCantidadPorUnidad           .setText(strCantidadUnidad);
            edtPrecioPorUnidad             .setText(String.valueOf(precioUnidad));
            edtUnidadesEnStock             .setText(String.valueOf(intUnidadStock));
            edtUnidadesEnPedido            .setText(String.valueOf(intUnidadPedido));
            edtNivelDeReaprovisionamiento  .setText(String.valueOf(intNivelReaprovisionamiento));
            edtInterrumpido                .setText(strInterrumpido);
            for (int i = 0; i < spiNombreProveedor.getCount(); i++) {
                if (spiNombreProveedor.getItemIdAtPosition(i)==Productos_ItemsFragment.intProveedorId) {
                    spiNombreProveedor.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < spiNombreCategoria.getCount(); i++) {
                if (spiNombreCategoria.getItemIdAtPosition(i)==Productos_ItemsFragment.intCategoriaId) {
                    spiNombreCategoria.setSelection(i);
                    break;
                }
            }

            final Builder builderEditarProducto = new Builder(this);
            builderEditarProducto.setTitle("Editar producto")
            .setView(viewEditarProducto)
            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
                    myDBHelper.openDatabase();
                    db = myDBHelper.getWritableDatabase();
                    ContentValues valores = new ContentValues();
                    if (edtNombreProducto.getText().toString().isEmpty()){
                        android.support.v7.app.AlertDialog.Builder builderEditProductoErr = new android.support.v7.app.AlertDialog.Builder(context);
                        builderEditProductoErr.setTitle("Error al actualizar Producto");
                        builderEditProductoErr.setMessage("Debe asignar Nombre");
                        builderEditProductoErr.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((ViewGroup)viewEditarProducto.getParent()).removeView(viewEditarProducto);
                                builderEditarProducto.show();
                                edtNombreProducto.requestFocus();
                            }
                        });
                        builderEditProductoErr.show();

                    }
                    else{
                    valores.put("NombreProducto", edtNombreProducto.getText().toString());
                    valores.put("ProveedorId", proveedorId);
                    valores.put("CategoriaId", categoriaId);
                    valores.put("CantidadPorUnidad", edtCantidadPorUnidad.getText().toString());
                    valores.put("PrecioPorUnidad", edtPrecioPorUnidad.getText().toString());
                    valores.put("UnidadesEnStock", edtUnidadesEnStock.getText().toString());
                    valores.put("UndadesEnPedido", edtUnidadesEnPedido.getText().toString());
                    valores.put("NivelDeReaprovisionamiento", edtNivelDeReaprovisionamiento.getText().toString());
                    String interrumpido=(edtInterrumpido.getText().toString().equalsIgnoreCase("Si")) ? "1":"0";
                    valores.put("Interrumpido", interrumpido);
                    valores.put("Foto", edtImagen.getText().toString());
                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{productoId};

                    try {
                        if(db.update("Productos",valores, where, whereArgs)!=0) {
                           Builder builderUpdateProductoSuccess = new Builder(context)
                            .setTitle("Editar producto")
                            .setMessage("Se actualizó el producto: " + strNombreProducto)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(context,ProductosActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            });
                            builderUpdateProductoSuccess.show();
                        }
                        db.close();
                        myDBHelper.close();

                    } catch (SQLException e) {
                        Builder builderUpdateProductoErr = new Builder(context);
                        builderUpdateProductoErr.setTitle("Error al editar producto")
                        .setMessage(e.getMessage())
                        .setNegativeButton("Aceptar", null)
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
            myCursor= myDBHelper.fetchProductoById(productoId);

            if (myCursor != null) {
                strNombreProducto = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(1))));
            }

            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
            dialog.setTitle("Eliminar Producto");
            dialog.setMessage("¿Seguro que deseas eliminar al Producto: " + strNombreProducto + "?");
            dialog.setNegativeButton("Cancelar", null);
            dialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{productoId};

                    try {
                        if (db.delete("Productos", where, whereArgs) != 0) {
                            db.close();
                            myDBHelper.close();
                            Toast.makeText(getApplicationContext(), "Se borro el producto: " + strNombreProducto, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context,ProductosActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }

                        if (ProductosActivity.mTwoPane) {
                            ((ProveedoresListaFragment.Refresh) getApplication()).refreshList();
                        }

                    } catch (SQLException e) {
                        AlertDialog.Builder builderInsertEmpErr = new AlertDialog.Builder(context);
                        builderInsertEmpErr.setTitle("Error al borrar producto");
                        builderInsertEmpErr.setMessage(e.getMessage());
                        builderInsertEmpErr.setNegativeButton("Aceptar",null);
                        builderInsertEmpErr.show();
                    }
                }
            });

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
