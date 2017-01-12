package alfonso8969.com.proyecto2.activities.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.pedidos.PedidosListaFragment;
import alfonso8969.com.proyecto2.fragments.pedidos.Pedidos_ItemsFragment;
import alfonso8969.com.proyecto2.fragments.productos.Productos_ItemsFragment;

public class PedidosActivity extends AppCompatActivity implements PedidosListaFragment.CallBacksPedidos,PedidosListaFragment.RefreshPedidos{
    public static boolean mTwoPane;
    private static  final String ELEMENTS_TAG = "ELEMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        if(findViewById(R.id.pedidos_list_item) != null ){
            mTwoPane =true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.pedidos_list_item,new Pedidos_ItemsFragment(), ELEMENTS_TAG)
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
    public void onItemSelected(String pedidoId) {
        if(getmTwoPane()){
            Bundle bundle = new Bundle();
            bundle.putString("pedidoId",pedidoId);
            Pedidos_ItemsFragment itemsFragment = new Pedidos_ItemsFragment();
            itemsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.pedidos_list_item,itemsFragment).
                    commit();
        }
        else{
            Intent intent = new Intent(this,Pedidos_ItemsList.class);
            intent.putExtra("pedidoId",pedidoId);
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

