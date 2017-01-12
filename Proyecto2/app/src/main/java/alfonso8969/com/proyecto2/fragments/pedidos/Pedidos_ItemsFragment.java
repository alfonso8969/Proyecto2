package alfonso8969.com.proyecto2.fragments.pedidos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 16/11/2016.
 */

public class Pedidos_ItemsFragment extends Fragment {
    public TextView tvClienteID,tvNumeroPedido,tvNombreCompañia, tvDireccionCodPostClientePedido,tvPaisCiuReClientePedido;
    public TextView tvCompañiaEnvio,tvNombreEmpleado,tvPrecioFlete, tvPrecioTotalLineaPedido,tvPrecioTotalPedido,tvFechaPedido,tvFechaRequerida,tvFechaEntrega;
    public TextView tvCantidad,tvPrecioPorUnidad,tvDescuento, tvLineasDePedido,tvDescontado;
    public String strClienteID,strNombreCompañia,strFechaPedido,strFechaRequerida,strFechaEntrega, strDireccionCoPos, strPaisCiudadRegion;
    public String strNombreEmpeado,strApellidoEmpleado,strNombreCompletoEmpleado,strNombreCompañiaEnvio,strNumeroPedido;
    public String idProducto,strNombreProducto;
    public int intNumeroEmpleado,intNumeroPedido,intViaEnvio,cantidad;
    double douPrecioFlete,precioUnidad,descuento,precioTotal,sumaTotalPrecioFlete,douDescontado;
    public DataBaseHelper myDBHelper;
    public Cursor myCursor;
    public ListView lv;
    Spinner spiNombreProducto;
    Context context;
    @SuppressLint("SetTextI18n")
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.pedidos_itemsfragment, container, false);
        final DecimalFormat df = new DecimalFormat("#.##");
        lv = (ListView)rootView.findViewById(R.id.lista_Pedidos);
        tvNumeroPedido = (TextView)rootView.findViewById(R.id.tv_numeroPedido);
        tvClienteID = (TextView)rootView.findViewById(R.id.tv_clienteIDPedido);
        tvFechaPedido= (TextView)rootView.findViewById(R.id.tv_fechaPedido);
        tvFechaRequerida = (TextView)rootView.findViewById(R.id.tv_fechaRequerida);
        tvFechaEntrega = (TextView)rootView.findViewById(R.id.tv_fechaEntrega);
        tvNombreCompañia = (TextView)rootView.findViewById(R.id.tv_nombre_compañiaPedido);
        tvDireccionCodPostClientePedido = (TextView)rootView.findViewById(R.id.tv_direccion_clientePedido);
        tvPaisCiuReClientePedido = (TextView)rootView.findViewById(R.id.tv_PaisCiuReClientePedido);
        tvCompañiaEnvio = (TextView)rootView.findViewById(R.id.tv_Via_Envio);
        tvNombreEmpleado = (TextView)rootView.findViewById(R.id.tv_nombreEmpleadoPedido);
        tvPrecioFlete = (TextView)rootView.findViewById(R.id.tv_PrecioFlete);
        tvPrecioTotalLineaPedido = (TextView)rootView.findViewById(R.id.tv_PrecioTotalPorLinea);
        tvPrecioTotalPedido = (TextView)rootView.findViewById(R.id.tv_PrecioTotal);
        tvCantidad =(TextView) rootView.findViewById(R.id.tv_Cantidad);
        tvPrecioPorUnidad =(TextView) rootView.findViewById(R.id.tv_PrecioPorUnidad);
        tvDescuento =(TextView) rootView.findViewById(R.id.tv_Descuento);
        tvLineasDePedido = (TextView) rootView.findViewById(R.id.tv_LineasDelPedido);
        tvDescontado = (TextView)rootView.findViewById(R.id.tv_Descontado);
        spiNombreProducto=(Spinner) rootView.findViewById(R.id.spi_nombreProducto);


        context=getActivity().getApplicationContext();

        Bundle bundle = getArguments();

        if(bundle != null) {

            boolean restart = bundle.getBoolean("restart");

            if (restart) {
                lv.setAdapter(null);
            } else {

                strNumeroPedido = (String) bundle.get("pedidoId");
                myDBHelper = new DataBaseHelper(getActivity());

                try {
                    myDBHelper.createDataBase();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                myDBHelper.openDatabase();
                myCursor = myDBHelper.fetchPedidoById(strNumeroPedido);

                if (myCursor != null) {
                    intNumeroPedido = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(1))));
                    strClienteID = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(2))));
                    intNumeroEmpleado=(myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(3))));

                    Cursor cursorNombreEmpleado =myDBHelper.fetchNombreEmpleadoById(String.valueOf(intNumeroEmpleado));
                    strNombreEmpeado =cursorNombreEmpleado.getString(cursorNombreEmpleado.getColumnIndex(cursorNombreEmpleado.getColumnName(0)));
                    strApellidoEmpleado =cursorNombreEmpleado.getString(cursorNombreEmpleado.getColumnIndex(cursorNombreEmpleado.getColumnName(1)));
                    strNombreCompletoEmpleado = strApellidoEmpleado+", "+strNombreEmpeado;

                    Cursor cursorNombreCompañia= myDBHelper.fetchDatosClienteByClienteID(strClienteID);
                    strNombreCompañia = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(0))));
                    String direccion = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(1))));
                    String ciudad = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(2))));
                    String region = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(3))));
                    String codPos = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(4))));
                    String pais = (cursorNombreCompañia.getString(cursorNombreCompañia.getColumnIndex(cursorNombreCompañia.getColumnName(5))));

                    strFechaPedido= (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(4))));
                    strFechaRequerida = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(5))));
                    strFechaEntrega = (myCursor.getString(myCursor.getColumnIndex(myCursor.getColumnName(6))));
                    intViaEnvio = (myCursor.getInt(myCursor.getColumnIndex(myCursor.getColumnName(7))));

                    Cursor cursorNombreCompañiaEnvio= myDBHelper.fetchNombreCompañiaEnvioById(String.valueOf(intViaEnvio));
                    strNombreCompañiaEnvio =(cursorNombreCompañiaEnvio.getString(cursorNombreCompañiaEnvio.getColumnIndex(cursorNombreCompañiaEnvio.getColumnName(0))));
                    Cursor cursorCalculoSumaTotalPrecioFlete = myDBHelper.calculoSumaTotalPrecioFlete(strNumeroPedido);
                    sumaTotalPrecioFlete=cursorCalculoSumaTotalPrecioFlete.getDouble(cursorCalculoSumaTotalPrecioFlete.getColumnIndex(cursorCalculoSumaTotalPrecioFlete.getColumnName(0)));
                    douPrecioFlete = sumaTotalPrecioFlete;

                    Cursor cursorProductoIdFromPedidosDetalles = myDBHelper.fetchAllProductoIdForPedidosDetallesPedidoId(String.valueOf(intNumeroPedido));
                    SimpleCursorAdapter adapterProductoIdFromPedidosDetalles = new SimpleCursorAdapter(getActivity().getApplicationContext(),
                            android.R.layout.select_dialog_item,cursorProductoIdFromPedidosDetalles,new String[] {"productoId"},
                            new int[] {android.R.id.text1}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                    adapterProductoIdFromPedidosDetalles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spiNombreProducto.setAdapter(adapterProductoIdFromPedidosDetalles);
                    spiNombreProducto.setPopupBackgroundResource(R.drawable.common_google_signin_btn_text_dark);
                    spiNombreProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView parent, View view, int pos,long id) {
                            if (parent.getId() == R.id.spi_nombreProducto) {
                                TextView tvNombreProducto = (TextView)spiNombreProducto.getSelectedView();
                                idProducto = tvNombreProducto.getText().toString();
                                myDBHelper.openDatabase();
                                Cursor idProductoCursor= myDBHelper.fetch_idNombresProducto(idProducto);

                                if(idProductoCursor!=null){
                                    strNombreProducto = idProductoCursor.getString(idProductoCursor.getColumnIndex(idProductoCursor.getColumnName(0)));
                                    tvNombreProducto.setText(strNombreProducto);

                                    Cursor cursorCantidadNombreProducto = myDBHelper.fetchCantidadByPedidoId(idProducto,String.valueOf(intNumeroPedido));
                                    cantidad = cursorCantidadNombreProducto.getInt(cursorCantidadNombreProducto.getColumnIndex(cursorCantidadNombreProducto.getColumnName(1)));

                                    Cursor cursorPrecioUnidad = myDBHelper.fetchPrecioUnidadByProductoIdAndPedidoId(idProducto,String.valueOf(intNumeroPedido));
                                    precioUnidad = cursorPrecioUnidad.getDouble(cursorPrecioUnidad.getColumnIndex(cursorPrecioUnidad.getColumnName(1)));

                                    Cursor cursorDescuento = myDBHelper.fetchDescuentoByPedidoId(idProducto,String.valueOf(intNumeroPedido));
                                    descuento = cursorDescuento.getDouble(cursorDescuento.getColumnIndex(cursorDescuento.getColumnName(0)));

                                    tvCantidad.setText(String.valueOf(cantidad));
                                    tvPrecioPorUnidad.setText(String.valueOf(precioUnidad)+"€");
                                    tvDescuento.setText(String.valueOf(descuento)+"%");
                                    douDescontado =(cantidad*precioUnidad*descuento);
                                    double douPrecioPorLinea=  (cantidad*precioUnidad)-douDescontado;
                                    tvDescontado.setText(String.valueOf(df.format(douDescontado))+"€");
                                    tvPrecioTotalLineaPedido.setText(String.valueOf(df.format(douPrecioPorLinea))+"€");
                                }

                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    Cursor cursorCalculoPrecioTotalPedido = myDBHelper.calculoSumaTotalPedido(String.valueOf(intNumeroPedido));
                    precioTotal = cursorCalculoPrecioTotalPedido.getDouble(cursorCalculoPrecioTotalPedido.getColumnIndex(cursorCalculoPrecioTotalPedido.getColumnName(0)));
                    Cursor cursorLineasDelPedido= myDBHelper.fetchLineasDePedido(String.valueOf(intNumeroPedido));
                    int intLineasPedido = cursorLineasDelPedido.getInt(cursorLineasDelPedido.getColumnIndex(cursorLineasDelPedido.getColumnName(0)));
                    myDBHelper.close();

                    tvNumeroPedido.setText(String.valueOf(intNumeroPedido));
                    tvClienteID.setText(String.valueOf(strClienteID));
                    tvNombreCompañia.setText(strNombreCompañia);
                    tvFechaPedido.setText(String.valueOf(strFechaPedido));
                    tvFechaRequerida.setText(strFechaRequerida);
                    tvFechaEntrega.setText(strFechaEntrega);
                    tvCompañiaEnvio.setText(strNombreCompañiaEnvio);
                    tvNombreEmpleado.setText(strNombreCompletoEmpleado);
                    tvPrecioFlete.setText(String.valueOf(douPrecioFlete)+"€");
                    strDireccionCoPos = direccion + "---" + codPos;
                    strPaisCiudadRegion = pais + "---" + ciudad + "---" + region;
                    tvDireccionCodPostClientePedido.setText(strDireccionCoPos);
                    tvPaisCiuReClientePedido.setText(strPaisCiudadRegion);
                    tvPrecioTotalPedido.setText(String.valueOf(df.format(precioTotal+douPrecioFlete)) + "€");
                    tvLineasDePedido.setText(String.valueOf(intLineasPedido));

                }
            }
        }

        return rootView;
    }
}
