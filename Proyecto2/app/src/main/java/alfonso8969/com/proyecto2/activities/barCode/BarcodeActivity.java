package alfonso8969.com.proyecto2.activities.barCode;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog.Builder;
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

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.barcode.BarCodeListaFragment;
import alfonso8969.com.proyecto2.fragments.barcode.BarCode_ItemsFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;


public class BarcodeActivity extends AppCompatActivity implements BarCodeListaFragment.CallBacksBarcode,BarCodeListaFragment.Refresh{
    public static boolean mTwoPane;
    private static  final String ELEMENTS_TAG = "ELEMENTS";
    private DataBaseHelper myDBHelper;
    private Spinner spiNombreProducto;
    private String strNombreProducto;
    private EditText edtNombreProducto;
    private TextView tvFormato,tvCodigoBarras;
    AlertDialog.Builder builderSeleccionarCodigoBarras;
    Cursor myCursor;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        if(findViewById(R.id.barcode_list_item) != null ){
            mTwoPane =true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.barcode_list_item,new BarCode_ItemsFragment(), ELEMENTS_TAG)
                .commit();
            }else {
                mTwoPane = false;
            }
        }
    }

    public boolean getmTwoPane(){
        return mTwoPane;
    }


    @Override
    public void onItemSelected(String codigo) {
        if(getmTwoPane()){
            Bundle bundle = new Bundle();
            bundle.putString("CodigoDeBarras",codigo);
            BarCode_ItemsFragment itemsFragment = new BarCode_ItemsFragment();
            itemsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.barcode_list_item,itemsFragment).
                    commit();
        }
        else{
            Intent intent = new Intent(this,BarCode_ItemsList.class);
            intent.putExtra("CodigoDeBarras",codigo);
            startActivity(intent);
        }

    }


    @Override
    public void refreshList() {
        Bundle bundle = new Bundle();
        BarCode_ItemsFragment itemsFragment = new BarCode_ItemsFragment();

        bundle.putBoolean("restart",true);
        itemsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.barcode_list_item,itemsFragment).
                commit();
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detaileditdel, menu);
        return super.onCreateOptionsMenu(menu);

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        context = this;
        myDBHelper=new DataBaseHelper(getApplication().getApplicationContext());
        myDBHelper.openDatabase();
        myCursor = myDBHelper.fetchAllNombreProductoFromBarCodes();
        myDBHelper.close();
        if (item.getItemId() == R.id.action_edit) {

            builderSeleccionarCodigoBarras = new AlertDialog.Builder(context);
            builderSeleccionarCodigoBarras.setView(getViewSeleccionar(context,myCursor))
                    .setTitle("Seleccionar códigos de barras")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            myDBHelper.openDatabase();
                            Cursor cursor= myDBHelper.fetchCodigoBarrasByNombreProducto(strNombreProducto);
                            myDBHelper.close();
                            final View viewEditarBarCode= View.inflate(context,R.layout.edit_barcodes,null);
                            edtNombreProducto = (EditText)viewEditarBarCode.findViewById(R.id.edtNombreProducto);
                            tvFormato = (TextView)viewEditarBarCode.findViewById(R.id.tvFormato);
                            tvCodigoBarras = (TextView)viewEditarBarCode.findViewById(R.id.tvResultado);
                            if(cursor!=null){
                                edtNombreProducto.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
                                tvCodigoBarras.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
                                tvFormato.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
                            }
                            final Builder builderEditarCodigoBarras = new Builder(context);
                            builderEditarCodigoBarras.setView(viewEditarBarCode)
                                    .setTitle("Editar códigos de barras")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ContentValues valuesCodigoBarras = new ContentValues();
                                            valuesCodigoBarras.put("NombreProducto",edtNombreProducto.getText().toString());
                                            SQLiteDatabase db = myDBHelper.getWritableDatabase();
                                            myDBHelper.openDatabase();
                                            if(edtNombreProducto.getText().toString().isEmpty()){
                                                Builder actualizar = new Builder(context);
                                                actualizar.setTitle("Error al actualizar")
                                                        .setMessage("El campo nombre producto no puede estar vacio")
                                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                ((ViewGroup)viewEditarBarCode.getParent()).removeView(viewEditarBarCode);
                                                                builderEditarCodigoBarras.show();
                                                                edtNombreProducto.requestFocus();
                                                            }
                                                        })
                                                        .show();
                                            }else{
                                                if(db.update("CodigoBarras",valuesCodigoBarras,"NombreProducto=?",new String[]{strNombreProducto})!=0) {
                                                    Toast.makeText(context, "Se actualizaron los datos", Toast.LENGTH_LONG).show();
                                                    BarCodeListaFragment barCodeListaFragment = new BarCodeListaFragment();
                                                    barCodeListaFragment.actualizarRecyclerViewBarCodes(context);
                                                    Intent intent = new Intent(getApplicationContext(), BarcodeActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    myDBHelper.close();
                                                }

                                                else
                                                    Toast.makeText(context,"Error al actualizar los datos",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancelar",null)
                                    .show();
                        }
                    })
                    .setNegativeButton("Cancelar",null)
                    .show();
        }
        if(item.getItemId() == R.id.action_delete){

            builderSeleccionarCodigoBarras = new AlertDialog.Builder(context);
            builderSeleccionarCodigoBarras.setView(getViewSeleccionar(context,myCursor))
                    .setTitle("Seleccionar códigos de barras")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            myDBHelper.openDatabase();
                            final SQLiteDatabase dbDelete = myDBHelper.getWritableDatabase();
                            final Builder builderBorrarCodigoBarras = new Builder(context);
                            builderBorrarCodigoBarras.setTitle("Borrar códigos de barras")
                                    .setMessage("Seguro que desea borrar el producto: "+strNombreProducto)
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if(dbDelete.delete("CodigoBarras","NombreProducto=?",new String[]{strNombreProducto})!=0){
                                                Toast.makeText(context, "Se borraron los datos", Toast.LENGTH_LONG).show();
                                                BarCodeListaFragment barCodeListaFragment = new BarCodeListaFragment();
                                                barCodeListaFragment.actualizarRecyclerViewBarCodes(context);
                                                Intent intent = new Intent(getApplicationContext(), BarcodeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                myDBHelper.close();
                                            }else{
                                                 Toast.makeText(context,"Error al borrar los datos",Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancelar",null)
                                    .show();
                        }
                    })
                    .setNegativeButton("Cancelar",null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }
    public View getViewSeleccionar(Context context, Cursor cursor){

        View view = View.inflate(context, R.layout.seleccionarbarcode_barcodes, null);
        spiNombreProducto = (Spinner)view.findViewById(R.id.spi_nombreProducto);
        SimpleCursorAdapter adapterNombreProducto = new SimpleCursorAdapter(getApplicationContext(),
                android.R.layout.select_dialog_item, cursor,new String[]{"NombreProducto"},
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
 }


