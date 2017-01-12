package alfonso8969.com.proyecto2.activities.barCode;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.barcode.BarCode_ItemsFragment;
import alfonso8969.com.proyecto2.helpers.DataBaseHelper;

public class BarCode_ItemsList extends AppCompatActivity {

    public DataBaseHelper myDBHelper;
    public String strCodigoBarras;
    public Cursor myCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode__items_list);
        Bundle intent = getIntent().getExtras();
        Bundle extras = getIntent().getExtras();
        strCodigoBarras = extras.getString("codigoBarras");
        if(savedInstanceState == null){

            BarCode_ItemsFragment fragment = new BarCode_ItemsFragment();
            fragment.setArguments(intent);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.barcode_list_item,fragment)
                    .commit();
        }
    }

}
