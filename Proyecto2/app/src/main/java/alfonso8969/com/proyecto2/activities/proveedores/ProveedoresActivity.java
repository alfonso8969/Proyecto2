package alfonso8969.com.proyecto2.activities.proveedores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.proveedores.ProveedoresListaFragment;
import alfonso8969.com.proyecto2.fragments.proveedores.Proveedores_ItemsFragment;

public class ProveedoresActivity extends AppCompatActivity implements ProveedoresListaFragment.CallBacksProveedores,ProveedoresListaFragment.Refresh{

    public static boolean mTwoPane;
    private static  final String ELEMENTS_TAG = "ELEMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedores);

        if(findViewById(R.id.proveedores_list_item) != null ){
            mTwoPane =true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.proveedores_list_item,new Proveedores_ItemsFragment(), ELEMENTS_TAG)
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
    public void onItemSelected(String proveedorId) {
        if(getmTwoPane()){
            Bundle bundle = new Bundle();
            bundle.putString("proveedorId",proveedorId);
            Proveedores_ItemsFragment itemsFragment = new Proveedores_ItemsFragment();
            itemsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.proveedores_list_item,itemsFragment).
                    commit();
        }
        else{
            Intent intent = new Intent(this,Proveedores_itemsList.class);
            intent.putExtra("proveedorId",proveedorId);
            startActivity(intent);
        }
    }

    @Override
    public void refreshList() {
        Bundle bundle = new Bundle();
        Proveedores_ItemsFragment itemsFragment = new Proveedores_ItemsFragment();

        bundle.putBoolean("restart",true);
        itemsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.proveedores_list_item,itemsFragment).
                commit();
    }
}
