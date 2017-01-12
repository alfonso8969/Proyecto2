package alfonso8969.com.proyecto2.fragments.pedidos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import java.io.IOException;
import java.util.Calendar;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.pedidos.PedidosActivity;
import alfonso8969.com.proyecto2.adapters.PedidosListaAdapter;
import alfonso8969.com.proyecto2.clases.RecyclerItemClickListener;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;
/**
 * Creado por alfonso en fecha 16/11/2016.
 */

public class PedidosListaFragment extends Fragment {

    private RecyclerView recyclerViewPedidos;
    private PedidosListaAdapter pedidosListaAdapter;
    private DataBaseHelper myDBHelper;
    private static final String TAG = "RecyclerViewPedidosFragment";
    private TextView tvNombreCompañiaPedido,tvDireccionClientePedido,tvNumeroPedido;
    private static TextView tvFechaPedido,tvFechaRequerida,tvFechaEntrega;
    public TextView tvPrecioPorUnidad,tvUnidadesEnStock,tvUnidadesEnPedido,tvPrecioFlete,tvPrecioTotal,delete,textViewNumPedido;
    private Spinner spiClienteÏd,spiVIaEnvio,spiNombreEmpleadoPedido,spiNombreProducto, spiDescuentoAgregarLineas;
    private Button btn_fechaPedido,btn_fechaRequerida,btn_fechaEntrega;
    private EditText edtCantidad,edtCantidadAgregarLineas,edtCantidadAgregarLineasAExistente;
    public String pedidoId,strNumeroPedido,strPedidoIDAgregarAExistente,strClienteId,strNombreCompañia,strDireccion,strNombreCompañiaEnvio,strNombreEmpleado,strCantidad,strCantidadAgregarLineas,direcCoPos;
    public String strNombreProducto,strCodPostal,strRegion,strCiudad,strPais,strNombreCliente,strNumPedidosPendientesDeEntrega;
    public static String strFechaPedido,strFechaRequerida,strFechaEntrega;
    public int intUltimoPedido,intSigPedido,intIdEmpleado,intIdNombreCompañia,intIdProducto,intCantidad,intUnidadesEnPedido,intUnidadesEnStock,intIdProductoAgregarLineasAExixtente;
    public double doublePrecioPorUnidad,doubleDescuento,doublePrecioFlete,doublePrecioTotal;
    public FloatingActionButton fb;
    public ViewGroup containerPedido;
    public static LayoutInflater inflaterPedido;
    public View viewAgregarLineasPedidoNuevo, viewSeleccionarPedidoExistente,viewAgregaLineasAPedidoExistente;
    public Builder builderAgregarLineasPedidoNuevo,builderAgregarLineasAExistente;
    public Spinner spiNumeroPedido;

    public interface CallBacksPedidos {
        void onItemSelected(String pedidoId);
    }

    public interface RefreshPedidos {
        void refreshList();
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        final View viewRoot = inflater.inflate(R.layout.lista_pedidos_frgment, container, false);
        viewRoot.setTag(TAG);
        containerPedido = container;
        inflaterPedido = inflater;
        recyclerViewPedidos = (RecyclerView) viewRoot.findViewById(R.id.lista_Pedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        fb = (FloatingActionButton)viewRoot.findViewById(R.id.fbPedidos);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Builder builderNuevoExistente = new Builder(getActivity());
                builderNuevoExistente.setTitle("Agregar un pedido")
                .setMessage("Que desea hacer?"+"\n"+"Agregar nuevo pedido (pulse Nuevo)"+"\n"+"Añadir lineas a existente (pulse Agregar a existente)")
                .setPositiveButton("Nuevo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        myDBHelper = new DataBaseHelper(getActivity().getApplicationContext());
                        onCreateView(inflater, container, saveInstanceState);
                        final View viewAgregarPedidoNuevo = inflater.inflate(R.layout.insertar_pedidonuevo, container, false);

                        myDBHelper = new DataBaseHelper(getActivity().getApplicationContext());

                        tvNumeroPedido = (TextView) viewAgregarPedidoNuevo.findViewById(R.id.tv_numeroPedido);

                        myDBHelper.openDatabase();
                        Cursor cursorUltimoPedido = myDBHelper.fetchLastPedidoID();
                        Cursor cursorClienteId = myDBHelper.fetchAllClienteID();
                        myDBHelper.close();
                        intUltimoPedido = cursorUltimoPedido.getInt(cursorUltimoPedido.getColumnIndex(cursorUltimoPedido.getColumnName(0)));
                        intSigPedido = intUltimoPedido + 1;
                        tvNumeroPedido.setText(String.valueOf(intSigPedido));

                        spiClienteÏd = (Spinner) viewAgregarPedidoNuevo.findViewById(R.id.spi_clienteIDPedido);
                        SimpleCursorAdapter adapterClienteID = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                android.R.layout.select_dialog_item, cursorClienteId, new String[]{"ClienteID"},
                                new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                        adapterClienteID.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiClienteÏd.setAdapter(adapterClienteID);
                        spiClienteÏd.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                        spiClienteÏd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                if (parent.getId() == R.id.spi_clienteIDPedido) {
                                    TextView textView = (TextView) spiClienteÏd.getSelectedView();
                                    strClienteId = textView.getText().toString();
                                    myDBHelper.openDatabase();
                                    Cursor cursorDatosCliente = myDBHelper.fetchDatosClienteByClienteID(strClienteId);
                                    myDBHelper.close();
                                    if (cursorDatosCliente != null) {
                                        strNombreCompañia = cursorDatosCliente.getString(cursorDatosCliente.getColumnIndex(cursorDatosCliente.getColumnName(0)));
                                        strDireccion = cursorDatosCliente.getString(cursorDatosCliente.getColumnIndex(cursorDatosCliente.getColumnName(1)));
                                        strCiudad = cursorDatosCliente.getString(cursorDatosCliente.getColumnIndex(cursorDatosCliente.getColumnName(2)));
                                        strRegion = cursorDatosCliente.getString(cursorDatosCliente.getColumnIndex(cursorDatosCliente.getColumnName(3)));
                                        strCodPostal = cursorDatosCliente.getString(cursorDatosCliente.getColumnIndex(cursorDatosCliente.getColumnName(4)));
                                        strPais = cursorDatosCliente.getString(cursorDatosCliente.getColumnIndex(cursorDatosCliente.getColumnName(5)));
                                        tvNombreCompañiaPedido = (TextView) viewAgregarPedidoNuevo.findViewById(R.id.tv_nombre_compañiaPedido);
                                        tvNombreCompañiaPedido.setText(strNombreCompañia);
                                        tvDireccionClientePedido = (TextView) viewAgregarPedidoNuevo.findViewById(R.id.tv_direccion_clientePedido);
                                        direcCoPos = strDireccion + "---" + strCodPostal;
                                        tvDireccionClientePedido.setText(direcCoPos);
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        tvFechaPedido = (TextView) viewAgregarPedidoNuevo.findViewById(R.id.tv_fechaPedido);
                        final Calendar calendar = Calendar.getInstance();
                        int yy = calendar.get(Calendar.YEAR);
                        int mm = calendar.get(Calendar.MONTH);
                        int dd = calendar.get(Calendar.DAY_OF_MONTH);
                        tvFechaPedido.setText(new StringBuilder()
                                .append(dd).append("/")
                                .append(mm + 1).append("/")
                                .append(yy).append(" "));
                        strFechaPedido = tvFechaPedido.getText().toString();
                        btn_fechaPedido = (Button) viewAgregarPedidoNuevo.findViewById(R.id.btn_fechaPedido);
                        btn_fechaPedido.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                DialogFragment newFragment = new SelectDateFragmentFechaPedido();
                                newFragment.show(getFragmentManager(), "DatePicker");

                            }
                        });

                        tvFechaRequerida = (TextView)viewAgregarPedidoNuevo.findViewById(R.id.tv_fechaRequerida);
                        btn_fechaRequerida = (Button)viewAgregarPedidoNuevo.findViewById(R.id.btn_fechaRequerida);
                        btn_fechaRequerida.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                DialogFragment newFragment = new SelectDateFragmentFechaRequerida();
                                newFragment.show(getFragmentManager(), "DatePicker");
                            }
                        });

                        tvFechaEntrega = (TextView) viewAgregarPedidoNuevo.findViewById(R.id.tv_fechaEntrega);
                        btn_fechaEntrega = (Button) viewAgregarPedidoNuevo.findViewById(R.id.btn_fechaEntrega);
                        btn_fechaEntrega.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                DialogFragment newFragment = new SelectDateFragmentFechaEntrega();
                                newFragment.show(getFragmentManager(), "DatePicker");

                            }
                        });

                        spiVIaEnvio = (Spinner)viewAgregarPedidoNuevo.findViewById(R.id.spi_Via_Envio);
                        myDBHelper.openDatabase();
                        Cursor cursorNombreCompañiaEnvio = myDBHelper.fetchAllNombresCompañiaEnvio();
                        myDBHelper.close();
                        SimpleCursorAdapter adapterNombreCompañiaEnvio = new SimpleCursorAdapter(getActivity().getApplicationContext(),
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

                        spiNombreEmpleadoPedido = (Spinner)viewAgregarPedidoNuevo.findViewById(R.id.spi_nombreEmpleadoPedido);
                        myDBHelper.openDatabase();
                        Cursor cursorNombreEmpleado = myDBHelper.fetchAllNombresEmpleados();
                        myDBHelper.close();
                        SimpleCursorAdapter adapterNombreEmpleado = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                android.R.layout.select_dialog_item,cursorNombreEmpleado,new String[]{"Nombre","Apellido"},
                                new int[]{android.R.id.text1},SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                        adapterNombreEmpleado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiNombreEmpleadoPedido.setAdapter(adapterNombreEmpleado);
                        spiNombreEmpleadoPedido.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                        spiNombreEmpleadoPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                if(parent.getId()== R.id.spi_nombreEmpleadoPedido){
                                    TextView textView = (TextView)spiNombreEmpleadoPedido.getSelectedView();
                                    strNombreEmpleado = textView.getText().toString();
                                    myDBHelper.openDatabase();
                                    Cursor cursorIdEmpleado = myDBHelper.fetchIdEmpleadoByNombre(strNombreEmpleado);
                                    myDBHelper.close();
                                    if(cursorIdEmpleado!=null){
                                        intIdEmpleado = cursorIdEmpleado.getInt(cursorIdEmpleado.getColumnIndex(cursorIdEmpleado.getColumnName(0)));
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        spiNombreProducto = (Spinner)viewAgregarPedidoNuevo.findViewById(R.id.spi_nombreProducto);
                        myDBHelper.openDatabase();
                        Cursor cursorNombreProducto = myDBHelper.fetchAllNombresProductos();
                        myDBHelper.close();
                        SimpleCursorAdapter adapterNombreProducto = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                android.R.layout.select_dialog_item,cursorNombreProducto,new String[]{"NombreProducto"},
                                new int[]{android.R.id.text1},SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                        adapterNombreProducto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiNombreProducto.setAdapter(adapterNombreProducto);
                        spiNombreProducto.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                        spiNombreProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                if(parent.getId()== R.id.spi_nombreProducto){
                                    TextView textView = (TextView)spiNombreProducto.getSelectedView();
                                    strNombreProducto = textView.getText().toString();
                                    myDBHelper.openDatabase();
                                    Cursor cursorIdProducto = myDBHelper.fetchIdProductoByNombreProducto(strNombreProducto);
                                    Cursor cursorNombreProducto = myDBHelper.fetchDatosProductosByNombreProducto(strNombreProducto);
                                    myDBHelper.close();
                                    if(cursorIdProducto!=null){
                                        intIdProducto = cursorIdProducto.getInt(cursorIdProducto.getColumnIndex(cursorIdProducto.getColumnName(0)));
                                    }
                                    tvPrecioPorUnidad = (TextView)viewAgregarPedidoNuevo.findViewById(R.id.tv_PrecioPorUnidad);
                                    tvUnidadesEnPedido = (TextView)viewAgregarPedidoNuevo.findViewById(R.id.tv_UnidadesEnPedido);
                                    tvUnidadesEnStock = (TextView)viewAgregarPedidoNuevo.findViewById(R.id.tv_UnidadesEnStock);
                                    if (cursorNombreProducto!=null){
                                        doublePrecioPorUnidad = cursorNombreProducto.getDouble(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(0)));
                                        intUnidadesEnPedido = cursorNombreProducto.getInt(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(1)));
                                        intUnidadesEnStock = cursorNombreProducto.getInt(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(2)));
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

                        tvPrecioFlete = (TextView) viewAgregarPedidoNuevo.findViewById(R.id.tv_PrecioFlete);
                        tvPrecioTotal = (TextView)viewAgregarPedidoNuevo.findViewById(R.id.tv_PrecioTotalPorLinea);

                        spiDescuentoAgregarLineas = (Spinner)viewAgregarPedidoNuevo.findViewById(R.id.spi_descuento);
                        final Double[] datosDescuento= new Double[]{0.0,0.05,0.1,0.15,0.20,0.25,0.3,0.40};
                        ArrayAdapter<Double> adapterDescuento = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, datosDescuento);
                        adapterDescuento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiDescuentoAgregarLineas.setAdapter(adapterDescuento);
                        spiDescuentoAgregarLineas.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                        spiDescuentoAgregarLineas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                if(parent.getId()==R.id.spi_descuento){
                                    TextView textView = (TextView) spiDescuentoAgregarLineas.getSelectedView();
                                    doubleDescuento = Double.valueOf(textView.getText().toString());
                                    edtCantidad = (EditText)viewAgregarPedidoNuevo.findViewById(R.id.edt_Cantidad);
                                    strCantidad = edtCantidad.getText().toString();

                                    if (!strCantidad.isEmpty()) {
                                        intCantidad = Integer.parseInt(strCantidad);

                                        if(intCantidad > intUnidadesEnStock){
                                            Builder builderCantidadSupUniEnStock = new Builder(getActivity());
                                            builderCantidadSupUniEnStock.setTitle("Error en Cantidad")
                                            .setMessage("La cantidad del pedido no puede superar las unidades en Stock")
                                            .setPositiveButton("Aceptar",null)
                                            .show();
                                            edtCantidad.setText("");
                                        }

                                    doublePrecioFlete = (intCantidad * doublePrecioPorUnidad * 20) / 100;
                                    doublePrecioTotal = ((intCantidad * doublePrecioPorUnidad) - (intCantidad * doublePrecioPorUnidad * doubleDescuento));
                                    tvPrecioFlete.setText(String.format("%s €", String.valueOf(doublePrecioFlete)));
                                    tvPrecioTotal.setText(String.format("%s €", String.valueOf(doublePrecioTotal)));
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        final Builder builderAgregarPedidoNuevo = new Builder(getActivity());
                        builderAgregarPedidoNuevo.setTitle("Agregar un pedido")
                        .setView(viewAgregarPedidoNuevo);
                        builderAgregarPedidoNuevo.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                    if (strCantidad.isEmpty()|| Integer.valueOf(strCantidad)==0){
                        Builder builderCantidadCero = new Builder(getActivity());
                        builderCantidadCero.setTitle("Error en Cantidad")
                        .setMessage("La cantidad del pedido no puede ser cero")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((ViewGroup)viewAgregarPedidoNuevo.getParent()).removeView(viewAgregarPedidoNuevo);
                                builderAgregarPedidoNuevo.show();
                                edtCantidad.setText("");
                                edtCantidad.requestFocus();
                            }
                        })
                        .show();

                    }else if(tvFechaRequerida.getText().toString().isEmpty()){
                        Builder builderFechasErr = new Builder(getActivity());
                        builderFechasErr.setTitle("Error en Fechas")
                        .setMessage("Revise las fechas del pedido\nFecha Requerida no puede estar vacia")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((ViewGroup)viewAgregarPedidoNuevo.getParent()).removeView(viewAgregarPedidoNuevo);
                                builderAgregarPedidoNuevo.show();
                                tvFechaRequerida.requestFocus();
                            }
                        })
                        .show();

                    }else{
                        myDBHelper.openDatabase();
                        SQLiteDatabase db = myDBHelper.getWritableDatabase();
                        ContentValues valoresPedidos = new ContentValues();
                        ContentValues valoresPedidosDetalles = new ContentValues();
                        ContentValues valoresUpDateProductos = new ContentValues();

                        valoresPedidos.put("PedidoId", intSigPedido);
                        valoresPedidos.put("ClienteId", strClienteId);
                        valoresPedidos.put("EmpleadoID", intIdEmpleado);
                        valoresPedidos.put("FechaPedido", strFechaPedido);
                        valoresPedidos.put("FechaRequerida", strFechaRequerida);
                        valoresPedidos.put("FechaEntrega", strFechaEntrega);
                        valoresPedidos.put("ViaEnvio",intIdNombreCompañia);
                        valoresPedidos.put("PrecioFlete",doublePrecioFlete);
                        valoresPedidos.put("ClienteReceptorEnvio",strNombreCompañia);
                        valoresPedidos.put("DireccionEnvio",strDireccion);
                        valoresPedidos.put("CiudadEnvio",strCiudad);
                        valoresPedidos.put("RegionEnvio",strRegion);
                        valoresPedidos.put("CodigoPostalEnvio",strCodPostal);
                        valoresPedidos.put("PaisEnvio",strPais);


                        valoresPedidosDetalles.put("PedidoId",intSigPedido);
                        valoresPedidosDetalles.put("productoId", intIdProducto);
                        valoresPedidosDetalles.put("PrecioUnidad", doublePrecioPorUnidad);
                        valoresPedidosDetalles.put("Cantidad", intCantidad);
                        valoresPedidosDetalles.put("Descuento", doubleDescuento);

                        if(db.insert("PedidosDetalles", null, valoresPedidosDetalles)!=0){
                            Toast.makeText(getContext(),"Se insertaron los valores en Detalles",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(),"No se insertaron los valores en Detalles",Toast.LENGTH_LONG).show();
                        }


                        Cursor cursorUnidadesEnPedido = myDBHelper.fetchUnidadesEnPedidoFromProductosByProductoId(String.valueOf(intIdProducto));
                        int unidadesEnPedidoAntigua = cursorUnidadesEnPedido.getInt(cursorUnidadesEnPedido.getColumnIndex(cursorUnidadesEnPedido.getColumnName(0)));
                        int unidadesEnPedidoActuales = unidadesEnPedidoAntigua+intCantidad;
                        Cursor cursorUnidadesEnStock  = myDBHelper.fetchUnidadesEnStockFromProductosByProductoId(String.valueOf(intIdProducto));
                        int unidadesEnStockAntigua = cursorUnidadesEnStock.getInt(cursorUnidadesEnStock.getColumnIndex(cursorUnidadesEnStock.getColumnName(0)));
                        int unidadesEnStockActuales = unidadesEnStockAntigua-intCantidad;
                        final String where = "_id" + "=?";
                        final String[] whereArgs = new String[]{String.valueOf(intIdProducto)};
                        valoresUpDateProductos.put("UnidadesEnStock",unidadesEnStockActuales);
                        valoresUpDateProductos.put("UndadesEnPedido",unidadesEnPedidoActuales);
                        db.update("Productos",valoresUpDateProductos, where, whereArgs);

                        try {

                            if (db.insert("Pedidos", null, valoresPedidos) != 0) {

                                Builder builderInsertPedidoSuccess = new Builder(getActivity());
                                builderInsertPedidoSuccess.setTitle("Nuevo pedido")
                                .setMessage("Se agregó el pedido: " + intSigPedido)
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onDestroyInsert();
                                    }
                                })
                                .setNegativeButton("Mas lineas?",  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getContext(),"Se agregó el pedido: "+intSigPedido,Toast.LENGTH_LONG).show();
                                        onCreateView(inflater, container, saveInstanceState);
                                        viewAgregarLineasPedidoNuevo = inflater.inflate(R.layout.insertarlineas_pedido, container, false);
                                        TextView tvNumeroPedido = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_numeroPedido);
                                        TextView tvClienteId = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_clienteIDPedido);
                                        TextView tvFechaPedido = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_fechaPedido);
                                        TextView tvFechaRequerida = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_fechaRequerida);
                                        TextView tvFechaEntrega = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_fechaEntrega);
                                        TextView tvNombreCompañia = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_nombre_compañiaPedido);
                                        TextView tvDireccionClientePedido = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_direccion_clientePedido);
                                        TextView tvViaEnvio = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_Via_Envio);
                                        TextView tvNombreEmpleadoPedido = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_nombreEmpleadoPedido);
                                        
                                        tvNumeroPedido.setText(String.valueOf(intSigPedido));
                                        tvClienteId.setText(strClienteId);
                                        tvFechaPedido.setText(strFechaPedido);
                                        tvFechaRequerida.setText(strFechaRequerida);
                                        tvFechaEntrega.setText(strFechaEntrega);
                                        tvNombreCompañia.setText(strNombreCompañia);
                                        tvDireccionClientePedido.setText(direcCoPos);
                                        tvViaEnvio.setText(strNombreCompañiaEnvio);
                                        tvNombreEmpleadoPedido.setText(strNombreEmpleado);
                                        myDBHelper.openDatabase();
                                        Cursor cursorNombreProducto = myDBHelper.fetchAllNombresProductos();
                                        myDBHelper.close();
                                        spiNombreProducto = (Spinner)viewAgregarLineasPedidoNuevo.findViewById(R.id.spi_nombreProducto);
                                        SimpleCursorAdapter adapterNombreProductoLineas = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                                android.R.layout.select_dialog_item,cursorNombreProducto,new String[]{"NombreProducto"},
                                                new int[]{android.R.id.text1},SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                                        adapterNombreProductoLineas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spiNombreProducto.setAdapter(adapterNombreProductoLineas);
                                        spiNombreProducto.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                                        spiNombreProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                                if(parent.getId()== R.id.spi_nombreProducto){
                                                    TextView textView = (TextView)spiNombreProducto.getSelectedView();
                                                    strNombreProducto = textView.getText().toString();
                                                    myDBHelper.openDatabase();
                                                    Cursor cursorIdProducto = myDBHelper.fetchIdProductoByNombreProducto(strNombreProducto);
                                                    Cursor cursorNombreProducto = myDBHelper.fetchDatosProductosByNombreProducto(strNombreProducto);
                                                    myDBHelper.close();
                                                    if(cursorIdProducto!=null){
                                                        intIdProducto = cursorIdProducto.getInt(cursorIdProducto.getColumnIndex(cursorIdProducto.getColumnName(0)));
                                                    }
                                                    tvPrecioPorUnidad = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_PrecioPorUnidad);
                                                    tvUnidadesEnPedido = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_UnidadesEnPedido);
                                                    tvUnidadesEnStock = (TextView)viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_UnidadesEnStock);
                                                    if (cursorNombreProducto!=null){
                                                        doublePrecioPorUnidad = cursorNombreProducto.getDouble(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(0)));
                                                        intUnidadesEnPedido = cursorNombreProducto.getInt(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(1)));
                                                        intUnidadesEnStock = cursorNombreProducto.getInt(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(2)));
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

                                        tvPrecioFlete = (TextView) viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_PrecioFlete);
                                        tvPrecioTotal = (TextView) viewAgregarLineasPedidoNuevo.findViewById(R.id.tv_PrecioTotalPorLinea);

                                        spiDescuentoAgregarLineas = (Spinner)viewAgregarLineasPedidoNuevo.findViewById(R.id.spi_descuento);
                                        final Double[] datosDescuento= new Double[]{0.0,0.05,0.1,0.15,0.20,0.25,0.3,0.40};
                                        ArrayAdapter<Double> adapterDescuento = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, datosDescuento);
                                        adapterDescuento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spiDescuentoAgregarLineas.setAdapter(adapterDescuento);
                                        spiDescuentoAgregarLineas.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                                        spiDescuentoAgregarLineas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                                if(parent.getId()==R.id.spi_descuento){
                                                    TextView textView = (TextView) spiDescuentoAgregarLineas.getSelectedView();
                                                    doubleDescuento = Double.valueOf(textView.getText().toString());
                                                    edtCantidadAgregarLineas = (EditText)viewAgregarLineasPedidoNuevo.findViewById(R.id.edt_Cantidad);
                                                    strCantidadAgregarLineas = edtCantidadAgregarLineas.getText().toString();

                                                    if (!strCantidadAgregarLineas.isEmpty()) {
                                                        intCantidad = Integer.parseInt(strCantidadAgregarLineas);

                                                        if(intCantidad > intUnidadesEnStock){
                                                            Builder builderCantidadSupUniEnStock = new Builder(getActivity());
                                                            builderCantidadSupUniEnStock.setTitle("Error en Cantidad")
                                                                    .setMessage("La cantidad del pedido no puede superar las unidades en Stock")
                                                                    .setPositiveButton("Aceptar",null)
                                                                    .show();
                                                            edtCantidadAgregarLineas.setText("");
                                                        }

                                                        doublePrecioFlete = (intCantidad * doublePrecioPorUnidad * 20) / 100;
                                                        doublePrecioTotal = ((intCantidad * doublePrecioPorUnidad) - (intCantidad * doublePrecioPorUnidad * doubleDescuento));
                                                        tvPrecioFlete.setText(String.format("%s €", String.valueOf(doublePrecioFlete)));
                                                        tvPrecioTotal.setText(String.format("%s €", String.valueOf(doublePrecioTotal)));
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });

                                        builderAgregarLineasPedidoNuevo = new Builder(getActivity());
                                        builderAgregarLineasPedidoNuevo.setView(viewAgregarLineasPedidoNuevo);
                                        builderAgregarLineasPedidoNuevo.setTitle("Agregar lineas al pedido")
                                        .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                             agregarLineasAPedidoNuevo();
                                             onDestroyInsert();
                                            }
                                        })
                                        .setNegativeButton("Cancelar",null)
                                        .setNeutralButton("Mas lineas", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                agregarLineasAPedidoNuevo();
                                                ((ViewGroup)viewAgregarLineasPedidoNuevo.getParent()).removeView(viewAgregarLineasPedidoNuevo);
                                                edtCantidadAgregarLineas.setText("");
                                                builderAgregarLineasPedidoNuevo.show();
                                            }
                                        });
                                        builderAgregarLineasPedidoNuevo.show();

                                    }
                                })
                                .show();
                            }

                        } catch (SQLException e) {
                            Builder builderInsertPedidoErr = new Builder(getActivity());
                            builderInsertPedidoErr.setTitle("Error  al insertar pedido")
                            .setMessage(e.getMessage())
                            .setNegativeButton("Aceptar", null)
                            .show();
                        }
                    }
          }
                        })
                        .setNegativeButton("Cancelar", null)
                        .setIcon(R.drawable.pedido)
                        .show();
                    }
                });
                builderNuevoExistente.setNegativeButton("Agregar a existente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onCreateView(inflater, container, saveInstanceState);
                        viewSeleccionarPedidoExistente = inflater.inflate(R.layout.seleccionar_pedidodexistente, container, false);
                        spiNumeroPedido = (Spinner) viewSeleccionarPedidoExistente.findViewById(R.id.spi_numeroPedido);
                        final Spinner spiClienteIdLineasExistente = (Spinner) viewSeleccionarPedidoExistente.findViewById(R.id.spi_clienteIDPedido);

                        myDBHelper.openDatabase();
                        Cursor cursorClienteId = myDBHelper.fetchAllClienteID();
                        myDBHelper.close();
                        SimpleCursorAdapter adapterClienteID = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                android.R.layout.select_dialog_item, cursorClienteId, new String[]{"ClienteID"},
                                new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                        adapterClienteID.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spiClienteIdLineasExistente.setAdapter(adapterClienteID);
                        spiClienteIdLineasExistente.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                        spiClienteIdLineasExistente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                if (parent.getId() == R.id.spi_clienteIDPedido) {
                                    final TextView textViewClienteId = (TextView) spiClienteIdLineasExistente.getSelectedView();
                                    String strClienteId = textViewClienteId.getText().toString();
                                    myDBHelper.openDatabase();
                                    Cursor cursorPedidosCliente = myDBHelper.fetchAllPedidosClienteByClienteIDAndFechaEntregaNull(strClienteId);
                                    Cursor cursorNumPedidosCliente = myDBHelper.fetchAllNumOfPedidosClienteByClienteIDAndFechaEntregaNull(strClienteId);
                                    Cursor cursorNombreCliente = myDBHelper.fetchDatosClienteByClienteID(strClienteId);
                                    myDBHelper.close();
                                    strNombreCliente = cursorNombreCliente.getString(cursorNombreCliente.getColumnIndex(cursorNombreCliente.getColumnName(0)));
                                    TextView tvMensage = (TextView) viewSeleccionarPedidoExistente.findViewById(R.id.tv_mensage);
                                    if (cursorPedidosCliente.getCount() != 0) {
                                        strNumPedidosPendientesDeEntrega = cursorNumPedidosCliente.getString(cursorNumPedidosCliente.getColumnIndex(cursorNumPedidosCliente.getColumnName(0)));
                                        String message;
                                        if(Integer.valueOf(strNumPedidosPendientesDeEntrega)>1)
                                            message =  " pedidos pendientes de entrega";
                                        else
                                            message =  " pedido pendiente de entrega";
                                        tvMensage.setText("El Cliente: " + strNombreCliente + " tiene " + strNumPedidosPendientesDeEntrega +message);
                                        SimpleCursorAdapter adapterPedidosCliente = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                                android.R.layout.select_dialog_item, cursorPedidosCliente, new String[]{"PedidoID"},
                                                new int[]{android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                                        adapterPedidosCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spiNumeroPedido.setAdapter(adapterPedidosCliente);
                                        spiNumeroPedido.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                                        spiNumeroPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                                if(parent.getId()==R.id.spi_numeroPedido){
                                                    textViewNumPedido = (TextView) spiNumeroPedido.getSelectedView();
                                                    strPedidoIDAgregarAExistente = textViewNumPedido.getText().toString();
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });
                                    }else{
                                        TextView textView  = (TextView) spiNumeroPedido.getSelectedView();
                                        textView.setBackgroundColor(Color.WHITE);
                                        textView.setText("");
                                        tvMensage.setText("El Cliente: "+strNombreCliente+"\ntiene todos los pedidos entregados");
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        Builder builderAgregarLineasPedidoExistente = new Builder(getActivity());
                        builderAgregarLineasPedidoExistente.setView(viewSeleccionarPedidoExistente)
                        .setTitle("Agregar lineas a pedido existente")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onCreateView(inflater, container, saveInstanceState);
                                viewAgregaLineasAPedidoExistente = inflater.inflate(R.layout.insertarlineas_pedido, container, false);
                                TextView tvNumeroPedido = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_numeroPedido);
                                TextView tvClienteId = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_clienteIDPedido);
                                TextView tvFechaPedido = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_fechaPedido);
                                TextView tvFechaRequerida = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_fechaRequerida);
                                TextView tvFechaEntrega = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_fechaEntrega);
                                TextView tvNombreCompañia = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_nombre_compañiaPedido);
                                TextView tvDireccionClientePedido = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_direccion_clientePedido);
                                TextView tvViaEnvio = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_Via_Envio);
                                TextView tvNombreEmpleadoPedido = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_nombreEmpleadoPedido);

                                tvNumeroPedido.setText(strPedidoIDAgregarAExistente);

                                myDBHelper.openDatabase();
                                Cursor cursorDatosPedido = myDBHelper.fetchPedidoById(strPedidoIDAgregarAExistente);
                                Cursor cursorNombreProducto = myDBHelper.fetchAllNombresProductos();
                                myDBHelper.close();

                                tvClienteId.setText(cursorDatosPedido.getString(cursorDatosPedido.getColumnIndex(cursorDatosPedido.getColumnName(2))));
                                String strIdEmpleado = cursorDatosPedido.getString(cursorDatosPedido.getColumnIndex(cursorDatosPedido.getColumnName(3)));
                                myDBHelper.openDatabase();
                                Cursor cursorNombreEmpleado = myDBHelper.fetchEmpleadoById(strIdEmpleado);
                                String strNombreEmpleado = cursorNombreEmpleado.getString(cursorNombreEmpleado.getColumnIndex(cursorNombreEmpleado.getColumnName(1)));
                                String strApellidoEmpleado = cursorNombreEmpleado.getString(cursorNombreEmpleado.getColumnIndex(cursorNombreEmpleado.getColumnName(2)));
                                tvNombreEmpleadoPedido.setText(strApellidoEmpleado+", "+strNombreEmpleado);
                                tvFechaPedido.setText(cursorDatosPedido.getString(cursorDatosPedido.getColumnIndex(cursorDatosPedido.getColumnName(4))));
                                tvFechaRequerida.setText(cursorDatosPedido.getString(cursorDatosPedido.getColumnIndex(cursorDatosPedido.getColumnName(5))));
                                tvFechaEntrega.setText(cursorDatosPedido.getString(cursorDatosPedido.getColumnIndex(cursorDatosPedido.getColumnName(6))));
                                String strIdViaEnvio=cursorDatosPedido.getString(cursorDatosPedido.getColumnIndex(cursorDatosPedido.getColumnName(7)));
                                Cursor cursorIdCompañiaEnvio = myDBHelper.fetchNombreCompañiaEnvioById(strIdViaEnvio);
                                myDBHelper.close();
                                String strNombreCompañia = cursorIdCompañiaEnvio.getString(cursorIdCompañiaEnvio.getColumnIndex(cursorIdCompañiaEnvio.getColumnName(0)));
                                tvViaEnvio.setText(strNombreCompañia);
                                tvNombreCompañia.setText(cursorDatosPedido.getString(cursorDatosPedido.getColumnIndex(cursorDatosPedido.getColumnName(9))));
                                tvDireccionClientePedido.setText(cursorDatosPedido.getString(cursorDatosPedido.getColumnIndex(cursorDatosPedido.getColumnName(10))));

                                final Spinner spiNombreProductoAgregarLineas = (Spinner)viewAgregaLineasAPedidoExistente.findViewById(R.id.spi_nombreProducto);
                                SimpleCursorAdapter adapterNombreProducto = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                                        android.R.layout.select_dialog_item,cursorNombreProducto,new String[]{"NombreProducto"},
                                        new int[]{android.R.id.text1},SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                                adapterNombreProducto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spiNombreProductoAgregarLineas.setAdapter(adapterNombreProducto);
                                spiNombreProductoAgregarLineas.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                                spiNombreProductoAgregarLineas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                        if(parent.getId()== R.id.spi_nombreProducto){
                                            TextView textView = (TextView)spiNombreProductoAgregarLineas.getSelectedView();
                                            strNombreProducto = textView.getText().toString();
                                            myDBHelper.openDatabase();
                                            Cursor cursorIdProducto = myDBHelper.fetchIdProductoByNombreProducto(strNombreProducto);
                                            Cursor cursorNombreProducto = myDBHelper.fetchDatosProductosByNombreProducto(strNombreProducto);
                                            myDBHelper.close();
                                            if(cursorIdProducto!=null){
                                                 intIdProductoAgregarLineasAExixtente = cursorIdProducto.getInt(cursorIdProducto.getColumnIndex(cursorIdProducto.getColumnName(0)));
                                            }
                                            tvPrecioPorUnidad = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_PrecioPorUnidad);
                                            tvUnidadesEnPedido = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_UnidadesEnPedido);
                                            tvUnidadesEnStock = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_UnidadesEnStock);
                                            if (cursorNombreProducto!=null){
                                                doublePrecioPorUnidad = cursorNombreProducto.getDouble(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(0)));
                                                intUnidadesEnPedido = cursorNombreProducto.getInt(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(1)));
                                                intUnidadesEnStock = cursorNombreProducto.getInt(cursorNombreProducto.getColumnIndex(cursorNombreProducto.getColumnName(2)));
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
                                final TextView tvPrecioFlete = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_PrecioFlete);
                                final TextView tvPrecioTotal = (TextView)viewAgregaLineasAPedidoExistente.findViewById(R.id.tv_PrecioTotalPorLinea);

                                final Spinner spiDescuentoAgregarLineasAExistente= (Spinner)viewAgregaLineasAPedidoExistente.findViewById(R.id.spi_descuento);
                                final Double[] datosDescuento= new Double[]{0.0,0.05,0.1,0.15,0.20,0.25,0.3,0.40};
                                ArrayAdapter<Double> adapterDescuento = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, datosDescuento);
                                adapterDescuento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spiDescuentoAgregarLineasAExistente.setAdapter(adapterDescuento);
                                spiDescuentoAgregarLineasAExistente.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                                spiDescuentoAgregarLineasAExistente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                                        if(parent.getId()==R.id.spi_descuento){
                                            TextView textView = (TextView) spiDescuentoAgregarLineasAExistente.getSelectedView();
                                            doubleDescuento = Double.valueOf(textView.getText().toString());
                                            edtCantidadAgregarLineasAExistente = (EditText)viewAgregaLineasAPedidoExistente.findViewById(R.id.edt_Cantidad);
                                            strCantidad = edtCantidadAgregarLineasAExistente.getText().toString();

                                            if (!strCantidad.isEmpty()) {
                                                intCantidad = Integer.parseInt(strCantidad);

                                                if(intCantidad > intUnidadesEnStock){
                                                    Builder builderCantidadSupUniEnStock = new Builder(getActivity());
                                                    builderCantidadSupUniEnStock.setTitle("Error en Cantidad")
                                                            .setMessage("La cantidad del pedido no puede superar las unidades en Stock")
                                                            .setPositiveButton("Aceptar",null)
                                                            .show();
                                                    edtCantidadAgregarLineasAExistente.setText("");
                                                }

                                                doublePrecioFlete = (intCantidad * doublePrecioPorUnidad * 20) / 100;
                                                doublePrecioTotal = ((intCantidad * doublePrecioPorUnidad) - (intCantidad * doublePrecioPorUnidad * doubleDescuento));
                                                tvPrecioFlete.setText(String.format("%s €", String.valueOf(doublePrecioFlete)));
                                                tvPrecioTotal.setText(String.format("%s €", String.valueOf(doublePrecioTotal)));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });

                                myDBHelper.close();

                                builderAgregarLineasAExistente = new Builder(getActivity());
                                builderAgregarLineasAExistente.setView(viewAgregaLineasAPedidoExistente)
                                        .setTitle("Agregar lineas a pedido existente")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                agregarLineasAPedidoExistente();
                                                onDestroyInsert();
                                            }
                                        })
                                        .setNegativeButton("Mas lineas?", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                if (strCantidad.isEmpty()){
                                                    Builder builderCantidadCero = new Builder(getActivity());
                                                    builderCantidadCero.setTitle("Error en Cantidad")
                                                            .setMessage("La cantidad del pedido no puede ser cero")
                                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    ((ViewGroup)viewAgregaLineasAPedidoExistente.getParent()).removeView(viewAgregaLineasAPedidoExistente);
                                                                    builderAgregarLineasAExistente.show();
                                                                    edtCantidadAgregarLineasAExistente.requestFocus();
                                                                }
                                                            })
                                                            .show();

                                                }else{
                                                    agregarLineasAPedidoExistente();
                                                }
                                            }
                                        })
                                        .setNeutralButton("Cancelar",null)
                                        .show();
                            }
                        })
                        .setNeutralButton("Cancelar",null)
                        .show();
                        myDBHelper.close();
                    }
                });

                builderNuevoExistente.setNeutralButton("Cancelar",null);
                builderNuevoExistente.setIcon(R.drawable.pedido);
                builderNuevoExistente.show();
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
            Cursor cursor = myDBHelper.fetchAllPedidos();
            if(cursor != null){
                pedidosListaAdapter = new PedidosListaAdapter(getActivity().getApplicationContext(),cursor);
                recyclerViewPedidos.setAdapter(pedidosListaAdapter);
                recyclerViewPedidos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity()
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
            TextView textViewId = (TextView) childView.findViewById(R.id.idPedido);
            pedidoId = textViewId.getText().toString();
            tvNumeroPedido = (TextView) childView.findViewById(R.id.numeroPedido);
            strNumeroPedido = tvNumeroPedido.getText().toString();
            ((CallBacksPedidos) getActivity()).onItemSelected(strNumeroPedido);
            Toast.makeText(getActivity(), "Ha seleccionado el número de pedido: " + strNumeroPedido
                    +"\nCon Id: "+ pedidoId, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemLongPress(View childView, int position){

            delete = (TextView) childView.findViewById(R.id.idPedido);
            tvNumeroPedido = (TextView) childView.findViewById(R.id.numeroPedido);
            pedidoId = delete.getText().toString();
            strNumeroPedido = tvNumeroPedido.getText().toString();
            myDBHelper = new DataBaseHelper(getActivity().getApplication());
            final SQLiteDatabase db = myDBHelper.getWritableDatabase();
            myDBHelper.openDatabase();
            Cursor cursorFechaEntregaPedido = myDBHelper.fetchFechaEntregaPedidoById(strNumeroPedido);
            strFechaEntrega = cursorFechaEntregaPedido.getString(cursorFechaEntregaPedido.getColumnIndex(cursorFechaEntregaPedido.getColumnName(0)));
            if(strFechaEntrega!=null){
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Eliminar Pedido");
                dialog.setMessage("¿Seguro que deseas eliminar el Pedido: "+ strNumeroPedido +" con id: "+pedidoId+"?");
                dialog.setNegativeButton("Cancelar", null);
                dialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String where = "_id" + "=?";
                        String[] whereArgs = new String[]{pedidoId};
                        String wherePedidosDetalles = "PedidoID" + "=?";
                        String[] whereArgsPedidoDetalles = new String[]{strNumeroPedido};
                        db.delete("Pedidos", where, whereArgs);
                        db.delete("PedidosDetalles",wherePedidosDetalles,whereArgsPedidoDetalles);
                        db.close();
                        myDBHelper.close();
                        Toast.makeText(getActivity(), "Se borro el pedido: " + strNumeroPedido, Toast.LENGTH_SHORT).show();

                        try {
                            myDBHelper.openDatabase();
                            Cursor nCursor = myDBHelper.fetchAllProductos();
                            myDBHelper.close();
                            if (nCursor != null) {
                                pedidosListaAdapter = new PedidosListaAdapter(getActivity().getApplicationContext(), nCursor);
                                recyclerViewPedidos.setAdapter(pedidosListaAdapter);
                                recyclerViewPedidos.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new OnItemClickListener()));
                            }

                            if (PedidosActivity.mTwoPane) {
                                ((RefreshPedidos)getActivity()).refreshList();
                            }

                        } catch (SQLException e) {
                            Builder builderDeleteProductoErr = new Builder(getActivity());
                            builderDeleteProductoErr.setTitle("Error al borrar el producto")
                                    .setMessage(e.getMessage())
                                    .setNegativeButton("Aceptar", null)
                                    .show();
                        }
                    }
                });
                myDBHelper.close();
                dialog.show();
            }else{
                Builder builderFechaEntrega = new Builder(getActivity());
                builderFechaEntrega.setTitle("Error al borrar Pedido")
                        .setMessage("El pedido: "+strNumeroPedido+" no esta entregado\nSi desea borrarlo edite el pedido y borre las lineas")
                        .setNegativeButton("Aceptar",null)
                        .show();
            }
        }
    }

    public void onDestroyInsert(){
        Intent intent = new Intent(getActivity(), PedidosActivity.class);
        getActivity().finish();
        startActivity(intent);
    }

    public static class SelectDateFragmentFechaPedido extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
            SetDateFechaPedido(yy, mm+1, dd);

        }

        public void SetDateFechaPedido(int mYear, int mMonth, int mDay) {
            tvFechaPedido.setText(new StringBuilder()
                    .append(mDay).append("/")
                    .append(mMonth).append("/")
                    .append(mYear).append(" "));
            strFechaPedido = tvFechaPedido.getText().toString();
        }

    }
    public static class SelectDateFragmentFechaRequerida extends DialogFragment implements DatePickerDialog.OnDateSetListener {

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
            SetDateFechaRequerida(yy, mm+1, dd);
        }

        private void SetDateFechaRequerida(int mYear, int mMonth, int mDay) {
            tvFechaRequerida.setText(new StringBuilder()
                    .append(mDay).append("/")
                    .append(mMonth).append("/")
                    .append(mYear).append(" "));
            strFechaRequerida = tvFechaRequerida.getText().toString();
        }

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
    public void agregarLineasAPedidoNuevo(){

        myDBHelper.openDatabase();
        final SQLiteDatabase dbLineas = myDBHelper.getWritableDatabase();
        final ContentValues valoresPedidosDetallesLineas = new ContentValues();
        final ContentValues valoresUpDateProductosLineas = new ContentValues();
        final ContentValues valoresPrecioFletePedido = new ContentValues();

        if (strCantidad.isEmpty()){
            Builder builderCantidadCero = new Builder(getActivity());
            builderCantidadCero.setTitle("Error en Cantidad")
                    .setMessage("La cantidad del pedido no puede ser cero")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((ViewGroup)viewAgregarLineasPedidoNuevo.getParent()).removeView(viewAgregarLineasPedidoNuevo);
                            builderAgregarLineasPedidoNuevo.show();
                            edtCantidad.requestFocus();
                        }
                    })
                    .show();

        }else{

            valoresPedidosDetallesLineas.put("PedidoId",intSigPedido);
            valoresPedidosDetallesLineas.put("productoId", intIdProducto);
            valoresPedidosDetallesLineas.put("PrecioUnidad", doublePrecioPorUnidad);
            valoresPedidosDetallesLineas.put("Cantidad", intCantidad);
            valoresPedidosDetallesLineas.put("Descuento", doubleDescuento);

            if(dbLineas.insert("PedidosDetalles", null, valoresPedidosDetallesLineas)!=0){
                Toast.makeText(getContext(),"Se insertaron los valores en Detalles",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(),"No se insertaron los valores en Detalles",Toast.LENGTH_LONG).show();
            }

            Cursor cursorUnidadesEnPedido = myDBHelper.fetchUnidadesEnPedidoFromProductosByProductoId(String.valueOf(intIdProducto));
            int unidadesEnPedidoAntigua = cursorUnidadesEnPedido.getInt(cursorUnidadesEnPedido.getColumnIndex(cursorUnidadesEnPedido.getColumnName(0)));
            int unidadesEnPedidoActuales = unidadesEnPedidoAntigua+intCantidad;
            Cursor cursorUnidadesEnStock  = myDBHelper.fetchUnidadesEnStockFromProductosByProductoId(String.valueOf(intIdProducto));
            int unidadesEnStockAntigua = cursorUnidadesEnStock.getInt(cursorUnidadesEnStock.getColumnIndex(cursorUnidadesEnStock.getColumnName(0)));
            int unidadesEnStockActuales = unidadesEnStockAntigua-intCantidad;
            String where = "_id" + "=?";
            String[] whereArgs = new String[]{String.valueOf(intIdProducto)};
            valoresUpDateProductosLineas.put("UnidadesEnStock",unidadesEnStockActuales);
            valoresUpDateProductosLineas.put("UndadesEnPedido",unidadesEnPedidoActuales);
            dbLineas.update("Productos",valoresUpDateProductosLineas, where, whereArgs);

            Cursor cursorPrecioFleteActual = myDBHelper.fetchPrecioFleteByPedidoId(String.valueOf(intSigPedido));
            double precioFleteActual = cursorPrecioFleteActual.getDouble(cursorPrecioFleteActual.getColumnIndex(cursorPrecioFleteActual.getColumnName(0)));
            double precioFleteFinal = precioFleteActual +doublePrecioFlete;
            valoresPrecioFletePedido.put("PrecioFlete",precioFleteFinal);
            dbLineas.update("Pedidos",valoresPrecioFletePedido,"PedidoID=?",new String[]{String.valueOf(intSigPedido)});
            myDBHelper.close();
        }
    }

    public void agregarLineasAPedidoExistente(){

        myDBHelper.openDatabase();
        final SQLiteDatabase dbLineas = myDBHelper.getWritableDatabase();
        final ContentValues valoresPedidosDetallesLineas = new ContentValues();
        final ContentValues valoresUpDateProductosLineas = new ContentValues();
        final ContentValues valoresPrecioFletePedido = new ContentValues();

            valoresPedidosDetallesLineas.put("PedidoId",strPedidoIDAgregarAExistente);
            valoresPedidosDetallesLineas.put("productoId", intIdProductoAgregarLineasAExixtente);
            valoresPedidosDetallesLineas.put("PrecioUnidad", doublePrecioPorUnidad);
            valoresPedidosDetallesLineas.put("Cantidad", intCantidad);
            valoresPedidosDetallesLineas.put("Descuento", doubleDescuento);

            if(dbLineas.insert("PedidosDetalles", null, valoresPedidosDetallesLineas)!=0){
                Toast.makeText(getContext(),"Se insertaron los valores en Detalles",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(),"No se insertaron los valores en Detalles",Toast.LENGTH_LONG).show();
            }

            Cursor cursorUnidadesEnPedido = myDBHelper.fetchUnidadesEnPedidoFromProductosByProductoId(String.valueOf(intIdProductoAgregarLineasAExixtente));
            int unidadesEnPedidoAntigua = cursorUnidadesEnPedido.getInt(cursorUnidadesEnPedido.getColumnIndex(cursorUnidadesEnPedido.getColumnName(0)));
            int unidadesEnPedidoActuales = unidadesEnPedidoAntigua+intCantidad;
            Cursor cursorUnidadesEnStock  = myDBHelper.fetchUnidadesEnStockFromProductosByProductoId(String.valueOf(intIdProductoAgregarLineasAExixtente));
            int unidadesEnStockAntigua = cursorUnidadesEnStock.getInt(cursorUnidadesEnStock.getColumnIndex(cursorUnidadesEnStock.getColumnName(0)));
            int unidadesEnStockActuales = unidadesEnStockAntigua-intCantidad;
            String where = "_id" + "=?";
            String[] whereArgs = new String[]{String.valueOf(intIdProductoAgregarLineasAExixtente)};
            valoresUpDateProductosLineas.put("UnidadesEnStock",unidadesEnStockActuales);
            valoresUpDateProductosLineas.put("UndadesEnPedido",unidadesEnPedidoActuales);
            dbLineas.update("Productos",valoresUpDateProductosLineas, where, whereArgs);

            Cursor cursorPrecioFleteActual = myDBHelper.fetchPrecioFleteByPedidoId(String.valueOf(strPedidoIDAgregarAExistente));
            double precioFleteActual = cursorPrecioFleteActual.getDouble(cursorPrecioFleteActual.getColumnIndex(cursorPrecioFleteActual.getColumnName(0)));
            double precioFleteFinal = precioFleteActual +doublePrecioFlete;
            valoresPrecioFletePedido.put("PrecioFlete",precioFleteFinal);
            dbLineas.update("Pedidos",valoresPrecioFletePedido,"PedidoID=?",new String[]{String.valueOf(strPedidoIDAgregarAExistente)});
            myDBHelper.close();
    }
}