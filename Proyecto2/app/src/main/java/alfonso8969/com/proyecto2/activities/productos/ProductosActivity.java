package alfonso8969.com.proyecto2.activities.productos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.productos.ProductosListaFragment;
import alfonso8969.com.proyecto2.fragments.productos.Productos_ItemsFragment;

public class ProductosActivity extends AppCompatActivity implements ProductosListaFragment.CallBacksProductos,ProductosListaFragment.RefreshProductos{

    public static boolean mTwoPane;
    private static  final String ELEMENTS_TAG = "ELEMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        if(findViewById(R.id.productos_list_item) != null ){
            mTwoPane =true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.productos_list_item,new Productos_ItemsFragment(), ELEMENTS_TAG)
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
    public void onItemSelected(String productoId) {
        if(getmTwoPane()){
            Bundle bundle = new Bundle();
            bundle.putString("productoId",productoId);
            Productos_ItemsFragment itemsFragment = new Productos_ItemsFragment();
            itemsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.productos_list_item,itemsFragment).
                    commit();
        }
        else{
            Intent intent = new Intent(this,Productos_ItemsList.class);
            intent.putExtra("productoId",productoId);
            startActivity(intent);
        }

    }

    @Override
    public void refreshList() {
        Bundle bundle = new Bundle();

        Productos_ItemsFragment itemsFragment = new Productos_ItemsFragment();

        bundle.putBoolean("restart",true);
        itemsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.productos_list_item,itemsFragment).
                commit();
    }
}
