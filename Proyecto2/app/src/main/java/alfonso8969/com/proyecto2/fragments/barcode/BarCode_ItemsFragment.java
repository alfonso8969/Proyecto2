package alfonso8969.com.proyecto2.fragments.barcode;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Objects;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.activities.barCode.BarcodeActivity;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

/**
 * Creado por alfonso en fecha 07/12/2016.
 */

public class BarCode_ItemsFragment extends Fragment {
    public TextView tvFormato, tvNombreProducto,tvResultado;
    public String strCodigoBarras,strFormatScan;
    public DataBaseHelper myDBHelper;
    public Cursor myCursor;
    public ListView lv;
    public Bundle bundle;
    public View rootView;
    Intent browserIntent;

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.barcodes_itemsfragment, container, false);
        lv = (ListView) rootView.findViewById(R.id.lista_BarCodes);
        tvFormato = (TextView) rootView.findViewById(R.id.edtNombreProducto);
        tvNombreProducto = (TextView) rootView.findViewById(R.id.tvFormato);
        tvResultado = (TextView) rootView.findViewById(R.id.tvResultado);
        bundle = getArguments();

        if (bundle != null) {

            boolean restart = bundle.getBoolean("restart");

            if (restart) {
                lv.setAdapter(null);
            } else {

                try {
                    buscarEnWeb();
                } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
                    e.printStackTrace();
                }
            }
        }

   return rootView;

        }

   @SuppressWarnings("deprecation")
   public void buscarEnWeb() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

       /*ConnectivityManager connMgr = (ConnectivityManager)
               getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
       if (networkInfo != null && networkInfo.isConnected()) {
           // Operaciones http
       } else {
           // Mostrar errores
       }*/

       myDBHelper = new DataBaseHelper(getContext());
       myDBHelper.openDatabase();
       strCodigoBarras= (String)bundle.get("CodigoDeBarras");
       Cursor cursorFormatScan = myDBHelper.fetchScanFormatByCodigoBarras(strCodigoBarras);
       strFormatScan = cursorFormatScan.getString(cursorFormatScan.getColumnIndex(cursorFormatScan.getColumnName(0)));
       myDBHelper.close();

       if(Objects.equals(strFormatScan, "EAN_13")){
           browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ean-search.org/perl/ean-search.pl?q="+strCodigoBarras));

       }else{
           browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.nl/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q="+strCodigoBarras));
       }

       Intent intent = new Intent(getActivity(), BarcodeActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       startActivity(intent);
       browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
       startActivity(browserIntent);
   }
}

