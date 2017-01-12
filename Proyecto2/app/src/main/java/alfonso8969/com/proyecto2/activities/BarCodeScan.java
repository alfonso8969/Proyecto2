package alfonso8969.com.proyecto2.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.barCode.BarcodeActivity;
import alfonso8969.com.proyecto2.fragments.barcode.BarCodeListaFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

public class BarCodeScan extends AppCompatActivity implements View.OnClickListener {

    Button btnScan, btnGuardar;
    TextView tvFormato, tvCodigoBarras;
    EditText edtNombreProducto;
    DataBaseHelper myDbHelper;
    SQLiteDatabase myDataBase;
    String strNombreProducto,strFormato,strCodigoDeBarras,strMessage;
    Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scan);
        myContext = this;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnScan = (Button) findViewById(R.id.btn_scan);
        btnGuardar = (Button) findViewById(R.id.btn_guardar);
        tvFormato = (TextView) findViewById(R.id.tvFormato);
        tvCodigoBarras = (TextView) findViewById(R.id.tvResultado);
        edtNombreProducto = (EditText) findViewById(R.id.edt_NombreProducto);
        btnScan.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_scan:
                    IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                    scanIntegrator.initiateScan();
                    break;
                case R.id.btn_guardar:
                    strNombreProducto = edtNombreProducto.getText().toString();
                    strFormato = tvFormato.getText().toString();
                    strCodigoDeBarras = tvCodigoBarras.getText().toString();
                    myDbHelper = new DataBaseHelper(getApplicationContext());
                    myDbHelper.openDatabase();
                    myDataBase =  myDbHelper.getWritableDatabase();
                    ContentValues valuesBarCode = new ContentValues();
                    valuesBarCode.put("NombreProducto",strNombreProducto);
                    valuesBarCode.put("CodigoBarras",strCodigoDeBarras);
                    valuesBarCode.put("Formato",strFormato);

                    if(strNombreProducto.isEmpty()) {
                        strMessage = "El nombre del producto no puede estar vacio";
                        getDialog(strMessage);
                    }else if(strCodigoDeBarras.isEmpty()){
                        strMessage = "El codigo de barras no puede estar vacio";
                        getDialog(strMessage);
                    }else if(strFormato.isEmpty()){
                        strMessage = "El fornato no puede estar vacio";
                        getDialog(strMessage);
                    }else{

                      if(myDataBase.insert("CodigoBarras",null,valuesBarCode)!=0){
                          Toast.makeText(getApplicationContext(),"Se insertaron los valores en CodigoBarras",Toast.LENGTH_LONG).show();
                          myDbHelper.close();
                          BarCodeListaFragment barCodeListaFragment = new BarCodeListaFragment();
                          barCodeListaFragment.actualizarRecyclerViewBarCodes(myContext);
                          Intent intent = new Intent(this, BarcodeActivity.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                          startActivity(intent);

                      }

                        else{
                          Toast.makeText(getApplicationContext(),"Error al insertar los valores en CodigoBarras",Toast.LENGTH_LONG).show();
                      }

                        myDbHelper.close();
                    }

                    break;
                default:
                    break;
            }
        }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            tvFormato.setText(scanFormat);
            tvCodigoBarras.setText(scanContent);
            if(Objects.equals(scanFormat,"EAN_13")){
                final String url = "http://www.ean-search.org/perl/ean-search.pl?q="+scanContent;
                final int intStatusConnectionCode = getStatusConnectionCode(url);
                String nombreProducto = "";
                if (intStatusConnectionCode == 200) {

                    Toast.makeText(getApplicationContext(), "El Status Code es: " + intStatusConnectionCode, Toast.LENGTH_SHORT).show();
                    // Obtengo el HTML de la web en un objeto Document
                    Document document = getHtmlDocument(url);

                    // Busco todas las entradas que estan dentro de:
                    Elements entradas = document.select("div#main");

                    //Toast.makeText(getApplicationContext(), "Número de entradas en la página: "+entradas.size(), Toast.LENGTH_SHORT).show();

                    // Paseo cada una de las entradas
                    for (Element elem : entradas) {
                        String textoDiv = elem.getElementById("main").text();
                        for(int i=35;i<=textoDiv.length();i++){

                            int indexEnd=textoDiv.indexOf("What");
                            nombreProducto= textoDiv.substring(35,indexEnd);

                        }
                //Toast.makeText(getApplicationContext(), nombreProducto, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "El Status Code no es OK es: " + intStatusConnectionCode, Toast.LENGTH_SHORT).show();
                }
                edtNombreProducto.setText(nombreProducto);
            }
        }
    }
    private void getDialog(String message){
        AlertDialog.Builder builderErr = new AlertDialog.Builder(myContext);
        builderErr.setTitle("Error al intentar guardar")
                .setMessage(message)
                .setPositiveButton("Aceptar",null)
                .show();

    }
    public int getStatusConnectionCode(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpConnection.Response response = null;

        try {
            response = (HttpConnection.Response) Jsoup.connect(url).userAgent("Mozilla/5.0").referrer("http://www.google.com").timeout(100000).ignoreHttpErrors(true).execute();
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(), "Excepción al obtener el Status Code: " + ex.getMessage(), Toast.LENGTH_SHORT).show();

        }
        assert response != null;
        return response.statusCode();
    }
    public Document getHtmlDocument(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").referrer("http://www.google.com").timeout(100000).get();
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(), "Excepción al obtener el HTML de la página: " + ex.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return doc;
    }
}