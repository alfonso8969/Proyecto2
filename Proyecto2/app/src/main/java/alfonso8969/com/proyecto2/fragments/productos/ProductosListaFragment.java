package alfonso8969.com.proyecto2.fragments.productos;

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
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.empleados.EmpleadosActivity;
import alfonso8969.com.proyecto2.activities.fotos.AddNewPhoto;
import alfonso8969.com.proyecto2.activities.productos.ProductosActivity;
import alfonso8969.com.proyecto2.adapters.ProductosListaAdapter;
import alfonso8969.com.proyecto2.clases.RecyclerItemClickListener;
import alfonso8969.com.proyecto2.fragments.empleados.EmpleadosListaFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 09/11/2016.
 */

public class ProductosListaFragment extends Fragment {

    RecyclerView recyclerViewProductos;
    ProductosListaAdapter productosListaAdapter;
    DataBaseHelper myDBHelper;
    public static final String TAG="RecyclerViewProductosFragment";
    TextView tvNombreProducto;
    String productoId,idNombreProveedor,categoriaId,proveedorId,idNombreCategoria;
    FloatingActionButton fb;
    public ViewGroup containerProducto;
    public static LayoutInflater inflaterProducto;
    TextView delete;
    String strNombreproducto;
    public interface CallBacksProductos{
        void onItemSelected(String productoId);
    }
    public  interface RefreshProductos{
        void refreshList();
    }
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        View viewRoot = inflater.inflate(R.layout.lista_productos_fragment, container, false);
        viewRoot.setTag(TAG);

        containerProducto = container;
        inflaterProducto = inflater;
        recyclerViewProductos = (RecyclerView) viewRoot.findViewById(R.id.lista_Productos);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        fb=(FloatingActionButton)viewRoot.findViewById(R.id.fbProductos);
        fb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final android.support.v7.app.AlertDialog.Builder builderAgregarNuevoProducto = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builderAgregarNuevoProducto.setTitle("Agregar un producto");
                myDBHelper = new DataBaseHelper(getActivity().getApplicationContext());

                onCreateView(inflater,container,saveInstanceState);
                final View viewAgregarNuevoProducto=inflater.inflate(R.layout.insertar_producto,container,false);

                builderAgregarNuevoProducto.setView(viewAgregarNuevoProducto);

                final EditText edtImagen=(EditText)viewAgregarNuevoProducto.findViewById(R.id.edt_imagenProducto) ;
                edtImagen.setText(AddNewPhoto.nombreFotoBuscar);
                final EditText edtNombreProducto=(EditText)viewAgregarNuevoProducto.findViewById(R.id.edt_nombreProducto);
                final EditText edtCantidadPorUnidad=(EditText)viewAgregarNuevoProducto.findViewById(R.id.edt_CantidadPorUnidad);
                final EditText edtPrecioPorUnidad=(EditText)viewAgregarNuevoProducto.findViewById(R.id.edt_PrecioPorUnidad);
                final EditText edtUnidadesEnStock=(EditText)viewAgregarNuevoProducto.findViewById(R.id.edt_UnidadesEnStock);
                final EditText edtUnidadesEnPedido=(EditText)viewAgregarNuevoProducto.findViewById(R.id.edt_UnidadesEnPedido);
                final EditText edtNivelDeReaprovisionamiento=(EditText)viewAgregarNuevoProducto.findViewById(R.id.edt_NivelDeReaprovisionamiento);
                final EditText edtInterrumpido=(EditText)viewAgregarNuevoProducto.findViewById(R.id.edt_Interrumpido);
                final Spinner spiNombreProveedor=(Spinner)viewAgregarNuevoProducto.findViewById(R.id.spi_nombreProveedor);
                final Spinner spiNombreCategoria=(Spinner)viewAgregarNuevoProducto.findViewById(R.id.spi_nombreCategoria);

                myDBHelper.openDatabase();
                Cursor cursorNombreProveedor = myDBHelper.fetchAllNombresProveedores();
                Cursor cursorNombreCategoria = myDBHelper.fetchAllNombresCategorias();
                myDBHelper.close();

                SimpleCursorAdapter adapterNombreProveedor = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                       android.R.layout.select_dialog_item,cursorNombreProveedor,new String[] {"NombreCompania"},
                        new int[] {android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                adapterNombreProveedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spiNombreProveedor.setAdapter(adapterNombreProveedor);
                spiNombreProveedor.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);


                SimpleCursorAdapter adapterNombreCategoria = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                        android.R.layout.select_dialog_item,cursorNombreCategoria,new String[]{"NombreCategoria"},
                        new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                adapterNombreCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spiNombreCategoria.setAdapter(adapterNombreCategoria);
                spiNombreCategoria.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);

                spiNombreProveedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                            if (parent.getId() == R.id.spi_nombreProveedor) {
                                TextView textView = (TextView)spiNombreProveedor.getSelectedView();
                                idNombreProveedor = textView.getText().toString();
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
                            TextView textView = (TextView)spiNombreCategoria.getSelectedView();
                            idNombreCategoria =  textView.getText().toString();
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
                builderAgregarNuevoProducto.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDBHelper.openDatabase();
                        SQLiteDatabase db = myDBHelper.getWritableDatabase();
                        ContentValues valores = new ContentValues();

                        if (edtNombreProducto.getText().toString().isEmpty()){
                            android.support.v7.app.AlertDialog.Builder builderInsertProductoErr = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builderInsertProductoErr.setTitle("Error  al insertar Producto");
                            builderInsertProductoErr.setMessage("Debe asignar Nombre");
                            builderInsertProductoErr.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((ViewGroup)viewAgregarNuevoProducto.getParent()).removeView(viewAgregarNuevoProducto);
                                    builderAgregarNuevoProducto.show();
                                    edtNombreProducto.requestFocus();
                                }
                            });
                            builderInsertProductoErr.show();

                        }else{

                            valores.put("NombreProducto", edtNombreProducto.getText().toString());
                            valores.put("ProveedorId", proveedorId);
                            valores.put("CategoriaId", categoriaId);
                            valores.put("CantidadPorUnidad", edtCantidadPorUnidad.getText().toString());
                            valores.put("PrecioPorUnidad", edtPrecioPorUnidad.getText().toString());
                            valores.put("UnidadesEnStock", edtUnidadesEnStock.getText().toString());
                            valores.put("UndadesEnPedido", edtUnidadesEnPedido.getText().toString());
                            valores.put("NivelDeReaprovisionamiento", edtNivelDeReaprovisionamiento.getText().toString());
                            String strInterrumpido=(edtInterrumpido.getText().toString().equalsIgnoreCase("Si")) ? "1":"0";
                            valores.put("Interrumpido", strInterrumpido);
                            valores.put("Foto", edtImagen.getText().toString());

                            try {

                                if (db.insert("Productos", null, valores) != 0) {
                                    android.support.v7.app.AlertDialog.Builder builderInsertEmpSuccess = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                    builderInsertEmpSuccess.setTitle("Nuevo producto");
                                    builderInsertEmpSuccess.setMessage("Se agregó el producto: " + edtNombreProducto.getText().toString());

                                    builderInsertEmpSuccess.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            onDestroyInsert();
                                        }
                                    });

                                    builderInsertEmpSuccess.show();
                                }
                                db.close();
                                myDBHelper.close();
                            } catch (SQLException e) {
                                android.support.v7.app.AlertDialog.Builder builderInsertProductoErr = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                builderInsertProductoErr.setTitle("Error  al insertar producto");
                                builderInsertProductoErr.setMessage(e.getMessage());
                                builderInsertProductoErr.setNegativeButton("Aceptar",null);
                                builderInsertProductoErr.show();
                            }
                        }
                    }
                });
                builderAgregarNuevoProducto.setNegativeButton("Cancelar",null);

                builderAgregarNuevoProducto.setNeutralButton("Foto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (edtImagen.getText().toString().isEmpty()) {
                            android.support.v7.app.AlertDialog.Builder builderInsertFoto = new android.support.v7.app.AlertDialog.Builder(getActivity());
                            builderInsertFoto.setTitle("Error  al insertar Foto");
                            builderInsertFoto.setMessage("Debe asignar un nombre de archivo imagen");
                            builderInsertFoto.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((ViewGroup)viewAgregarNuevoProducto.getParent()).removeView(viewAgregarNuevoProducto);
                                    builderAgregarNuevoProducto.show();
                                    edtImagen.requestFocus();
                                }
                            });
                            builderInsertFoto.show();
                        }
                        else{
                            Intent intent = new Intent(getActivity(), AddNewPhoto.class);
                            String nombreFoto = edtImagen.getText().toString();
                            intent.putExtra("nombreFoto", nombreFoto);
                            startActivity(intent);
                        }
                    }
                });
                builderAgregarNuevoProducto.setIcon(R.drawable.productos);
                builderAgregarNuevoProducto.show();
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
            Cursor cursor = myDBHelper.fetchAllProductos();
            if(cursor != null){
                productosListaAdapter = new ProductosListaAdapter(getActivity().getApplicationContext(),cursor);
                recyclerViewProductos.setAdapter(productosListaAdapter);
                recyclerViewProductos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
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
            TextView textViewId = (TextView) childView.findViewById(R.id.idProducto);
            productoId = textViewId.getText().toString();
            tvNombreProducto = (TextView) childView.findViewById(R.id.NombreProducto);
            ((CallBacksProductos) getActivity()).onItemSelected(productoId);
            Toast.makeText(getActivity(), "Ha seleccionado el producto: " + tvNombreProducto.getText().toString()
                    +"\nCon Id: "+ productoId, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onItemLongPress(View childView, int position){

            delete = (TextView) childView.findViewById(R.id.idProducto);
            tvNombreProducto = (TextView) childView.findViewById(R.id.NombreProducto);
            productoId = delete.getText().toString();
            strNombreproducto = tvNombreProducto.getText().toString();
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Eliminar Producto");
            dialog.setMessage("¿Seguro que deseas eliminar el Producto: "+ strNombreproducto +" con id: "+productoId+"?");
            dialog.setNegativeButton("Cancelar", null);
            dialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            myDBHelper = new DataBaseHelper(getActivity().getApplication());
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
            String where = "_id" + "=?";
            String[] whereArgs = new String[]{productoId};

            db.delete("Productos", where, whereArgs);

            db.close();
            myDBHelper.close();
            Toast.makeText(getActivity(), "Se borro el producto: " + strNombreproducto, Toast.LENGTH_SHORT).show();

            try {
            myDBHelper.openDatabase();
            Cursor nCursor = myDBHelper.fetchAllProductos();
            myDBHelper.close();
            if (nCursor != null) {
            productosListaAdapter = new ProductosListaAdapter(getActivity().getApplicationContext(), nCursor);
            recyclerViewProductos.setAdapter(productosListaAdapter);
            recyclerViewProductos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new OnItemClickListener()));
            }


            if (EmpleadosActivity.mTwoPane) {
            ((EmpleadosListaFragment.Refresh)getActivity()).refreshList();
            }

            } catch (SQLException e) {
            android.support.v7.app.AlertDialog.Builder builderDeleteProductoErr = new android.support.v7.app.AlertDialog.Builder(getActivity());
            builderDeleteProductoErr.setTitle("Error al borrar el producto");
            builderDeleteProductoErr.setMessage(e.getMessage());
            builderDeleteProductoErr.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
            });
            builderDeleteProductoErr.show();
            }

            }
            });
            dialog.show();

        }
    }
    public void onDestroyInsert(){
        Intent intent = new Intent(getActivity(), ProductosActivity.class);
        getActivity().finish();
        startActivity(intent);
    }
}
