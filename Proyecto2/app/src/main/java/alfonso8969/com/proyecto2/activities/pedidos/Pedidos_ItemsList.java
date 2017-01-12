package alfonso8969.com.proyecto2.activities.pedidos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.pedidos.PedidosListaFragment;
import alfonso8969.com.proyecto2.fragments.pedidos.Pedidos_ItemsFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

public class Pedidos_ItemsList extends AppCompatActivity {
String pedidoId;
    public ViewGroup containerPedidos;
    public DataBaseHelper myDBHelper;
    public Cursor myCursor;
    public Context context;
    public PedidosListaFragment pedidosListaFragment;
    public SQLiteDatabase db;
    public TextView tvNumPedido,tvClienteId,tvFechaPedido,tvFechaRequerida,tvNombreCliente,tvDireccionCliente,tvNombreCompañiaEnvio;
    public TextView tvNombreEmpleado,tvPrecioPorUnidad,tvUnidadesEnPedido,tvUnidadesEnStock;
    public Button btn_fechaEntrega;
    public Spinner spiVIaEnvio,spiNombreProductoPedido, spiNombreProductoNuevo,spiDescuentoNuevo,spiNombreProductoPedidoBorrar;
    public String strNombreCompañiaEnvio, strNombreProductoEnPedido,strClienteID,strNombreEmpleado,strApellidoEmpleado,strNombreCompletoEmpleado;
    public String strNombreCompañia,strFechaPedido,strFechaRequerida, strIdProductoEnPedido,strCantidad;
    public int intIdNombreCompañia,intIdNombreCompañiaPedido, intIdProductoNuevo,intUnidadesEnPedido,intUnidadesEnStock;
    public int intNumeroPedido,intNumeroEmpleado,intViaEnvio,cantidadEnPedido,intCantidadNueva,intNumeroDeLineas;
    public static TextView tvFechaEntrega;
    public static String strFechaEntrega;
    private double precioUnidad,descuento,douDescontado,doubleDescuento,doublePrecioFleteNuevo,douPrecioFlete,doublePrecioPorUnidad;
    private TextView tvCantidad,tvDescuento,tvPrecioTotalLineaPedido,tvPrecioTotalPedido;
    private View viewEditarPedido,viewBorrarPedido;
    private EditText edtCantidad;
    public boolean boolCambios;
    private Builder builderBorrarLineasDelPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos__items_list);

        Bundle intent = getIntent().getExtras();
        Bundle extras = getIntent().getExtras();
        pedidoId = extras.getString("pedidoId");
        if(savedInstanceState == null){

            Pedidos_ItemsFragment fragment = new Pedidos_ItemsFragment();
            fragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pedidos_list_item,fragment)
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
            final DecimalFormat df = new DecimalFormat("#.##");
            context = this;
            myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
            pedidosListaFragment = new PedidosListaFragment();
            containerPedidos = pedidosListaFragment.containerPedido;
            viewEditarPedido = PedidosListaFragment.inflaterPedido.inflate(R.layout.editar_pedido,containerPedidos,false);
            myDBHelper.openDatabase();
            myCursor= myDBHelper.fetchPedidoById(pedidoId);
            Cursor cursorFechaEntregaPedido = myDBHelper.fetchFechaEntregaPedidoById(pedidoId);
            strFechaEntrega = cursorFechaEntregaPedido.getString(cursorFechaEntregaPedido.getColumnIndex(cursorFechaEntregaPedido.getColumnName(0)));

            tvNumPedido      = (TextView)viewEditarPedido.findViewById(R.id.tv_numeroPedido);
            tvClienteId      = (TextView)viewEditarPedido.findViewById(R.id.tv_clienteIDPedido);
            tvFechaPedido    = (TextView)viewEditarPedido.findViewById(R.id.tv_fechaPedido);
            tvFechaRequerida = (TextView)viewEditarPedido.findViewById(R.id.tv_fechaRequerida);
            tvFechaEntrega = (TextView) viewEditarPedido.findViewById(R.id.tv_fechaEntrega);
            btn_fechaEntrega = (Button) viewEditarPedido.findViewById(R.id.btn_fechaEntrega);
            btn_fechaEntrega.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DialogFragment newFragment = new SelectDateFragmentFechaEntrega();
                    newFragment.show(getSupportFragmentManager(), "DatePicker");

                }
            });
            tvNombreCliente  = (TextView)viewEditarPedido.findViewById(R.id.tv_nombre_compañiaPedido);
            tvDireccionCliente = (TextView)viewEditarPedido.findViewById(R.id.tv_direccion_clientePedido);
            tvNombreCompañiaEnvio = (TextView)viewEditarPedido.findViewById(R.id.tv_Via_Envio);
            spiVIaEnvio = (Spinner)viewEditarPedido.findViewById(R.id.spi_Via_Envio);
            myDBHelper.openDatabase();
            Cursor cursorNombreCompañiaEnvio = myDBHelper.fetchAllNombresCompañiaEnvio();
            myDBHelper.close();
            SimpleCursorAdapter adapterNombreCompañiaEnvio = new SimpleCursorAdapter(getApplicationContext(),
                    android.R.layout.select_dialog_item, cursorNombreCompañiaEnvio, new String[]{"NombreCompania"},
                    new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            adapterNombreCompañiaEnvio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiVIaEnvio.setAdapter(adapterNombreCompañiaEnvio);
            spiVIaEnvio.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
            spiVIaEnvio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                    if (parent.getId() == R.id.spi_Via_Envio) {
                        TextView textView = (TextView) spiVIaEnvio.getSelectedView();
                        strNombreCompañiaEnvio = textView.getText().toString();
                        myDBHelper.openDatabase();
                        Cursor cursorIdNombreCompañiaEnvio = myDBHelper.fetch_idByNombreCompaniaEnvios(strNombreCompañiaEnvio);
                        myDBHelper.close();
                        if(cursorIdNombreCompañiaEnvio!=null){
                            intIdNombreCompañia=cursorIdNombreCompañiaEnvio.getInt(cursorIdNombreCompañiaEnvio.getColumnIndex(cursorIdNombreCompañiaEnvio.getColumnName(0)));
                        }

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            tvNombreEmpleado = (TextView)viewEditarPedido.findViewById(R.id.tv_nombreEmpleadoPedido);
            tvPrecioPorUnidad  = (TextView)viewEditarPedido.findViewById(R.id.tv_PrecioPorUnidad);
            tvUnidadesEnPedido = (TextView)viewEditarPedido.findViewById(R.id.tv_UnidadesEnPedido);
            tvUnidadesEnStock  = (TextView)viewEditarPedido.findViewById(R.id.tv_UnidadesEnStock);
            tvCantidad= (TextView)viewEditarPedido.findViewById(R.id.tv_CantidadEnPedido);
            tvDescuento = (TextView)viewEditarPedido.findViewById(R.id.tv_DescuentoEnPedido);
            final TextView tvPrecioFlete = (TextView) viewEditarPedido.findViewById(R.id.tv_PrecioFlete);
            tvPrecioTotalLineaPedido =(TextView)viewEditarPedido.findViewById(R.id.tv_PrecioTotalPorLinea);

            spiNombreProductoPedido = (Spinner)viewEditarPedido.findViewById(R.id.spi_nombreProductoEnPedido);
            spiNombreProductoNuevo = (Spinner)viewEditarPedido.findViewById(R.id.spi_nombreProductoNuevo);
            myDBHelper.openDatabase();
            Cursor cursorNombreProducto = myDBHelper.fetchAllNombresProductos();
            myDBHelper.close();
            SimpleCursorAdapter adapterNombreProducto = new SimpleCursorAdapter(getApplicationContext(),
                    android.R.layout.select_dialog_item,cursorNombreProducto,new String[]{"NombreProducto"},
                    new int[]{android.R.id.text1},SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            adapterNombreProducto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiNombreProductoNuevo.setAdapter(adapterNombreProducto);
            spiNombreProductoNuevo.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
            spiNombreProductoNuevo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                    if(parent.getId()== R.id.spi_nombreProductoNuevo){
                        TextView textView = (TextView) spiNombreProductoNuevo.getSelectedView();
                        strNombreProductoEnPedido = textView.getText().toString();
                        myDBHelper.openDatabase();
                        Cursor cursorIdProducto = myDBHelper.fetchIdProductoByNombreProducto(strNombreProductoEnPedido);
                        Cursor cursorNombreProducto = myDBHelper.fetchDatosProductosByNombreProducto(strNombreProductoEnPedido);
                        myDBHelper.close();
                        if(cursorIdProducto!=null){
                            intIdProductoNuevo = cursorIdProducto.getInt(cursorIdProducto.getColumnIndex(cursorIdProducto.getColumnName(0)));
                        }
                        if (cursorNombreProducto!=null){
                            doublePrecioPorUnidad = cursorNombreProducto.getDouble(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(0)));
                            intUnidadesEnPedido = cursorNombreProducto.getInt(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(1)));
                            intUnidadesEnStock  = cursorNombreProducto.getInt(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(2)));
                            tvPrecioPorUnidad.setText(String.valueOf(doublePrecioPorUnidad));
                            tvUnidadesEnPedido.setText(String.valueOf(intUnidadesEnPedido));
                            tvUnidadesEnStock.setText(String.valueOf(intUnidadesEnStock));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            if (strFechaEntrega == null){
                if (myCursor != null) {

                    intNumeroPedido = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                    strClienteID = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                    intNumeroEmpleado=(myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(3))));
                    myDBHelper.openDatabase();
                    Cursor cursorNombreEmpleado =myDBHelper.fetchNombreEmpleadoById(String.valueOf(intNumeroEmpleado));
                    strNombreEmpleado =cursorNombreEmpleado.getString(cursorNombreEmpleado.getColumnIndex(cursorNombreEmpleado.getColumnName(0)));
                    strApellidoEmpleado =cursorNombreEmpleado.getString(cursorNombreEmpleado.getColumnIndex(cursorNombreEmpleado.getColumnName(1)));
                    strNombreCompletoEmpleado = strApellidoEmpleado+", "+strNombreEmpleado;

                    Cursor cursorNombreCompañia= myDBHelper.fetchDatosClienteByClienteID(strClienteID);
                    strNombreCompañia = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(0))));
                    String direccion = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(1))));
                    String codPos = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(4))));


                    strFechaPedido= (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                    strFechaRequerida = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(5))));

                    intViaEnvio = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(7))));
                    Cursor cursorNombreCompañiaEnvioById = myDBHelper.fetchNombreCompañiaEnvioById(String.valueOf(intViaEnvio));
                    strNombreCompañiaEnvio = cursorNombreCompañiaEnvioById.getString(cursorNombreCompañiaEnvioById.getColumnIndex(cursorNombreCompañiaEnvioById.getColumnName(0)));

                    Cursor cursorCalculoSumaTotalPrecioFlete = myDBHelper.calculoSumaTotalPrecioFlete(String.valueOf(intNumeroPedido));
                    douPrecioFlete = cursorCalculoSumaTotalPrecioFlete.getDouble(cursorCalculoSumaTotalPrecioFlete.getColumnIndex(cursorCalculoSumaTotalPrecioFlete.getColumnName(0)));

                    spiNombreProductoPedido = (Spinner)viewEditarPedido.findViewById(R.id.spi_nombreProductoEnPedido);
                    Cursor cursorProductoIdFromPedidosDetalles = myDBHelper.fetchAllProductoIdForPedidosDetallesPedidoId(String.valueOf(intNumeroPedido));
                    SimpleCursorAdapter adapterProductoIdFromPedidosDetalles = new SimpleCursorAdapter(getApplicationContext(),
                            android.R.layout.select_dialog_item,cursorProductoIdFromPedidosDetalles,new String[] {"productoId"},
                            new int[] {android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                    adapterProductoIdFromPedidosDetalles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spiNombreProductoPedido.setAdapter(adapterProductoIdFromPedidosDetalles);
                    spiNombreProductoPedido.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                    myDBHelper.close();
                    spiNombreProductoPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                            if (parent.getId() == R.id.spi_nombreProductoEnPedido) {
                                TextView tvNombreProducto = (TextView)spiNombreProductoPedido.getSelectedView();
                                strIdProductoEnPedido = tvNombreProducto.getText().toString();
                                myDBHelper.openDatabase();
                                Cursor idProductoCursor= myDBHelper.fetch_idNombresProducto(strIdProductoEnPedido);

                                if(idProductoCursor!=null){
                                    strNombreProductoEnPedido = idProductoCursor.getString(idProductoCursor.getColumnIndex(idProductoCursor.getColumnName(0)));
                                    tvNombreProducto.setText(strNombreProductoEnPedido);

                                    Cursor cursorCantidadNombreProducto = myDBHelper.fetchCantidadByPedidoId(strIdProductoEnPedido,String.valueOf(intNumeroPedido));
                                    cantidadEnPedido = cursorCantidadNombreProducto.getInt(cursorCantidadNombreProducto.getColumnIndex(cursorCantidadNombreProducto.getColumnName(1)));

                                    Cursor cursorPrecioUnidad = myDBHelper.fetchPrecioUnidadByProductoIdAndPedidoId(strIdProductoEnPedido,String.valueOf(intNumeroPedido));
                                    precioUnidad = cursorPrecioUnidad.getDouble(cursorPrecioUnidad.getColumnIndex(cursorPrecioUnidad.getColumnName(1)));

                                    Cursor cursorDescuento = myDBHelper.fetchDescuentoByPedidoId(strIdProductoEnPedido,String.valueOf(intNumeroPedido));
                                    descuento = cursorDescuento.getDouble(cursorDescuento.getColumnIndex(cursorDescuento.getColumnName(0)));

                                    tvCantidad.setText(String.valueOf(cantidadEnPedido));
                                    tvPrecioPorUnidad.setText(String.format("%s €", String.valueOf(precioUnidad)));
                                    tvDescuento.setText(String.format("%s%%", String.valueOf(descuento)));
                                    douDescontado =(cantidadEnPedido *precioUnidad*descuento);
                                    double douPrecioPorLinea=  (cantidadEnPedido *precioUnidad)-douDescontado;

                                    tvPrecioTotalLineaPedido.setText(String.format("%s €", String.valueOf(df.format(douPrecioPorLinea))));
                                    myDBHelper.close();
                                }

                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spiDescuentoNuevo = (Spinner)viewEditarPedido.findViewById(R.id.spi_DescuentoNuevo);
                    final Double[] datosDescuento= new Double[]{0.0,0.05,0.1,0.15,0.20,0.25,0.3,0.40};
                    ArrayAdapter<Double> adapterDescuento = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, datosDescuento);
                    adapterDescuento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spiDescuentoNuevo.setAdapter(adapterDescuento);
                    spiDescuentoNuevo.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                    spiDescuentoNuevo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                            if(parent.getId()==R.id.spi_DescuentoNuevo){

                                TextView textView = (TextView) spiDescuentoNuevo.getSelectedView();
                                doubleDescuento = Double.valueOf(textView.getText().toString());
                                edtCantidad = (EditText)viewEditarPedido.findViewById(R.id.edt_CantidadNueva);
                                strCantidad = edtCantidad.getText().toString();

                                if (!strCantidad.isEmpty()) {
                                    intCantidadNueva = Integer.parseInt(strCantidad);
                                    boolCambios = true;
                                    if(intCantidadNueva > intUnidadesEnStock){
                                        Builder builderCantidadSupUniEnStock = new Builder(context);
                                        builderCantidadSupUniEnStock.setTitle("Error en Cantidad")
                                                .setMessage("La cantidadEnPedido del pedido no puede superar las unidades en Stock")
                                                .setPositiveButton("Aceptar",null)
                                                .show();
                                        edtCantidad.setText("");
                                    }
                                    myDBHelper.openDatabase();
                                    Cursor cursorPrecioUnidad = myDBHelper.fetchPrecioUnidadByProductoId(String.valueOf(intIdProductoNuevo));
                                    precioUnidad = cursorPrecioUnidad.getDouble(cursorPrecioUnidad.getColumnIndex(cursorPrecioUnidad.getColumnName(0)));
                                    myDBHelper.close();
                                    doublePrecioFleteNuevo = (intCantidadNueva * doublePrecioPorUnidad * 20) / 100;
                                    tvPrecioFlete.setText(String.format("%s €", String.valueOf(doublePrecioFleteNuevo)));
                                    douDescontado =(intCantidadNueva *precioUnidad*doubleDescuento);
                                    double douPrecioPorLinea=  (intCantidadNueva *precioUnidad)-douDescontado;
                                    tvPrecioTotalLineaPedido.setText(String.format("%s €", String.valueOf(df.format(douPrecioPorLinea))));
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                   /* myDBHelper.openDatabase();
                    Cursor cursorCalculoPrecioTotalPedido = myDBHelper.calculoSumaTotalPedido(String.valueOf(intNumeroPedido));
                    double precioTotal = cursorCalculoPrecioTotalPedido.getDouble(cursorCalculoPrecioTotalPedido.getColumnIndex(cursorCalculoPrecioTotalPedido.getColumnName(0)));
                    Cursor cursorLineasDelPedido= myDBHelper.fetchLineasDePedido(String.valueOf(intNumeroPedido));
                    int intLineasPedido = cursorLineasDelPedido.getInt(cursorLineasDelPedido.getColumnIndex(cursorLineasDelPedido.getColumnName(0)));
                    myDBHelper.close();*/

                    tvNumPedido.setText(String.valueOf(intNumeroPedido));
                    tvClienteId.setText(String.valueOf(strClienteID));
                    tvNombreCliente.setText(strNombreCompañia);
                    tvFechaPedido.setText(String.valueOf(strFechaPedido));
                    tvFechaRequerida.setText(strFechaRequerida);
                    tvFechaEntrega.setText(strFechaEntrega);
                    tvNombreCompañiaEnvio.setText(strNombreCompañiaEnvio);
                    tvNombreEmpleado.setText(strNombreCompletoEmpleado);
                    tvPrecioFlete.setText(String.format("%s€", String.valueOf(douPrecioFlete)));
                    String strDireccionCoPos = direccion + "---" + codPos;
                    tvDireccionCliente.setText(strDireccionCoPos);
                    //tvPrecioTotalPedido.setText(String.valueOf(df.format(precioTotal+douPrecioFlete)) + "€");
                   // tvLineasDePedido.setText(String.valueOf(intLineasPedido));

                }


                Builder builderEditarPedido = new Builder(this);
                builderEditarPedido.setView(viewEditarPedido)
                .setTitle("Editar Pedido")
                .setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDBHelper.openDatabase();
                        SQLiteDatabase db = myDBHelper.getWritableDatabase();

                        ContentValues valoresPedidosDetalles = new ContentValues();
                        ContentValues valoresUpDateProductos = new ContentValues();
                        ContentValues valoresUpDateProductosNuevas = new ContentValues();
                        ContentValues valoresPrecioFletePedido = new ContentValues();
                        ContentValues valoresPedidos = new ContentValues();
                        if(boolCambios){
                            if (strCantidad.isEmpty() || Integer.valueOf(strCantidad)==0) {
                                Builder builderCantidadCero = new Builder(context);
                                builderCantidadCero.setTitle("Error en Cantidad")
                                        .setMessage("La cantidadEnPedido del pedido no puede ser cero")
                                        .setPositiveButton("Aceptar",null)
                                        .show();
                                edtCantidad.setText("");
                                edtCantidad.requestFocus();
                            }
                            db.execSQL("Delete FROM PedidosDetalles WHERE PedidoId="+pedidoId +" AND productoId="+ strIdProductoEnPedido);
                            valoresPedidosDetalles.put("PedidoId",pedidoId);
                            valoresPedidosDetalles.put("productoId", intIdProductoNuevo);
                            valoresPedidosDetalles.put("PrecioUnidad", doublePrecioPorUnidad);
                            valoresPedidosDetalles.put("Cantidad", intCantidadNueva);
                            valoresPedidosDetalles.put("Descuento", doubleDescuento);

                            if(db.insert("PedidosDetalles", null, valoresPedidosDetalles)!=0){
                                Toast.makeText(context,"Se insertaron los valores en Detalles",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context,"No se insertaron los valores en Detalles",Toast.LENGTH_LONG).show();
                            }
                            Cursor cursorUnidadesEnPedidoNuevas = myDBHelper.fetchUnidadesEnPedidoFromProductosByProductoId(String.valueOf(intIdProductoNuevo));
                            int unidadesEnPedidoNuevas = cursorUnidadesEnPedidoNuevas.getInt(cursorUnidadesEnPedidoNuevas.getColumnIndex(cursorUnidadesEnPedidoNuevas.getColumnName(0)));
                            int unidadesEnPedidoActualesNuevas = unidadesEnPedidoNuevas + intCantidadNueva;
                            Cursor cursorUnidadesEnStockNuevas  = myDBHelper.fetchUnidadesEnStockFromProductosByProductoId(String.valueOf(intIdProductoNuevo));
                            int unidadesEnStockNuevas = cursorUnidadesEnStockNuevas.getInt(cursorUnidadesEnStockNuevas.getColumnIndex(cursorUnidadesEnStockNuevas.getColumnName(0)));
                            int unidadesEnStockActualesNuevas = unidadesEnStockNuevas-intCantidadNueva;
                            final String whereNuevas = "_id" + "=?";
                            final String[] whereArgsNuevas = new String[]{String.valueOf(intIdProductoNuevo)};
                            valoresUpDateProductosNuevas.put("UnidadesEnStock",unidadesEnStockActualesNuevas);
                            valoresUpDateProductosNuevas.put("UndadesEnPedido",unidadesEnPedidoActualesNuevas);
                            db.update("Productos",valoresUpDateProductosNuevas, whereNuevas, whereArgsNuevas);

                            Cursor cursorUnidadesEnPedido = myDBHelper.fetchUnidadesEnPedidoFromProductosByProductoId(strIdProductoEnPedido);
                            int unidadesEnPedidoAntiguaEnPedido = cursorUnidadesEnPedido.getInt(cursorUnidadesEnPedido.getColumnIndex(cursorUnidadesEnPedido.getColumnName(0)));
                            int unidadesEnPedidoActualesEnPedido = unidadesEnPedidoAntiguaEnPedido - cantidadEnPedido;
                            Cursor cursorUnidadesEnStock  = myDBHelper.fetchUnidadesEnStockFromProductosByProductoId(strIdProductoEnPedido);
                            int unidadesEnStockAntigua = cursorUnidadesEnStock.getInt(cursorUnidadesEnStock.getColumnIndex(cursorUnidadesEnStock.getColumnName(0)));
                            int unidadesEnStockActuales = unidadesEnStockAntigua+cantidadEnPedido;
                            final String where = "_id" + "=?";
                            final String[] whereArgs = new String[]{strIdProductoEnPedido};
                            valoresUpDateProductos.put("UnidadesEnStock",unidadesEnStockActuales);
                            valoresUpDateProductos.put("UndadesEnPedido",unidadesEnPedidoActualesEnPedido);
                            db.update("Productos",valoresUpDateProductos, where, whereArgs);

                            Cursor cursorPrecioFleteActual = myDBHelper.fetchPrecioFleteByPedidoId(pedidoId);
                            double precioFleteActual = cursorPrecioFleteActual.getDouble(cursorPrecioFleteActual.getColumnIndex(cursorPrecioFleteActual.getColumnName(0)));
                            double precioFleteFinal = precioFleteActual - douPrecioFlete + doublePrecioFleteNuevo;
                            valoresPrecioFletePedido.put("PrecioFlete",precioFleteFinal);
                            db.update("Pedidos",valoresPrecioFletePedido,"PedidoID=?",new String[]{pedidoId});
                            onDestroyEditAndDeleteMoreLines();

                        }
                        valoresPedidos.put("FechaEntrega",strFechaEntrega);
                        if(strFechaEntrega!=null){
                            db.update("Pedidos",valoresPedidos,"PedidoID=?",new String[]{pedidoId});
                            Toast.makeText(context,"Fecha entrega actualizada",Toast.LENGTH_LONG).show();
                            onDestroyEdit();
                        }

                        myDBHelper.close();
                    }
                })
                .setNegativeButton("Cancelar",null)
                .show();
            }
            else{
                Builder builderFechaEntrega = new Builder(this);
                builderFechaEntrega.setTitle("Error al editar Pedido")
                        .setMessage("El pedido esta entregado\nFecha entrega: "+strFechaEntrega)
                        .setNegativeButton("Aceptar",null)
                .show();
            }
        }

        if (item.getItemId() == R.id.action_delete) {
            context = this;
            myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
            myDBHelper.openDatabase();
            pedidosListaFragment = new PedidosListaFragment();
            containerPedidos = pedidosListaFragment.containerPedido;
            viewBorrarPedido = PedidosListaFragment.inflaterPedido.inflate(R.layout.borrar_pedido,containerPedidos,false);


            Cursor cursorFechaEntregaPedido = myDBHelper.fetchFechaEntregaPedidoById(pedidoId);
            strFechaEntrega = cursorFechaEntregaPedido.getString(cursorFechaEntregaPedido.getColumnIndex(cursorFechaEntregaPedido.getColumnName(0)));
            if(strFechaEntrega == null){
               cargarDatosDelete(viewBorrarPedido);


                builderBorrarLineasDelPedido = new Builder(context);
                builderBorrarLineasDelPedido.setTitle("Borrar Lineas del Pedido")
                        .setView(viewBorrarPedido)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            deleteLines();
                            onDestroyDelete();

                        }
                        })
                        .setNegativeButton("Borrar más lineas?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteLines();
                                if(intNumeroDeLineas!=1){
                                    cargarDatosDelete(viewBorrarPedido);
                                    onDestroyDeleteMoreLines();
                                }else
                                onDestroyDelete();



                            }
                        })
                        .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onDestroyEditAndDeleteMoreLines();
                            }
                        })
                        .show();


            }else{
                Builder builderBorrarPedido = new Builder(this);
                builderBorrarPedido.setTitle("Borrar el Pedido")
                        .setMessage("El pedido esta entregado\nFecha entrega: "+strFechaEntrega+"\nSeguro que desea borrar el pedido: "+pedidoId)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SQLiteDatabase dbDelete =myDBHelper.getWritableDatabase();
                                myDBHelper.openDatabase();
                                String wherePedidos = "PedidoID" + "=?";
                                String wherePedidosDetalles = "PedidoId"+"=?";
                                String[] whereArgs = new String[]{pedidoId};
                                dbDelete.delete("Pedidos", wherePedidos, whereArgs);
                                dbDelete.delete("PedidosDetalles",wherePedidosDetalles,whereArgs);
                                dbDelete.close();
                                myDBHelper.close();
                                onDestroyDelete();
                                Toast.makeText(context, "Se borro el pedido: " + pedidoId, Toast.LENGTH_SHORT).show();
                            }
                            })
                        .setNegativeButton("Cancelar",null)
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public static class SelectDateFragmentFechaEntrega extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            SetDateFechaEntrega(yy, mm+1, dd);
        }

        private void SetDateFechaEntrega(int mYear, int mMonth, int mDay) {

            tvFechaEntrega.setText(new StringBuilder()
                    .append(mDay).append("/")
                    .append(mMonth).append("/")
                    .append(mYear).append(" "));
            strFechaEntrega = tvFechaEntrega.getText().toString();
        }
    }

    public void deleteLines(){
        ContentValues valoresUpDateProductos = new ContentValues();
        myDBHelper.openDatabase();
        SQLiteDatabase dbDelete = myDBHelper.getWritableDatabase();
        Cursor cursorUnidadesEnPedido = myDBHelper.fetchUnidadesEnPedidoFromProductosByProductoId(strIdProductoEnPedido);
        int unidadesEnPedidoAntigua = cursorUnidadesEnPedido.getInt(cursorUnidadesEnPedido.getColumnIndex(cursorUnidadesEnPedido.getColumnName(0)));
        int unidadesEnPedidoActuales = unidadesEnPedidoAntigua - cantidadEnPedido;
        Cursor cursorUnidadesEnStock  = myDBHelper.fetchUnidadesEnStockFromProductosByProductoId(strIdProductoEnPedido);
        int unidadesEnStockAntigua = cursorUnidadesEnStock.getInt(cursorUnidadesEnStock.getColumnIndex(cursorUnidadesEnStock.getColumnName(0)));
        int unidadesEnStockActuales = unidadesEnStockAntigua+cantidadEnPedido;
        final String where = "_id" + "=?";
        final String[] whereArgs = new String[]{strIdProductoEnPedido};
        valoresUpDateProductos.put("UnidadesEnStock",unidadesEnStockActuales);
        valoresUpDateProductos.put("UndadesEnPedido",unidadesEnPedidoActuales);
        dbDelete.update("Productos",valoresUpDateProductos, where, whereArgs);

        if(intNumeroDeLineas==1){
            dbDelete.execSQL("DELETE FROM PedidosDetalles WHERE PedidoId="+pedidoId);
            dbDelete.execSQL("DELETE FROM Pedidos WHERE PedidoID="+pedidoId);
            Toast.makeText(context,"Se borro la linea del pedido y se borro el pedido: "+pedidoId,Toast.LENGTH_LONG).show();
        }else{
            dbDelete.execSQL("DELETE FROM PedidosDetalles WHERE productoId="+strIdProductoEnPedido+" AND PedidoId="+pedidoId);
            Toast.makeText(context,"Se borro la linea del pedido: "+pedidoId,Toast.LENGTH_LONG).show();
        }
        myDBHelper.close();
    }

    public void onDestroyEdit(){
        Intent intent = new Intent(context, PedidosActivity.class);
        ((ViewGroup)viewEditarPedido.getParent()).removeView(viewEditarPedido);
        startActivity(intent);
    }
    public void onDestroyDelete(){
        Intent intent = new Intent(context, PedidosActivity.class);
        startActivity(intent);
    }

    public void onDestroyEditAndDeleteMoreLines(){
        Intent intent = new Intent(context, PedidosActivity.class);
        startActivity(intent);

    }
    public void onDestroyDeleteMoreLines(){
        ((ViewGroup)viewBorrarPedido.getParent()).removeView(viewBorrarPedido);
        builderBorrarLineasDelPedido.show();
    }
    public void cargarDatosDelete(View view){
        TextView tvNumeroPedido = (TextView)view.findViewById(R.id.tv_numeroPedido);
        TextView tvClienteIDPedido = (TextView)view.findViewById(R.id.tv_clienteIDPedido);
        TextView tvFechaPedido = (TextView)view.findViewById(R.id.tv_fechaPedido);
        TextView tvFechaRequerida = (TextView)view.findViewById(R.id.tv_fechaRequerida);
        TextView tvNombreCompañiaPedido = (TextView)view.findViewById(R.id.tv_nombre_compañiaPedido);
        TextView tvLineasPedido = (TextView)view.findViewById(R.id.tv_LineasDelPedido);
        final TextView tvCantidadEnPedido = (TextView)view.findViewById(R.id.tv_CantidadEnPedido);
        spiNombreProductoPedidoBorrar = (Spinner)view.findViewById(R.id.spi_nombreProductoEnPedido);
        myDBHelper.openDatabase();
        Cursor cursorProductoIdFromPedidosDetalles = myDBHelper.fetchAllProductoIdForPedidosDetallesPedidoId(pedidoId);
        SimpleCursorAdapter adapterProductoIdFromPedidosDetalles = new SimpleCursorAdapter(getApplicationContext(),
                android.R.layout.select_dialog_item,cursorProductoIdFromPedidosDetalles,new String[] {"productoId"},
                new int[] {android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        adapterProductoIdFromPedidosDetalles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiNombreProductoPedidoBorrar.setAdapter(adapterProductoIdFromPedidosDetalles);
        spiNombreProductoPedidoBorrar.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
        myDBHelper.close();
        spiNombreProductoPedidoBorrar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                if (parent.getId() == R.id.spi_nombreProductoEnPedido) {
                    TextView tvNombreProducto = (TextView)spiNombreProductoPedidoBorrar.getSelectedView();
                    strIdProductoEnPedido = tvNombreProducto.getText().toString();

                    myDBHelper.openDatabase();
                    Cursor idProductoCursor= myDBHelper.fetch_idNombresProducto(strIdProductoEnPedido);

                    if(idProductoCursor!=null){
                        strNombreProductoEnPedido = idProductoCursor.getString(idProductoCursor.getColumnIndex(idProductoCursor.getColumnName(0)));
                        tvNombreProducto.setText(strNombreProductoEnPedido);
                        Cursor cursorCantidadProductoEnPedido = myDBHelper.fetchCantidadByPedidoId(strIdProductoEnPedido,pedidoId);
                        cantidadEnPedido = cursorCantidadProductoEnPedido.getInt(cursorCantidadProductoEnPedido.getColumnIndex(cursorCantidadProductoEnPedido.getColumnName(1)));
                        tvCantidadEnPedido.setText(String.valueOf(cantidadEnPedido));
                    }
                    myDBHelper.close();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        myDBHelper.openDatabase();
        myCursor= myDBHelper.fetchPedidoById(pedidoId);

        if (myCursor!=null){
            intNumeroPedido = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(1))));
            strClienteID = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
            Cursor cursorNombreCompañia= myDBHelper.fetchDatosClienteByClienteID(strClienteID);
            strNombreCompañia = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(0))));
            strFechaPedido= (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
            strFechaRequerida = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(5))));
        }
        Cursor cursorNumeroDeLineas = myDBHelper.fetchLineasDePedido(pedidoId);
        if(cursorNumeroDeLineas!=null){
            intNumeroDeLineas = cursorNumeroDeLineas.getInt(cursorNumeroDeLineas.getColumnIndex(cursorNumeroDeLineas.getColumnName(0)));
        }

        myDBHelper.close();

        tvNumeroPedido.setText(pedidoId);
        tvClienteIDPedido.setText(strNombreCompañia);
        tvFechaPedido.setText(strFechaPedido);
        tvFechaRequerida.setText(strFechaRequerida);
        tvNombreCompañiaPedido.setText(strNombreCompañia);
        tvLineasPedido.setText(String.valueOf(intNumeroDeLineas));

    }
 }

