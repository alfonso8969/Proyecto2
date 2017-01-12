package alfonso8969.com.proyecto2.activities.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import alfonso8969.com.proyecto2.R;
import alfonso8969.com.proyecto2.fragments.clientes.ClientesListaFragment;
import alfonso8969.com.proyecto2.fragments.clientes.Clientes_ItemsFragment;

public class ClientesActivity extends AppCompatActivity implements ClientesListaFragment.CallBacksClientes,ClientesListaFragment.Refresh {
    public static boolean mTwoPane;
    private static  final String ELEMENTS_TAG = "ELEMENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        if(findViewById(R.id.clientes_list_item) != null ){
            mTwoPane =true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.clientes_list_item,new Clientes_ItemsFragment(), ELEMENTS_TAG)
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
    public void onItemSelected(String clienteId) {
        if(getmTwoPane()){
            Bundle bundle = new Bundle();
            bundle.putString("clienteId",clienteId);
            Clientes_ItemsFragment itemsFragment = new Clientes_ItemsFragment();
            itemsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.clientes_list_item,itemsFragment).
                    commit();
        }
        else{
            Intent intent = new Intent(this,Clientes_itemsList.class);
            intent.putExtra("clienteId",clienteId);
            startActivity(intent);
        }

    }

    @Override
    public void refreshList() {
        Bundle bundle = new Bundle();
        Clientes_ItemsFragment itemsFragment = new Clientes_ItemsFragment();

        bundle.putBoolean("restart",true);
        itemsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.clientes_list_item,itemsFragment).
                commit();
    }
}
