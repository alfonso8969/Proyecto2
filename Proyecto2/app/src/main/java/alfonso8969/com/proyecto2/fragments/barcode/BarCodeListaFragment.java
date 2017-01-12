package alfonso8969.com.proyecto2.fragments.barcode;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.BarCodeScan;
import alfonso8969.com.proyecto2.activities.barCode.BarcodeActivity;
import alfonso8969.com.proyecto2.adapters.BarCodeListaAdapter;
import alfonso8969.com.proyecto2.clases.RecyclerItemClickListener;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;
/**
 * Creado por alfonso en fecha 07/12/2016.
 */

public class BarCodeListaFragment extends Fragment {

    public static RecyclerView recyclerViewBarCodes;
    public static final String TAG = "RecyclerViewBarCodesFragment";
    BarCodeListaAdapter barCodeListaAdapter;
    DataBaseHelper myDBHelper;
    TextView delete;
    TextView tvNombreProducto,tvCodigoBarras;
    String strCodigoBarrasId,strCodigoBarras,strNombreProducto;
    FloatingActionButton fb;
    public ViewGroup containerBarCode;
    public static LayoutInflater inflaterBarCode;
    Context context;


    public interface CallBacksBarcode {
        void onItemSelected(String codigo);
    }

    public interface Refresh {
        void refreshList();
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle saveInstanceState) {
        final View viewRoot = inflater.inflate(R.layout.lista_barcodes_fragment, container, false);
        viewRoot.setTag(TAG);
        containerBarCode = container;
        inflaterBarCode = inflater;
        context = getActivity().getApplicationContext();
        recyclerViewBarCodes = (RecyclerView) viewRoot.findViewById(R.id.lista_BarCodes);
        recyclerViewBarCodes.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        fb = (FloatingActionButton)viewRoot.findViewById(R.id.fbBarCode);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentScan = new Intent(getActivity(), BarCodeScan.class);
                startActivity(intentScan);

            }
        });

        actualizarRecyclerViewBarCodes(context);

        return viewRoot;
    }
    private class OnItemClickListener extends RecyclerItemClickListener.SimpleOnItemClickListener{

        public void onItemClick(View childView, int position){
            TextView textViewId = (TextView) childView.findViewById(R.id.idBarcode);
            strCodigoBarrasId=textViewId.getText().toString();
            tvNombreProducto = (TextView) childView.findViewById(R.id.edtNombreProducto);
            strNombreProducto = tvNombreProducto.getText().toString();
            tvCodigoBarras = (TextView)childView.findViewById(R.id.tvResultado);
            strCodigoBarras = tvCodigoBarras.getText().toString();
            ((CallBacksBarcode) getActivity()).onItemSelected(strCodigoBarras);
            Toast.makeText(getActivity(), "Ha seleccionado el producto: " + strNombreProducto
                    +"\nCon codigo de barras: " + strCodigoBarras, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onItemLongPress(View childView, int position){

            delete = (TextView) childView.findViewById(R.id.idBarcode);
            tvNombreProducto = (TextView) childView.findViewById(R.id.edtNombreProducto);
            strCodigoBarrasId=delete.getText().toString();
            strNombreProducto=tvNombreProducto.getText().toString();
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Eliminar Empleado");
            dialog.setMessage("¿Seguro que deseas eliminar el producto: "+ strNombreProducto+"?");
            dialog.setNegativeButton("Cancelar", null);
            dialog.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDBHelper = new DataBaseHelper(getActivity().getApplication());
                    SQLiteDatabase db = myDBHelper.getWritableDatabase();
                    String where = "_id" + "=?";
                    String[] whereArgs = new String[]{strCodigoBarrasId};

                    db.delete("CodigoBarras", where, whereArgs);

                    db.close();

                    Toast.makeText(getActivity(), "Se borro el producto: " + strNombreProducto, Toast.LENGTH_SHORT).show();

                    actualizarRecyclerViewBarCodes(context);
                    Intent intent = new Intent(getActivity(), BarcodeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);


                }
            })
                    .show();
        }
    }
    public void actualizarRecyclerViewBarCodes(Context context){

        try {

            myDBHelper = new DataBaseHelper(context);

            try {
                myDBHelper.createDataBase();
            } catch (IOException e) {
                throw new Error("No se puede crear BD");
            }

            myDBHelper.openDatabase();
            Cursor cursor = myDBHelper.fetchAllBarCodes();
            myDBHelper.close();
            if(cursor != null){
                barCodeListaAdapter = new BarCodeListaAdapter(context,cursor);
                recyclerViewBarCodes.setAdapter(barCodeListaAdapter);
                recyclerViewBarCodes.addOnItemTouchListener(new RecyclerItemClickListener(context, new OnItemClickListener()));

            }
        } catch (SQLException e) {
            throw new Error("Fallo en BD: "+e.getMessage()+"\n"+e.getCause());
        }
    }
}